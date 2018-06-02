package SnderService.SendToUspn;

import Enums.DocumentType;
import SnderService.UploadToVio.UploadService;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendServiceImpl implements SendService {
    private final String uri;
    private final int threadCount;
    private final UploadService uploadService;
    private String uppID;

    public SendServiceImpl(String uri, int threadCount, UploadService uploadService) {
        this.uri = uri + "/backend/process/sendNpfDocument";
        this.threadCount = threadCount;
        this.uploadService = uploadService;
    }

    private String sendeUPP (File upp){
        if (uppID == null){
            try {
                uppID = uploadService.upload(upp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return uppID;
    }

    private String postRequestExecute(String fildId, String uppId, String docTypeName) throws IOException {
        String json = String.format("{\"uppFileId\": %s,\"documentFileId\": \"%s\",\"documentType\": \"%s\"}"
                , uppId, fildId, docTypeName);
        Request post = Request.Post(uri)
                .addHeader("Content-type", "application/json")
                .bodyByteArray(json.getBytes(), ContentType.APPLICATION_JSON);

        return Executor.newInstance().execute(post).returnContent().asString();
    }

    @Override
    public String sendToUspn(File file, File upp, DocumentType type) throws Exception {
        String uploadedFileID = uploadService.upload(file);
        String uppFileId = sendeUPP(upp);

        return postRequestExecute(uploadedFileID, uppFileId, type.name());
    }


    @Override
    public Map<File, String> sendToUspn(Map<File, DocumentType> files, File upp) throws Exception {
        Map<File, String> result = new HashMap<>();
        Map<File, String> uploadedDoc = uploadService.upload(new ArrayList<>(files.keySet()));
        String uppFileId = sendeUPP(upp);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (Map.Entry<File, DocumentType> pair: files.entrySet()) {
            File file = pair.getKey();
            String uploadedFileID = uploadedDoc.get(file);
            String docTypeName = pair.getValue().name();

            String sendResult = executorService.submit(postRequestExecute(uploadedFileID, uppFileId, docTypeName)::toString).get();
            result.put(file, sendResult);
        }

        executorService.shutdown();
        return result;
    }

}
