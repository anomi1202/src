package USPN_WEB.documentHandlers;

import Documents.disposal.DisposalDocument;
import USPN_WEB.documentHandlers.interfaces.DisposalHandlerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.javafx.css.converters.StringConverter;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.swing.table.TableStringConverter;
import java.util.List;

public class DisposalHandlerServiceImpl implements DisposalHandlerService {
    private Logger logger = LoggerFactory.getLogger(DisposalHandlerServiceImpl.class);
    private Retrofit retrofit;
    private DisposalHandlerService disposalService;

    public DisposalHandlerServiceImpl(String uri, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
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
    public Call<String> confirmDisposal(long baseDocumentId, String baseDocumentNumber, String baseDocumentDate) throws Exception {
        Call<String> responseBodyCall = disposalService.confirmDisposal(baseDocumentId, baseDocumentNumber, baseDocumentDate);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> reflectDisposal(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = disposalService.reflectDisposal(json);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> rollbackDisposal(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = disposalService.rollbackDisposal(json);
        logger.info(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }
}
