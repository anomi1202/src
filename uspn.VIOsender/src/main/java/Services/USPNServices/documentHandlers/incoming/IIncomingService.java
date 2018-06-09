package Services.USPNServices.documentHandlers.incoming;

import Documents.forJson.incoming.IncomingDocument;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface IIncomingService {
    /**
     * Получение список док-тов в УСПН на форме входящих документов
     *
     * @param createDateRangeStart дата создания распоряжения - createDateRange.start=04.06.2018
     * @param documentType тип входящего документа (тип в УСПН - реестр/сведения и т.п.)
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
     * Создание распоряжение связанного с входящим документом (реестром)
     *
     * @param baseDocumentNumber дата создания распоряжения - baseDocumentNumber=суды-06-2018
     * @param baseDocumentDate дата распоряжения - baseDocumentDate=2018-06-07
     * @param documentIds ID входящего документа - ocumentIds=9140
     * @return объект Call<T> после execute возвращается список обьектов IncomingDocument
     * */
    @POST("npf/prepareDisposal")
    Call<String> createDisposal(
            @Query("baseDocumentNumber") String baseDocumentNumber,
            @Query("baseDocumentDate") String baseDocumentDate,
            @Query("documentIds") String documentIds
    ) throws Exception;

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
