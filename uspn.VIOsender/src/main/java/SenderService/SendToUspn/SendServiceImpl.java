package SenderService.SendToUspn;

import Enums.DocumentType;
import SenderService.SendToUspn.interfaces.SendService;
import SenderService.SendToUspn.interfaces.SendServiceRest;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.sendService = retrofit.create(SendServiceRest.class);
        this.threadCount = threadCount;
    }

    @Override
    public Call<ResponseBody> send(Map<String, String> body) throws Exception {
        return sendService.send(body);
    }

    @Override
    public String send(String fileId, DocumentType type, String uppId) throws Exception {
        String requestMessage = null;

        Map<String, String> map = new HashMap<>();
        map.put("uppFileId", uppId);
        map.put("documentFileId", fileId);
        map.put("documentType", type.name());

        Call<ResponseBody> responseBodyCall = send(map);
        Response<ResponseBody> execute = responseBodyCall.execute();

        if (execute.isSuccessful()) {
            requestMessage = execute.body().string();
        } else {
            throw new Exception(execute.errorBody().string());
        }

        logger.info(String.format("Send service:" +
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
