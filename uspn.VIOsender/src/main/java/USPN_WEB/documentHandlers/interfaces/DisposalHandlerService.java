package USPN_WEB.documentHandlers.interfaces;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface DisposalHandlerService {
    /**
     * Получение список док-тов в УСПН на форме входящих документов
     *
     * @param createDateRangeStart дата создания распоряжения - createDateRange.start=04.06.2018
     * @return объект Call<T> с JSON-списком документов
     [
        {
            "id": 8831,
            "number": "nagr-14-05-2018",
            "date": "14.05.2018 00:00:00",
            "operationType": 250,
            "insuredPersonCount": 0,
            "amount": 0,
            "status": {
                "code": 9,
                "clsItem": {
                    "label": "Подготовлен",
                    "shortLabel": null,
                    "extId": null,
                    "form": null,
                    "value": 9
                },
                "temp": false,
                "nameUI": "PREPARED"
            },
            "changeDate": "14.05.2018 08:34:00",
            "createDate": "14.05.2018 06:40:10"
        },
        {another JSON body}
     ]
     * */
    @POST("npf/internalDocuments.json")
    Call<RequestBody> getDisposalList(@Query("createDateRange.start") String createDateRangeStart);


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
    Call<RequestBody> confirmDisposal(
            @Query("baseDocumentId") String baseDocumentId,
            @Query("baseDocumentNumber") String baseDocumentNumber,
            @Query("baseDocumentDate") String baseDocumentDate
    );

    /**
     * Отражение распоряжения в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T>. После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/reflectDisposal")
    Call<RequestBody> reflectDisposal(@Body Map<String, String> body);

    /**
     * Озыв распоряжения в УСПН
     *
     * @param body словарь с парами отправляемого JSON файла - {"documentIds":[9137, 9138, 9139]}
     * @return объект Call<T>. После execute возвращается boolean = true=OK/false=fail
     * */
    @POST("npf/rollbackDisposal")
    Call<RequestBody> rollbackDisposal(@Body Map<String, String> body);

}
