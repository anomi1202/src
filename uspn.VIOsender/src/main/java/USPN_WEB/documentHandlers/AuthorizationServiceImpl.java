package USPN_WEB.documentHandlers;

import USPN_WEB.documentHandlers.interfaces.AuthorizationService;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class AuthorizationServiceImpl implements AuthorizationService {
    private Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private Retrofit retrofit;
    private AuthorizationService authService;
    private static final String USER_NAME = "admin";
    private static final String USER_PASS = "admin";
    private static final String USER_SUBMIT = "Войти";

    public AuthorizationServiceImpl(String uri, OkHttpClient client) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.authService = retrofit.create(AuthorizationService.class);
    }

    public void getAuthorizationCookies() throws Exception {
        try {
            getAuthCookie(USER_NAME, USER_PASS, USER_SUBMIT).execute();
        } catch (Exception e) {
            logger.error("FAILED", e);
            throw e;
        }
    }

    @Override
    public Call<ResponseBody> getAuthCookie(String user, String pass, String submit) throws Exception {
        Call<ResponseBody> authCookie = authService.getAuthCookie(user, pass, submit);
        logger.info(String.format("Request to USPN: %s", authCookie.request().toString()));
        return authCookie;
    }
}
