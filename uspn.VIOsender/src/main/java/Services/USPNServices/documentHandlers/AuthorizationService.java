package Services.USPNServices.documentHandlers;

import Services.USPNServices.documentHandlers.authorization.AbstractAuthorizationService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationService extends AbstractAuthorizationService {
    private Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    private static final String USER_NAME = "admin";
    private static final String USER_PASS = "admin";
    private static final String USER_SUBMIT = "Войти";

    public AuthorizationService(String uri, OkHttpClient client) {
        super(uri, client);
    }

    /**
     * Метод для авторизации в УСПН
     * Получение cookie's используемых для последующих POST/GET запросах
     * Cookie's охраняются в словаре родительского абстрактного класса AbstractHandlerService
     * */

    public void doAuthorization() {
        try {
            super.getAuthCookie(USER_NAME, USER_PASS, USER_SUBMIT).execute();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
    }
}
