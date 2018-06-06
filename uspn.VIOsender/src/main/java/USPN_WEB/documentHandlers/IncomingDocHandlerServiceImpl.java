package USPN_WEB.documentHandlers;

import Documents.incoming.IncomingDocument;
import USPN_WEB.documentHandlers.interfaces.IncomingDocHandlerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.List;
import java.util.Map;

public class IncomingDocHandlerServiceImpl implements IncomingDocHandlerService {
    private final Logger logger = LoggerFactory.getLogger(IncomingDocHandlerServiceImpl.class);
    private final Retrofit retrofit;
    private final IncomingDocHandlerService inDocService;

    public IncomingDocHandlerServiceImpl(String uri, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
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
    public Call<Boolean> checkOnBaseControl(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = inDocService.checkOnBaseControl(json);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> reflect(JsonObject json) throws Exception {
        Call<Boolean> reflect = inDocService.reflect(json);
        logger.info(String.format("Request to USPN: %s", reflect.request().toString()));
        return reflect;
    }

    @Override
    public Call<Boolean> rollback(JsonObject json) throws Exception {
        Call<Boolean> rollback = inDocService.rollback(json);
        logger.info(String.format("Request to USPN: %s", rollback.request().toString()));
        return rollback;
    }
}
