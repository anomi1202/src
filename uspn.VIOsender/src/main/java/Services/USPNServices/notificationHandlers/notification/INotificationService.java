package Services.USPNServices.notificationHandlers.notification;

import Documents.forJson.notification.Notification;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface INotificationService {

    @POST("cabinet/notificationsList.json")
    Call<List<Notification>> getNotification(@Query("createDate.start") String createDateStart);
}
