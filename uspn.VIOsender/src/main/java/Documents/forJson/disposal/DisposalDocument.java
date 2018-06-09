
package Documents.forJson.disposal;

import com.google.gson.annotations.SerializedName;

public class DisposalDocument {
    @SerializedName("id")
    private long id;

    @SerializedName("number")
    private String number;

    @SerializedName("status")
    private Status status;

    @SerializedName("date")
    private String date;

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getStatusRUS() {
        return status.getStatus();
    }

    public String getStatusENG() {
        return status.getNameUI();
    }

    public String getDate(){
        return date;
    }
}
