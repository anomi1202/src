package Services.SenderService.SendToUspn.interfaces;

import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendServiceRest {
    /**
     * Отправка файла из ВИО-эмулятора в приложение УСПН
     *
     * @param json JSON с ID отправляемого УПП, документа и типа отправляемого документа
     *             {"uppFileId":123, "documentFileId":1234, documentType": TYPE_DOC}
     * @return объект Call<T>
     */
    @POST("backend/process/sendNpfDocument")
    Call<ResponseBody> send(@Body JsonObject json) throws Exception;
}
