import Documents.Document;
import PlansExecutionDocuments.DocumentsSender;
import PlansExecutionDocuments.RegisterHandler;
import PlansExecutionDocuments.SvedeniaHandler;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SAIPStressTest {
    private List<Document> documentList;

    public static void main(String[] args) {
        new SAIPStressTest().doRunStressTest();
    }

    private void doRunStressTest() {
        try {
            sendDocumentsToUspn();
//            SvedeniaHandler.getInstance().runHandler(documentList);
//            RegisterHandler.getInstance().runHandler(documentList);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            saveDocumentsToJson();
        }
    }

    private void saveDocumentsToJson() {
        String json = new GsonBuilder().setPrettyPrinting()
                .create().toJson(documentList);

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("result.json"))){
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
