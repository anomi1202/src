package Services.USPNServices.documentHandlers.authorization;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAuthorizationService {

    @FormUrlEncoded
    @POST("j_spring_security_check")
    Call<ResponseBody> getAuthCookie(
            @Field("username") String user,
            @Field("password") String pass,
            @Field("submit") String submit
    ) throws Exception;
}
