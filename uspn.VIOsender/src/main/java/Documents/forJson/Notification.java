package Documents.forJson;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("notificationMessageType")
    private String notificationMessageType;

    @SerializedName("startDate")
    private String startDate;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotificationMessageType() {
        return notificationMessageType;
    }

    public String getStartDate() {
        return startDate;
    }
}
