package Services.USPNServices.notificationHandlers.notification;

import Documents.forJson.notification.Notification;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.List;

public abstract class AbstractNotificationService implements INotificationService {
    private Logger logger = LoggerFactory.getLogger(AbstractNotificationService.class);
    private Retrofit retrofit;
    private INotificationService notificationService;

    public AbstractNotificationService(String uri, OkHttpClient client) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.notificationService = retrofit.create(INotificationService.class);
    }

    @Override
    public Call<List<Notification>> getNotification(String createDateStart) {
        Call<List<Notification>> responseBody = notificationService.getNotification(createDateStart);
        logger.info(String.format("GET NOTIFICATIONS with create date: %s", createDateStart));
        logger.trace(String.format("Request to USPN: %s", responseBody.request().toString()));
        return responseBody;
    }
}
