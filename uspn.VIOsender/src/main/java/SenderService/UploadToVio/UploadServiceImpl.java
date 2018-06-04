package SenderService.UploadToVio;

import SenderService.UploadToVio.interfaces.UploadService;
import SenderService.UploadToVio.interfaces.UploadServiceRest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadServiceImpl implements UploadServiceRest, UploadService {
    private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
    private final int threadCount;
    private Retrofit retrofit;
    private UploadServiceRest uploadService;

    public UploadServiceImpl(String uri, int threadCount) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.uploadService = retrofit.create(UploadServiceRest.class);
        this.threadCount = threadCount;
    }

    @Override
    public Call<ResponseBody> postRequest(MultipartBody.Part file) throws Exception {
        return uploadService.postRequest(file);
    }

    @Override
    public String upload(File file) throws Exception {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName() , body);

        Call<ResponseBody> responseBodyCall = postRequest(multipartBody);
        Response<ResponseBody> executeResult = responseBodyCall.execute();
        String requestMessage = executeResult.isSuccessful() ? executeResult.body().string().replaceAll("\"", "") : executeResult.errorBody().string();
        logger.info(String.format("Upload service:" +
                        "\r\n\t%s method to URL: %s" +
                        "\r\n\tUploading file: %s" +
                        "\r\n\tRequest message: %s"
                , responseBodyCall.request().method()
                , responseBodyCall.request().url()
                , file.getName()
                , requestMessage
        ));

        return requestMessage;
    }

    @Override
    public Map<File, String> upload(List<File> files) throws Exception {
        Map<File, String> resultMap = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (File file : files) {
            resultMap.put(file, executorService.submit(upload(file)::toString).get().replaceAll("\"", ""));
        }

        executorService.shutdown();
        return resultMap;
    }

}
