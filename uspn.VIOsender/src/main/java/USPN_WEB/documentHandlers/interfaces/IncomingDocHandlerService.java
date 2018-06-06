package USPN_WEB.documentHandlers.interfaces;

import Documents.incoming.IncomingDocument;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface IncomingDocHandlerService {
    /**
     * Получение список док-тов в УСПН на форме входящих документов
     *
     * @param createDateRangeStart дата создания распоряжения - createDateRange.start=04.06.2018
     * @return объект Call<T> после execute возвращается список обьектов IncomingDocument
     * */
    @POST("npf/incomingDocuments.json")
    Call<List<IncomingDocument>> getIncomingDocList(
            @Query("createDocumentDateRange.start") String createDateRangeStart,
            @Query("documentType") String documentType
    ) throws Exception;

    /**
     * Проверка БК док-тов в УСПН
     *
     * @param json JSON с ID документов, отправляемых на проверку БК - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute всегда возвращается boolean = true
     * */
    @POST("npf/checkOnBaseControl")
    Call<Boolean> checkOnBaseControl(@Body JsonObject json) throws Exception;

    /**
     * Отражение док-тов в УСПН
     *
     * @param json JSON с ID документов, отправляемых на отражение - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute всегда возвращается boolean = true
     * */
    @POST("npf/reflect")
    Call<Boolean> reflect(@Body JsonObject json) throws Exception;

    /**
     * Отзыв док-тов в УСПН
     *
     * @param json JSON с ID документов, отправляемых на отзыв - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T> После execute всегда возвращается boolean = true
     * */
    @POST("npf/rollback")
    Call<Boolean> rollback(@Body JsonObject json) throws Exception;
}
