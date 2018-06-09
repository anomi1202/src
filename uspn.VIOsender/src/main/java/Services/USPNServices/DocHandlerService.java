package Services.USPNServices;

import Services.USPNServices.documentHandlers.AuthorizationService;
import Services.USPNServices.documentHandlers.DisposalService;
import Services.USPNServices.documentHandlers.IncomingService;
import Services.USPNServices.documentHandlers.PaymentService;
import Services.USPNServices.notificationHandlers.NotificationService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocHandlerService extends AbstractHandlerService{
    private final Logger logger = LoggerFactory.getLogger(DocHandlerService.class);

    private IncomingService inDocService;
    private DisposalService disposalService;
    private PaymentService paymentService;
    private AuthorizationService authService;
    private NotificationService notificationService;
    private OkHttpClient client;

    public static DocHandlerService create() {
        return new DocHandlerService().build();
    }

    private DocHandlerService build() {
        String uri = getUri();

        try {
            if (uri == null){
                throw new Exception("Uri is NULL!");
            }

            client = new OkHttpClient.Builder().cookieJar(initCookieJar()).build();

            authService = new AuthorizationService(uri, client);
            inDocService = new IncomingService(uri, client);
            disposalService = new DisposalService(uri, client);
            paymentService = new PaymentService(uri, client);
            notificationService = new NotificationService(uri, client);

            return this;
        } catch (Exception e) {
            return null;
        }
    }

    public AuthorizationService authorizationService(){
        return authService;
    }

    public DisposalService disposalService(){
        return disposalService;
    }

    public PaymentService paymentService(){
        return paymentService;
    }

    public IncomingService incomingService(){
        return inDocService;
    }

    public NotificationService notificationService() {
        return notificationService;
    }
}
