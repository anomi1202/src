package USPN_WEB.documentHandlers.interfaces;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface PaymentHandlerService {
    /**
     * Cоздание ПП в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла
     *             {
     *                  "id":"",
     *                  "number":"3",
     *                  "date":"04.06.2018",
     *                  "amount":3
     *             }
     * @return объект Call<T> после execute возвращается JSON
    {
    "id": 1643,
    "number": "3",
    "date": "04.06.2018 00:00:00",
    "amount": 3,
    "description": null,
    "createDate": "04.06.2018",
    "changeDate": null
    }
     * */
    @POST("payments/new")
    Call<RequestBody> createPayment(@Body Map<String, String> body);

    /**
     * Линковка ПП с распоряжением в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"baseDocumentId":7474,"ids":[1372]}
     *             baseDocumentId - распоряжение
     *             ids - массив ПП
     * @return объект Call<T>. После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("#/payments/linkToDocument")
    Call<RequestBody> paymentsLinkToDocument(@Body Map<String, String> body);

    /**
     * Линковка ПП с распоряжением в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"baseDocumentId":7474,"ids":[1372]}
     *             baseDocumentId - распоряжение
     *             ids - массив ПП
     * @return объект Call<T>. После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("#/payments/linkToDisposal")
    Call<RequestBody> paymentsLinkToDisposal(@Body Map<String, String> body);

    /**
     * Подтверждение ПП в УСПН
     *
     * @param documentId ID подтверждаемого реестра/распоряжения
     * @return объект Call<T>. После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/confirmPaymentOrders")
    Call<RequestBody> confirmPaymentOrders(@Query("documentId") String documentId);

}
