package SenderService.SendToUspn.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Map;

public interface SendServiceRest {
    /**
     * Отправка файла из ВИО-эмулятора в приложение УСПН
     *
     * @param body словарь с парами отправляемого JSON файла
     * @return объект Call<T>
     */
    @POST("backend/process/sendNpfDocument")
    Call<ResponseBody> send(@Body Map<String, String> body) throws Exception;
}
