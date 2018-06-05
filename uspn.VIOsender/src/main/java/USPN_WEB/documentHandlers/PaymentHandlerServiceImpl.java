package USPN_WEB.documentHandlers;

import Documents.payment.Payment;
import USPN_WEB.documentHandlers.interfaces.PaymentHandlerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

public class PaymentHandlerServiceImpl implements PaymentHandlerService {
    private Logger logger = LoggerFactory.getLogger(PaymentHandlerServiceImpl.class);
    private Retrofit retrofit;
    private PaymentHandlerService paymentService;
    private Gson gson;

    public PaymentHandlerServiceImpl(String uri, OkHttpClient client) {
        this.gson = new GsonBuilder()
                .setLenient()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(this.gson))
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
    public Call<ResponseBody> paymentsLinkToDocument(Map<String, String> body) throws Exception {
        Call<ResponseBody> paymentsLinkToDocument = paymentService.paymentsLinkToDocument(body);
        logger.info(String.format("Request to USPN: %s", paymentsLinkToDocument.request().toString()));
        return paymentsLinkToDocument;
    }

    @Override
    public Call<ResponseBody> paymentsLinkToDisposal(Map<String, String> body) throws Exception {
        Call<ResponseBody> paymentsLinkToDisposal = paymentService.paymentsLinkToDisposal(body);
        logger.info(String.format("Request to USPN: %s", paymentsLinkToDisposal.request().toString()));
        return paymentsLinkToDisposal;
    }

    @Override
    public Call<ResponseBody> confirmPaymentOrders(String documentId) throws Exception {
        Call<ResponseBody> confirmPaymentOrders = paymentService.confirmPaymentOrders(documentId);
        logger.info(String.format("Request to USPN: %s", confirmPaymentOrders.request().toString()));
        return confirmPaymentOrders;
    }
}
