package USPN_WEB.documentHandlers;

import Documents.payment.Payment;
import USPN_WEB.documentHandlers.interfaces.PaymentHandlerService;
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

public class PaymentHandlerServiceImpl implements PaymentHandlerService {
    private Logger logger = LoggerFactory.getLogger(PaymentHandlerServiceImpl.class);
    private Retrofit retrofit;
    private PaymentHandlerService paymentService;

    public PaymentHandlerServiceImpl(String uri, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        paymentService = retrofit.create(PaymentHandlerService.class);
    }

    @Override
    public Call<Payment> createPayment(Payment newPayment) throws Exception {
        Call<Payment> payment = paymentService.createPayment(newPayment);
        logger.info(String.format("Request to USPN: %s", payment.request().toString()));
        return payment;
    }

    @Override
    public Call<List<Payment>> getPaymentsOfDocument(String baseDocumentId) throws Exception {
        Call<List<Payment>> paymentsOfDocument = paymentService.getPaymentsOfDocument(baseDocumentId);
        logger.info(String.format("Request to USPN: %s", paymentsOfDocument.request().toString()));
        return paymentsOfDocument;
    }

    @Override
    public Call<Boolean> paymentsLinkToDocument(JsonObject json) throws Exception {
        Call<Boolean> paymentsLinkToDocument = paymentService.paymentsLinkToDocument(json);
        logger.info(String.format("Request to USPN: %s", paymentsLinkToDocument.request().toString()));
        return paymentsLinkToDocument;
    }

    @Override
    public Call<Boolean> paymentsLinkToDisposal(JsonObject json) throws Exception {
        Call<Boolean> paymentsLinkToDisposal = paymentService.paymentsLinkToDisposal(json);
        logger.info(String.format("Request to USPN: %s", paymentsLinkToDisposal.request().toString()));
        return paymentsLinkToDisposal;
    }

    @Override
    public Call<Boolean> confirmPaymentOrders(String documentId) throws Exception {
        Call<Boolean> confirmPaymentOrders = paymentService.confirmPaymentOrders(documentId);
        logger.info(String.format("Request to USPN: %s", confirmPaymentOrders.request().toString()));
        return confirmPaymentOrders;
    }
}
