package USPN_WEB.documentHandlers.interfaces;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface IncomingDocHandlerService {
    /**
     * Получение список док-тов в УСПН на форме входящих документов
     *
     * @param createDateRangeStart дата создания распоряжения - createDateRange.start=04.06.2018
     * @return объект Call<T> с JSON-списком документов
     * */
    @POST("npf/incomingDocuments.json")
    Call<RequestBody> getIncomingDocList(@Query("createDateRange.start") String createDateRangeStart);

    /**
     * Отражение док-тов в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/checkOnBaseControl")
    Call<RequestBody> checkOnBaseControl(@Body Map<String, String> body);

    /**
     * Отражение док-тов в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/reflect")
    Call<RequestBody> reflect(@Body Map<String, String> body);

    /**
     * Отзыв док-тов в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T>
     * */
    @POST("npf/rollback")
    Call<RequestBody> rollback(@Body Map<String, String> body);
}
