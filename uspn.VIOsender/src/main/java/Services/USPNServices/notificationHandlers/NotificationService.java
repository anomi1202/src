package Services.USPNServices.notificationHandlers;

import Documents.forJson.notification.Notification;
import Services.USPNServices.notificationHandlers.notification.AbstractNotificationService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.util.List;

public class NotificationService extends AbstractNotificationService {
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(String uri, OkHttpClient client) {
        super(uri, client);
    }

    /**
     * Метод для работы с уведомлениями
     * Получение списка документов с формы входящих документов
     * @param createDateStart - Дата создания уведомления
     *                             отбираются уведомления дата создания которых >= указанной даты
     * */
    public List<Notification> getNotificationsList(String createDateStart){
        List<Notification> notificationslList = null;
        try {
            Response<List<Notification>> notificationslListResponse = super.getNotification(createDateStart).execute();
            notificationslList = notificationslListResponse.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return notificationslList;
    }
}
