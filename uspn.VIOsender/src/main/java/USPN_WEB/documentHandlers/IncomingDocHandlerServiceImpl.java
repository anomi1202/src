package USPN_WEB.documentHandlers;

import USPN_WEB.documentHandlers.interfaces.IncomingDocHandlerService;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

public class IncomingDocHandlerServiceImpl implements IncomingDocHandlerService {
    private Logger logger = LoggerFactory.getLogger(IncomingDocHandlerServiceImpl.class);
    private Retrofit retrofit;
    private IncomingDocHandlerService inDocService;

    public IncomingDocHandlerServiceImpl(String uri) {
        retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inDocService = retrofit.create(IncomingDocHandlerServiceImpl.class);
    }

    @Override
    public Call<RequestBody> getIncomingDocList(String createDateRangeStart) {
        return inDocService.getIncomingDocList(createDateRangeStart);
    }

    @Override
    public Call<RequestBody> checkOnBaseControl(Map<String, String> body) {
        return inDocService.checkOnBaseControl(body);
    }

    @Override
    public Call<RequestBody> reflect(Map<String, String> body) {
        return inDocService.reflect(body);
    }

    @Override
    public Call<RequestBody> rollback(Map<String, String> body) {
        return inDocService.rollback(body);
    }
}
