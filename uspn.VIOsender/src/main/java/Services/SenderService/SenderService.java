package Services.SenderService;

import Documents.Enums.DocumentType;
import Services.SenderService.SendToUspn.SendServiceImpl;
import Services.SenderService.SendToUspn.interfaces.SendService;
import Services.SenderService.UploadToVio.UploadServiceImpl;
import Services.SenderService.UploadToVio.interfaces.UploadService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class SenderService {
    private final Logger logger = LoggerFactory.getLogger(SenderService.class);
    private final String USPN_PROPERTIES = "USPN.properties";
    private final String DEFAULT_CONTEXT = "vio-emulator";
    private final int DEFAULT_THREAD_COUNT = 10;

    private UploadService uploadService;
    private SendService sendService;

    private String host;
    private int port;
    private String context;

    private Map<File, DocumentType> docMap;
    private File upp;

    private SenderService() throws IOException {
        this.context = DEFAULT_CONTEXT;
        this.docMap = new HashMap<>();
        initProp();
    }

    public static SenderService newInstance() {
        try {
            return new SenderService();
        } catch (IOException e) {
            return null;
        }
    }

    public void build() throws Exception {
        if (host != null
                && port > 0 && port < 65536){
            String uri = "http://" + host + ":" + port + "/" + context + "/";
            logger.info(String.format("Generate URL link to webApp: %s", uri));

            uploadService = new UploadServiceImpl(uri, DEFAULT_THREAD_COUNT);
            sendService = new SendServiceImpl(uri, DEFAULT_THREAD_COUNT);

            logger.info("START sender service!");
        } else {
            throw new Exception("URI is incorrect!");
        }
    }

    public SenderService documentToSend(File docToSend, DocumentType type){
        try {
            docMap = new HashMap<>();
            docMap.put(docToSend, type);
            logger.info(String.format("Add document to send: %s %s", docToSend.getName(), type));

            return this;
        } catch (Exception e){
            logger.error("FAILED", e);
            return null;
        }
    }

    public SenderService documentsToSend(Map<File, DocumentType> documentsMap){
        try {
            documentsMap.forEach((docToSend, type) -> {
                docMap.put(docToSend, type);
                logger.info(String.format("Add document to send: %s %s", docToSend.getName(), type));
            });

            return this;
        } catch (Exception e) {
            logger.error("FAILED", e);
            return null;
        }
    }

    public SenderService upp(File upp){
        try {
            this.upp = upp;
            logger.info(String.format("Add upp to send: %s ", upp.getName()));

            return this;
        } catch (Exception e){
            logger.error("FAILED", e);
            return null;
        }
    }

    public Map<File, Boolean> send() {
        Map<File, Boolean> requestMap = null;

        try {
            String uppId = uploadService.upload(upp);
            //Map of pair File-ID
            Map<File, String> filesId = uploadService.upload(new ArrayList<>(docMap.keySet()));

            //Map of pair ID-DocumentType
            Map<String, DocumentType> uploadedFiles = filesId.entrySet().stream()
                    .collect(Collectors.toMap(
                            pair -> pair.getValue(),
                            pair -> docMap.get(pair.getKey())
                    ));

            //Map of pair ID-Message
            Map<String, String> sendedFiles = sendService.send(uploadedFiles, uppId);
            //Map of pair File-Message
            requestMap = filesId.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            pair -> {
                                String jsonRequest = sendedFiles.get(pair.getValue());
                                JsonObject json = new Gson().fromJson(jsonRequest, JsonObject.class);
                                return json.get("message").getAsString().equals("send NPF document");
                            }));
        } catch (Exception e) {
            logger.error("FAILED", e);
        }


        logger.info("STOP sender service!");
        return requestMap;
    }

    private void initProp() throws IOException, NumberFormatException {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(USPN_PROPERTIES))) {
            prop.load(is);

            host = prop.getProperty("vio.host");
            port = Integer.parseInt(prop.getProperty("vio.port"));
            context = prop.getProperty("vio.context");

            logger.info(String.format("Read properties file: %s" +
                    "\r\n\thost: %s" +
                    "\r\n\tport: %s" +
                    "\r\n\tcontext: %s"
                    , USPN_PROPERTIES, host, port, context));
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
            throw e;
        }
    }
}
