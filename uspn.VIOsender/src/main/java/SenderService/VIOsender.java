package SenderService;

import Enums.DocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VIOsender {
    private static Logger logger = LoggerFactory.getLogger(VIOsender.class);
    private Map<File, DocumentType> docMap;
    private File upp;
    private SenderServiceBuilder sendService;

    public static VIOsender newInstance(){
        VIOsender sender = new VIOsender();
        sender.sendService = SenderServiceBuilder.create();
        logger.info("Sender service is ON!");
        return sender;
    }

    public VIOsender documentToSend(Path docToSend, int typeNum){
        DocumentType typeName = DocumentType.getName(typeNum);

        docMap = new HashMap<>();
        docMap.put(docToSend.toFile(), DocumentType.getName(typeNum));
        logger.info(String.format("Add document to send: %s %s", docToSend.getFileName(), typeName));

        return this;
    }

    public VIOsender documentsToSend(Map<Path, Integer> documentsMap){
        HashMap<File, DocumentType> map = new HashMap<>();
        documentsMap.forEach((docToSend, typeNum) -> {
            DocumentType typeName = DocumentType.getName(typeNum);
            map.put(docToSend.toFile(), typeName);
            logger.info(String.format("Add document to send: %s %s", docToSend.getFileName(), typeName));
        });
        docMap = map;

        return this;
    }

    public VIOsender upp(Path upp){
        this.upp = upp.toFile();
        logger.info(String.format("Add upp to send: %s ", upp.getFileName()));

        return this;
    }

    public Map<File, String> send(){
        Map<File, String> request = null;
        try {
            request = sendService.send(docMap, upp);
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return request;
    }
}
