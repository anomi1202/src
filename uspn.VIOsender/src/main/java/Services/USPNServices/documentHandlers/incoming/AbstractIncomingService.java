package Services.USPNServices.documentHandlers.incoming;

import Documents.forJson.IncomingDocument;
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

public abstract class AbstractIncomingService implements IIncomingService {
    private final Logger logger = LoggerFactory.getLogger(AbstractIncomingService.class);
    private final Retrofit retrofit;
    private final IIncomingService inDocService;

    public AbstractIncomingService(String uri, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.inDocService = retrofit.create(IIncomingService.class);
    }

    @Override
    public Call<List<IncomingDocument>> getIncomingDocList(String createDateRangeStart, String documentType) throws Exception {
        Call<List<IncomingDocument>> responseBodyCall = inDocService.getIncomingDocList(createDateRangeStart, documentType);
        logger.trace(String.format("GET INCOMING LIST  with create date: %s", createDateRangeStart));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> checkOnBaseControl(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = inDocService.checkOnBaseControl(json);
        logger.info(String.format("DO CHECK BASE CONTROL DOCUMENT with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<String> createDisposal(String baseDocumentNumber, String baseDocumentDate, String documentIds) throws Exception {
        Call<String> responseBodyCall = inDocService.createDisposal(baseDocumentNumber, baseDocumentDate, documentIds);
        logger.info(String.format("CREATE DISPOSAL %s at %s for REGISTER with IDs: %s"
                , baseDocumentNumber, baseDocumentDate
                , documentIds));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> reflect(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = inDocService.reflect(json);
        logger.info(String.format("REFLECT DOCUMENT with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> rollback(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = inDocService.rollback(json);
        logger.info(String.format("ROLLBACK DOCUMENT with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s\"", responseBodyCall.request().toString()));
        return responseBodyCall;
    }
}
