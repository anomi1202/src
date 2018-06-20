package Services.SenderService.UploadToVio.interfaces;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadServiceRest {
    /**
     * Post запрос на отправку файла в ВИО-эмулятор
     *
     * @param file загружаемый multipart файл
     * @return объект Call<T>
     */
    @Multipart
    @POST("userFile")
    Call<ResponseBody> postRequest(@Part MultipartBody.Part file) throws Exception;
}
