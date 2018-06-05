package USPN_WEB;

import Documents.disposal.DisposalDocument;
import Documents.incoming.IncomingDocument;
import Documents.payment.Payment;
import USPN_WEB.documentHandlers.AuthorizationServiceImpl;
import USPN_WEB.documentHandlers.DisposalHandlerServiceImpl;
import USPN_WEB.documentHandlers.IncomingDocHandlerServiceImpl;
import USPN_WEB.documentHandlers.PaymentHandlerServiceImpl;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DocHandlerServiceBuilder extends AbstractHandlerService{
    private final Logger logger = LoggerFactory.getLogger(DocHandlerServiceBuilder.class);

    private IncomingDocHandlerServiceImpl inDocService;
    private DisposalHandlerServiceImpl disposalService;
    private PaymentHandlerServiceImpl paymentService;
    private AuthorizationServiceImpl authService;
    private OkHttpClient client;

    private DocHandlerServiceBuilder() {
        this.context = DEFAULT_CONTEXT;
        initProp();
    }

    public static DocHandlerServiceBuilder create() {
        return new DocHandlerServiceBuilder().build();
    }

    private DocHandlerServiceBuilder build() {
        String uri = null;
        if (host != null
                && port > 0 && port < 65536) {
            uri = "http://" + host + ":" + port + "/" + context + "/";
            logger.info(String.format("Generate URL link to webApp: %s", uri));
        }

        try {
            client = new OkHttpClient.Builder().cookieJar(initCookieJar()).build();

            authService = new AuthorizationServiceImpl(uri, client);
            authService.getAuthorizationCookies();

            inDocService = new IncomingDocHandlerServiceImpl(uri, client);
            disposalService = new DisposalHandlerServiceImpl(uri, client);
            paymentService = new PaymentHandlerServiceImpl(uri, client);

            return this;
        } catch (Exception e) {
            return null;
        }
    }

    public List<IncomingDocument> getIncomingDocList(String createDateRangeStart){
        List<IncomingDocument> incomingDocuments = null;
        try {
            Response<List<IncomingDocument>> v_documents = inDocService.getIncomingDocList(createDateRangeStart, "V_DOCUMENTS").execute();
            incomingDocuments = v_documents.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return incomingDocuments;
    }

    public List<DisposalDocument> getDisposalList(String createDateRangeStart){
        List<DisposalDocument> disposalList = null;
        try {
            Response<List<DisposalDocument>> disposalListResponse = disposalService.getDisposalList(createDateRangeStart).execute();
            disposalList = disposalListResponse.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return disposalList;
    }

    public Payment createNewPayment(){
        JsonObject newPayment = new GsonBuilder().
        return paymentService.createPayment();
    }

    public IncomingDocHandlerServiceImpl inDocHandlerExecute(){
        return inDocService;
    }

    public DisposalHandlerServiceImpl disposalHandlerExecute(){
        return disposalService;
    }

    public PaymentHandlerServiceImpl paymentHandlerExecute(){
        return paymentService;
    }

}
