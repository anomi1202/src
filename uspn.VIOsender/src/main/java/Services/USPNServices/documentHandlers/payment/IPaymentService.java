package Services.USPNServices.documentHandlers.payment;

import Documents.forJson.payment.Payment;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface IPaymentService {
    /**
     * Cоздание ПП в УСПН
     *
     * @param newPayment отправляемый обьект. Отправляется как JSON
     *             {
     *                  "number":"3",
     *                  "date":"04.06.2018",
     *                  "amount":3
     *             }
     * @return объект Call<T> после execute возвращается обьект Payment
     * */
    @POST("payments/new")
    Call<Payment> createPayment(@Body Payment newPayment) throws Exception;

    /**
     * Получение списка ПП у документа в УСПН
     *
     * @param baseDocumentId отправляемый обьект. Отправляется как JSON
     *             {
     *                  "id":"",
     *                  "number":"3",
     *                  "date":"04.06.2018",
     *                  "amount":3
     *             }
     * @return объект Call<T> после execute список обьектов Payment
     * */
    @POST("payments/getPayments.json")
    Call<List<Payment>> paymentsOfDocument(@Query("baseDocumentId") long baseDocumentId) throws Exception;

    /**
     * Линковка ПП с распоряжением в УСПН
     *
     * @param json JSON с ID документа и ПП, отправляемых на линковку - {"baseDocumentId":7474,"ids":[1372]}
     *             baseDocumentId - распоряжение
     *             ids - массив ПП
     * @return объект Call<T>. После execute возвращается boolean = true
     * */
    @POST("/uspn/forms//payments/linkToDocument")
    Call<Boolean> paymentsLinkToDocument(@Body JsonObject json) throws Exception;

    /**
     * Линковка ПП с распоряжением в УСПН
     *
     * @param json JSON с ID распоряжения и ПП, отправляемых на линковку - {"baseDocumentId":7474,"ids":[1372]}
     *             baseDocumentId - распоряжение
     *             ids - массив ПП
     * @return объект Call<T>. После execute возвращается boolean = true
     * */
    @POST("/uspn/forms//payments/linkToDisposal")
    Call<Boolean> paymentsLinkToDisposal(@Body JsonObject json) throws Exception;

    /**
     * Подтверждение ПП в УСПН
     *
     * @param documentId ID подтверждаемого реестра/распоряжения
     * @return объект Call<T>. После execute возвращается boolean = true
     * */
    @POST("npf/confirmPaymentOrders")
    Call<Boolean> confirmPaymentOrders(@Query("documentId") long documentId) throws Exception;

}
