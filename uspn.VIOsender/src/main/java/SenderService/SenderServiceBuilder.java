package SenderService;

import Enums.DocumentType;
import SenderService.SendToUspn.SendServiceImpl;
import SenderService.SendToUspn.interfaces.SendService;
import SenderService.UploadToVio.UploadServiceImpl;
import SenderService.UploadToVio.interfaces.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SenderServiceBuilder {
    private final Logger logger = LoggerFactory.getLogger(SenderServiceBuilder.class);
    private final String VIOSENDER_PROPERTIES = "USPN.properties";
    private final String DEFAULT_CONTEXT = "vio-emulator";
    private final int DEFAULT_THREAD_COUNT = 10;

    private UploadService uploadService;
    private SendService sendService;

    private String host;
    private int port;
    private String context;

    private Map<File, DocumentType> docMap;
    private File upp;

    private SenderServiceBuilder() throws IOException {
        this.context = DEFAULT_CONTEXT;
        initProp();
    }

    public static SenderServiceBuilder newInstance() {
        try {
            return new SenderServiceBuilder();
        } catch (IOException e) {
            return null;
        }
    }

    public SenderServiceBuilder build() {
        if (host != null
                && port > 0 && port < 65536){
            String uri = "http://" + host + ":" + port + "/" + context + "/";
            logger.info(String.format("Generate URL link to webApp: %s", uri));

            uploadService = new UploadServiceImpl(uri, DEFAULT_THREAD_COUNT);
            sendService = new SendServiceImpl(uri, DEFAULT_THREAD_COUNT);
            return this;
        } else {
            return null;
        }
    }

    public SenderServiceBuilder documentToSend(Path docToSend, int typeNum){
        try {
            DocumentType typeName = DocumentType.getName(typeNum);

            docMap = new HashMap<>();
            docMap.put(docToSend.toFile(), DocumentType.getName(typeNum));
            logger.info(String.format("Add document to send: %s %s", docToSend.getFileName(), typeName));

            return this;
        } catch (Exception e){
            logger.error("FAILED", e);
            return null;
        }
    }

    public SenderServiceBuilder documentsToSend(Map<Path, Integer> documentsMap){
        try {
            HashMap<File, DocumentType> map = new HashMap<>();
            documentsMap.forEach((docToSend, typeNum) -> {
                DocumentType typeName = DocumentType.getName(typeNum);
                map.put(docToSend.toFile(), typeName);
                logger.info(String.format("Add document to send: %s %s", docToSend.getFileName(), typeName));
            });
            docMap = map;

            return this;
        } catch (Exception e) {
            logger.error("FAILED", e);
            return null;
        }
    }

    public SenderServiceBuilder upp(Path upp){
        try {
            this.upp = upp.toFile();
            logger.info(String.format("Add upp to send: %s ", upp.getFileName()));

            return this;
        } catch (Exception e){
            logger.error("FAILED", e);
            return null;
        }
    }

    public Map<File, String> send() {
        Map<File, String> requestMap = new HashMap<>();

        try {
            String uppId = uploadService.upload(upp);
            Map<File, String> filesId = uploadService.upload(new ArrayList<>(docMap.keySet()));

            Map<String, DocumentType> uploadedFiles = new HashMap<>();
            filesId.forEach((file, id) -> uploadedFiles.put(id, docMap.get(file)));

            Map<String, String> sendedFiles = sendService.send(uploadedFiles, uppId);
            sendedFiles.forEach((fileId, request) ->
                    filesId.forEach((file, id) -> {
                        if (id.equals(fileId)) {
                            requestMap.put(file, request);
                        }
                    })
            );
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return requestMap;
    }

    private void initProp() throws IOException, NumberFormatException {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(VIOSENDER_PROPERTIES))) {
            prop.load(is);

            host = prop.getProperty("vio.host");
            port = Integer.parseInt(prop.getProperty("vio.port"));
            context = prop.getProperty("vio.context");

            logger.info(String.format("Read properties file: %s" +
                    "\r\n\thost: %s" +
                    "\r\n\tport: %s" +
                    "\r\n\tcontext: %s"
                    , VIOSENDER_PROPERTIES, host, port, context));
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
            throw e;
        }
    }
}
