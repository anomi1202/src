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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SenderServiceBuilder {
    private final Logger logger = LoggerFactory.getLogger(SenderServiceBuilder.class);
    private final String VIOSENDER_PROPERTIES = "VIO.properties";
    private final String DEFAULT_CONTEXT = "vio-emulator";
    private final int DEFAULT_THREAD_COUNT = 10;
    private UploadService uploadService;
    private SendService sendService;

    private String host;
    private int port;
    private String context;
    private int threadCount;

    private SenderServiceBuilder() {
        this.context = DEFAULT_CONTEXT;
        this.threadCount = DEFAULT_THREAD_COUNT;
        initProp();
    }

    public static SenderServiceBuilder create() {
        return new SenderServiceBuilder().build();
    }

    private SenderServiceBuilder build() {
        if (host != null
                && port > 0 && port < 65536){
            String uri = "http://" + host + ":" + port + "/" + context + "/";
            logger.info(String.format("Generate URL link to webApp: %s", uri));

            uploadService = new UploadServiceImpl(uri, threadCount);
            sendService = new SendServiceImpl(uri, threadCount);
            return this;
        } else {
            return null;
        }
    }

    public Map<File, String> send(Map<File, DocumentType> fileMap, File upp) throws Exception {
        Map<File, String> requestMap = new HashMap<>();

        String uppId = uploadService.upload(upp);
        Map<File, String> filesId = uploadService.upload(new ArrayList<>(fileMap.keySet()));

        Map<String, DocumentType> uploadedFiles = new HashMap<>();
        filesId.forEach((file, id) -> uploadedFiles.put(id, fileMap.get(file)));

        Map<String, String> sendedFiles = sendService.send(uploadedFiles, uppId);
        sendedFiles.forEach((fileId, request) ->
                filesId.forEach((file, id) -> {
                    if (id.equals(fileId)) {
                        requestMap.put(file, request);
                    }
                })
        );

        return requestMap;
    }

    private void initProp() {
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
        }
    }
}
