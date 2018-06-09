import Documents.Document;
import PlansExecutionDocuments.DocumentsSender;
import PlansExecutionDocuments.RegisterHandler;
import PlansExecutionDocuments.SvedeniaHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SAIPStressTest {
    private List<Document> documentList;

    public static void main(String[] args) {
        new SAIPStressTest().doRunStressTest();
    }

    private void doRunStressTest() {
        sendDocumentsToUspn();
        RegisterHandler.getInstance().runHandler(documentList);
        SvedeniaHandler.getInstance().runHandler(documentList);
    }

    private void sendDocumentsToUspn(){
        DocumentsSender documentsSender = new DocumentsSender();
        documentsSender.initSenderService();
        documentList = documentsSender.sendDocument()
                .entrySet().stream()
                .filter(Map.Entry<Document, Boolean>::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
