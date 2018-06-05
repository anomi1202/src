package USPN_WEB.documentHandlers.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthorizationService {

    @FormUrlEncoded
    @POST("j_spring_security_check")
    Call<ResponseBody> getAuthCookie(
            @Field("username") String user,
            @Field("password") String pass,
            @Field("submit") String submit
    ) throws Exception;
}
