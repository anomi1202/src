package USPN_WEB.documentHandlers.interfaces;

import Documents.disposal.DisposalDocument;
import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

public interface DisposalHandlerService {
    /**
     * Получение список док-тов в УСПН на форме входящих документов
     *
     * @param createDateRangeStart дата создания распоряжения - createDateRange.start=04.06.2018
     * @return объект Call<T> после execute возвращается список обьектов DisposalDocument
     * */
    @POST("npf/internalDocuments.json")
    Call<List<DisposalDocument>> getDisposalList(@Query("createDateRange.start") String createDateRangeStart) throws Exception;


    /**
     * Подтвердить распряжение в УСПН
     *
     * @param baseDocumentId ID подтверждаемого распоряжения
     * @param baseDocumentNumber номер подтверждаемого распоряжения
     * @param baseDocumentDate дата подтверждаемого распоряжения
     *
     * комбинация, не должна совпадать с ранее созданным распоряжением
     *      baseDocumentNumber - baseDocumentDate
     *
     * @return объект Call<T>. После execute возвращается string = ""
     * */
    @POST("npf/confirmDisposal")
    Call<String> confirmDisposal(
            @Query("baseDocumentId") long baseDocumentId,
            @Query("baseDocumentNumber") String baseDocumentNumber,
            @Query("baseDocumentDate") String baseDocumentDate
    )throws Exception;

    /**
     * Отражение распоряжения в УСПН
     *
     * @param json JSON с ID распоряжений, отправляемых на отражение - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T>. После execute возвращается boolean = true
     * */
    @POST("npf/reflectDisposal")
    Call<Boolean> reflectDisposal(@Body JsonObject json) throws Exception;

    /**
     * Озыв распоряжения в УСПН
     *
     * @param json JSON с ID распоряжений, отправляемых на отзыв - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T>. После execute возвращается boolean = true
     * */
    @POST("npf/rollbackDisposal")
    Call<Boolean> rollbackDisposal(@Body JsonObject json) throws Exception;

}
