package Services.USPNServices.documentHandlers.payment;

import Documents.forJson.Payment;
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

public abstract class AbstractPaymentService implements IPaymentService {
    private Logger logger = LoggerFactory.getLogger(AbstractPaymentService.class);
    private Retrofit retrofit;
    private IPaymentService paymentService;

    public AbstractPaymentService(String uri, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        paymentService = retrofit.create(IPaymentService.class);
    }

    @Override
    public Call<Payment> createPayment(Payment newPayment) throws Exception {
        Call<Payment> responseBodyCall = paymentService.createPayment(newPayment);
        logger.info(String.format("CREATE new PAYMENT with:" +
                "number: %s\r\n\t" +
                "date: %s\r\n\t" +
                "admount: %d"
                , newPayment.getNumber()
                , newPayment.getDate()
                , newPayment.getAmount()
        ));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<List<Payment>> paymentsOfDocument(long baseDocumentId) throws Exception {
        Call<List<Payment>> responseBodyCall = paymentService.paymentsOfDocument(baseDocumentId);
        logger.trace(String.format("GET PAYMENT LIST of DOCUMENT with ID: %d", baseDocumentId));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> paymentsLinkToDocument(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = paymentService.paymentsLinkToDocument(json);
        logger.info(String.format("LINK PAYMENTS TO DOCUMENT with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> paymentsLinkToDisposal(JsonObject json) throws Exception {
        Call<Boolean> responseBodyCall = paymentService.paymentsLinkToDisposal(json);
        logger.info(String.format("LINK PAYMENTS TO DISPOSAL with IDs: %s", json.toString()));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }

    @Override
    public Call<Boolean> confirmPaymentOrders(long documentId) throws Exception {
        Call<Boolean> responseBodyCall = paymentService.confirmPaymentOrders(documentId);
        logger.info(String.format("CONFIRM PAYMENT ORDERS with IDs: %d", documentId));
        logger.trace(String.format("Request to USPN: %s", responseBodyCall.request().toString()));
        return responseBodyCall;
    }
}
