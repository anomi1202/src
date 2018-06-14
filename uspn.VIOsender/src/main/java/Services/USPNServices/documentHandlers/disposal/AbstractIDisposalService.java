package Services.USPNServices.documentHandlers.disposal;

import Documents.forJson.DisposalDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.List;

public abstract class AbstractIDisposalService implements IDisposalService {
    private Logger logger = LoggerFactory.getLogger(AbstractIDisposalService.class);
    private Retrofit retrofit;
    private IDisposalService disposalService;

    public AbstractIDisposalService(String uri, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        disposalService = retrofit.create(IDisposalService.class);
    }

    @Override
    public Call<List<DisposalDocument>> getDisposalList(String createDateRangeStart) throws Exception {
        Call<List<DisposalDocument>> responseBodyCall = disposalService.getDisposalList(createDateRangeStart);
        logger.trace(String.format("GET DISPOSAL LIST  with create date: %s", createDateRangeStart));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<String> confirmDisposal(long baseDocumentId, String baseDocumentNumber, String baseDocumentDate) throws Exception {
        Call<String> responseBodyCall = disposalService.confirmDisposal(baseDocumentId, baseDocumentNumber, baseDocumentDate);
        logger.info(String.format("CONFIRM DISPOSAL with ID: %s\r\n\t" +
                        "new number: %s\r\n\t" +
                        "new date: %s."
                , baseDocumentId
                , baseDocumentNumber
                , baseDocumentDate));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> reflectDisposal(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = disposalService.reflectDisposal(json);
        logger.info(String.format("REFLECT DISPOSALS with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> rollbackDisposal(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = disposalService.rollbackDisposal(json);
        logger.info(String.format("ROLLBACK DISPOSALS with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }
}
