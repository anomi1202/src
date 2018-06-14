package Services.SenderService.SendToUspn;

import Documents.Enums.DocumentType;
import Services.SenderService.SendToUspn.interfaces.SendService;
import Services.SenderService.SendToUspn.interfaces.SendServiceRest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendServiceImpl implements SendService, SendServiceRest {
    private Logger logger = LoggerFactory.getLogger(SendServiceImpl.class);
    private final int threadCount;
    private Retrofit retrofit;
    private SendServiceRest sendService;

    public SendServiceImpl(String uri, int threadCount) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.sendService = retrofit.create(SendServiceRest.class);
        this.threadCount = threadCount;
    }

    @Override
    public Call<String> send(JsonObject json) throws Exception {
        return sendService.send(json);
    }

    @Override
    public String send(String fileId, DocumentType type, String uppId) throws Exception {
        String requestMessage = null;

        JsonObject json = new JsonObject();
        json.addProperty("uppFileId", uppId);
        json.addProperty("documentFileId", fileId);
        json.addProperty("documentType", type.name());

        Call<String> responseBodyCall = send(json);
        Response<String> execute = responseBodyCall.execute();

        if (execute.isSuccessful()) {
            requestMessage = execute.body();
        } else {
            throw new Exception(execute.errorBody().string());
        }

        logger.info(String.format("Send service. Send file %s with ID to USPN: %s", type.getCyrillicName(), fileId));
        logger.trace(String.format("Send service:" +
                        "\r\n\t%s method to URL: %s" +
                        "\r\n\tSending file ID: %s" +
                        "\r\n\tSending UPP ID: %s" +
                        "\r\n\tRequest message: %s"
                , responseBodyCall.request().method()
                , responseBodyCall.request().url()
                , fileId
                , uppId
                , requestMessage
        ));
        return requestMessage;
    }

    @Override
    public Map<String, String> send(Map<String, DocumentType> files, String uppId) throws Exception {
        Map<String, String> result = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (Map.Entry<String, DocumentType> filePair: files.entrySet()) {
            String fileId = filePair.getKey();
            DocumentType docTypeName = filePair.getValue();

            String sendResult = executorService.submit(send(fileId, docTypeName, uppId)::toString).get();
            result.put(fileId, sendResult);
        }

        executorService.shutdown();
        return result;
    }
}
