package SnderService;

import Enums.DocumentType;
import SnderService.SendToUspn.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VIOsender {
    private Logger logger = LoggerFactory.getLogger(VIOsender.class);
    private Map<File, DocumentType> docMap;
    private File docToSend;
    private File upp;
    private int docTypeNum;
    private SendService vioService;

    public static VIOsender newInstance(){
        VIOsender sender = new VIOsender();
        sender.vioService = SenderServiceBuilder.create();

        return sender;
    }

    public VIOsender documentToSend(Path docToSend, int docTypeNum){
        this.docToSend = docToSend.toFile();
        this.docTypeNum = docTypeNum;

        return this;
    }

    public VIOsender documentsToSend(Map<Path, Integer> docMap){
        HashMap<File, DocumentType> map = new HashMap<>();
        docMap.forEach((path, typeNum) -> map.put(path.toFile(), DocumentType.getName(typeNum)));
        this.docMap = map;
        this.docToSend = null;

        return this;
    }

    public VIOsender upp(Path upp){
        this.upp = upp.toFile();

        return this;
    }

    public Map<File, String> send(){
        Map<File, String> request = null;
        try {
            if (docToSend != null) {
                request = new HashMap<>();
                request.put(docToSend, vioService.sendToUspn(docToSend, upp, DocumentType.getName(docTypeNum)));
            } else {
                request = vioService.sendToUspn(docMap, upp);
            }
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return request;
    }
}
