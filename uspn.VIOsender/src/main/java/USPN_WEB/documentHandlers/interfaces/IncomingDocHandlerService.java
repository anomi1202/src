package USPN_WEB.documentHandlers.interfaces;

import Documents.incoming.IncomingDocument;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface IncomingDocHandlerService {
    /**
     * Получение список док-тов в УСПН на форме входящих документов
     *
     * @param createDateRangeStart дата создания распоряжения - createDateRange.start=04.06.2018
     * @return объект Call<T> с JSON-списком документов
     * */
    @POST("npf/incomingDocuments.json")
    Call<List<IncomingDocument>> getIncomingDocList(
            @Query("createDocumentDateRange.start") String createDateRangeStart,
            @Query("documentType") String documentType
    ) throws Exception;

    /**
     * Отражение док-тов в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/checkOnBaseControl")
    Call<ResponseBody> checkOnBaseControl(@Body Map<String, String> body) throws Exception;

    /**
     * Отражение док-тов в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/reflect")
    Call<ResponseBody> reflect(@Body Map<String, String> body) throws Exception;

    /**
     * Отзыв док-тов в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/rollback")
    Call<ResponseBody> rollback(@Body Map<String, String> body) throws Exception;
}
