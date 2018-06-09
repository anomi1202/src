package Services.USPNServices.documentHandlers;

import Documents.forJson.payment.Payment;
import Services.USPNServices.documentHandlers.payment.AbstractPaymentService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class PaymentService extends AbstractPaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(String uri, OkHttpClient client) {
        super(uri, client);
    }

    /**
     * Метод для работы с ПП
     * Создание нового ПП
     * @param newPayment - ПП - у ПП заполнять только номер, дату и сумму
     *        Payment.newPayment().withNumber("123").withDate("15.05.2018").withAmount(123)
     * */
    public Payment createNewPayment(Payment newPayment){
        try {
            Response<Payment> paymentResponse = super.createPayment(newPayment).execute();
            newPayment = paymentResponse.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return newPayment;
    }

    /**
     * Метод для работы с ПП
     * Получение списка ПП слинкованного с распоряжением
     * @param baseDocumentId - ID распоряжения
     * */
    public List<Payment> getPaymentsOfDocument(long baseDocumentId){
        List<Payment> listPaymenyOfDocument = null;
        try{
            listPaymenyOfDocument = super.paymentsOfDocument(baseDocumentId).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listPaymenyOfDocument;
    }

    /**
     * Метод для работы с ПП
     * Линковка ПП с входящим документом
     * @param docId - ID входящаго документа
     * @param paymentList - список ПП которые необходимо слинковать с документом
     * */
    public boolean paymentsLinkToDocument(long docId, List<Payment> paymentList){
        boolean bodyResponse = false;

        List<Long> collectID = paymentList.stream()
                .flatMapToLong(pp -> LongStream.of(pp.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.addProperty("baseDocumentId", docId);
        json.add("ids", new Gson().toJsonTree(collectID));

        try {
            Boolean body = super.paymentsLinkToDocument(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с ПП
     * Линковка ПП с распоряжением
     * @param docId - ID распоряжения
     * @param paymentList - список ПП которые необходимо слинковать с распоряжением
     * */
    public boolean paymentsLinkToDisposal(long docId, List<Payment> paymentList){
        boolean bodyResponse = false;

        List<Long> collectID = paymentList.stream()
                .flatMapToLong(pp -> LongStream.of(pp.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.addProperty("baseDocumentId", docId);
        json.add("ids", new Gson().toJsonTree(collectID));

        try {
            Boolean body = super.paymentsLinkToDisposal(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с ПП
     * Подтверждение ПП в УСПН
     * @param docId - ID реестра/распоряжения
     * */
    public boolean confirmPayments(long docId){
        boolean bodyResponse = false;

        try {
            Boolean body = super.confirmPaymentOrders(docId).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

}
