package USPN_WEB.documentHandlers;

import Documents.incoming.IncomingDocument;
import USPN_WEB.documentHandlers.interfaces.IncomingDocHandlerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.Map;

public class IncomingDocHandlerServiceImpl implements IncomingDocHandlerService {
    private final Logger logger = LoggerFactory.getLogger(IncomingDocHandlerServiceImpl.class);
    private final Retrofit retrofit;
    private final IncomingDocHandlerService inDocService;
    private final Gson gson;

    public IncomingDocHandlerServiceImpl(String uri, OkHttpClient client) {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .build();

        this.inDocService = retrofit.create(IncomingDocHandlerService.class);
    }

    @Override
    public Call<List<IncomingDocument>> getIncomingDocList(String createDateRangeStart, String documentType) throws Exception {
        Call<List<IncomingDocument>> incomingDocList = inDocService.getIncomingDocList(createDateRangeStart, documentType);
        logger.info(String.format("Request to USPN: %s", incomingDocList.request().toString()));
        return incomingDocList;
    }

    @Override
    public Call<ResponseBody> checkOnBaseControl(Map<String, String> body) throws Exception {
        Call<ResponseBody> responseBodyCall = inDocService.checkOnBaseControl(body);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<ResponseBody> reflect(Map<String, String> body) throws Exception {
        Call<ResponseBody> reflect = inDocService.reflect(body);
        logger.info(String.format("Request to USPN: %s", reflect.request().toString()));
        return reflect;
    }

    @Override
    public Call<ResponseBody> rollback(Map<String, String> body) throws Exception {
        Call<ResponseBody> rollback = inDocService.rollback(body);
        logger.info(String.format("Request to USPN: %s", rollback.request().toString()));
        return rollback;
    }
}
