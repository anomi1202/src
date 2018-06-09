package Services.USPNServices.documentHandlers.authorization;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class AbstractAuthorizationService implements IAuthorizationService {
    private Logger logger = LoggerFactory.getLogger(AbstractAuthorizationService.class);
    private Retrofit retrofit;
    private IAuthorizationService authService;

    public AbstractAuthorizationService(String uri, OkHttpClient client) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.authService = retrofit.create(IAuthorizationService.class);
    }

    @Override
    public Call<ResponseBody> getAuthCookie(String user, String pass, String submit) throws Exception {
        Call<ResponseBody> responseBody = authService.getAuthCookie(user, pass, submit);
        logger.info(String.format("Authorization! Request to USPN: %s", responseBody.request().toString()));
        return responseBody;
    }
}
