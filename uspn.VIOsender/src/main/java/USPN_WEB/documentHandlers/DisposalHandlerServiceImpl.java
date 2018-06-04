package USPN_WEB.documentHandlers;

import USPN_WEB.documentHandlers.interfaces.DisposalHandlerService;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

public class DisposalHandlerServiceImpl implements DisposalHandlerService {
    private Logger logger = LoggerFactory.getLogger(DisposalHandlerServiceImpl.class);
    private Retrofit retrofit;
    private DisposalHandlerService disposalService;

    public DisposalHandlerServiceImpl(String uri) {
        retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        disposalService = retrofit.create(DisposalHandlerService.class);
    }

    @Override
    public Call<RequestBody> getDisposalList(String createDateRangeStart) {
        return disposalService.getDisposalList(createDateRangeStart);
    }

    @Override
    public Call<RequestBody> confirmDisposal(String baseDocumentId, String baseDocumentNumber, String baseDocumentDate) {
        return disposalService.confirmDisposal(baseDocumentId, baseDocumentNumber, baseDocumentDate);
    }

    @Override
    public Call<RequestBody> reflectDisposal(Map<String, String> body) {
        return disposalService.reflectDisposal(body);
    }

    @Override
    public Call<RequestBody> rollbackDisposal(Map<String, String> body) {
        return disposalService.rollbackDisposal(body);
    }
}
