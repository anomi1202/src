package USPN_WEB.documentHandlers;

import Documents.disposal.DisposalDocument;
import USPN_WEB.documentHandlers.interfaces.DisposalHandlerService;
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

public class DisposalHandlerServiceImpl implements DisposalHandlerService {
    private Logger logger = LoggerFactory.getLogger(DisposalHandlerServiceImpl.class);
    private Retrofit retrofit;
    private DisposalHandlerService disposalService;
    private Gson gson;

    public DisposalHandlerServiceImpl(String uri, OkHttpClient client) {
        this.gson = new GsonBuilder()
                .setLenient()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .build();

        disposalService = retrofit.create(DisposalHandlerService.class);
    }

    @Override
    public Call<List<DisposalDocument>> getDisposalList(String createDateRangeStart) throws Exception {
        Call<List<DisposalDocument>> disposalList = disposalService.getDisposalList(createDateRangeStart);
        logger.info(String.format("Request to USPN: %s", disposalList.request().toString()));
        return disposalList;
    }

    @Override
    public Call<ResponseBody> confirmDisposal(String baseDocumentId, String baseDocumentNumber, String baseDocumentDate) throws Exception {
        Call<ResponseBody> responseBodyCall = disposalService.confirmDisposal(baseDocumentId, baseDocumentNumber, baseDocumentDate);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<ResponseBody> reflectDisposal(Map<String, String> body) throws Exception {
        Call<ResponseBody> responseBodyCall = disposalService.reflectDisposal(body);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<ResponseBody> rollbackDisposal(Map<String, String> body) throws Exception {
        Call<ResponseBody> responseBodyCall = disposalService.rollbackDisposal(body);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }
}
