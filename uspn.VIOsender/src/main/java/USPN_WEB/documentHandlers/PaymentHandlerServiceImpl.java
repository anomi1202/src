package USPN_WEB.documentHandlers;

import USPN_WEB.documentHandlers.interfaces.PaymentHandlerService;
import okhttp3.RequestBody;
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

    public PaymentHandlerServiceImpl(String uri) {
        retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        paymentService = retrofit.create(PaymentHandlerService.class);
    }
    @Override
    public Call<RequestBody> createPayment(Map<String, String> body) {
        return paymentService.createPayment(body);
    }

    @Override
    public Call<RequestBody> paymentsLinkToDocument(Map<String, String> body) {
        return paymentService.paymentsLinkToDocument(body);
    }

    @Override
    public Call<RequestBody> paymentsLinkToDisposal(Map<String, String> body) {
        return paymentService.paymentsLinkToDisposal(body);
    }

    @Override
    public Call<RequestBody> confirmPaymentOrders(String documentId) {
        return paymentService.confirmPaymentOrders(documentId);
    }
}
