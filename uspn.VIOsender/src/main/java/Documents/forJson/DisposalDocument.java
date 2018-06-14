
package Documents.forJson;

import Documents.forJson.common.Status;
import Documents.forJson.common.Type;
import Documents.forJson.interfaces.IDocuments;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DisposalDocument implements IDocuments {
    @SerializedName("id")
    private long id;

    @SerializedName("number")
    private String number;

    @SerializedName("status")
    private Status status;

    @SerializedName("date")
    private String date;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public String getStatusRUS() {
        return status.getStatus();
    }

    @Override
    public String getStatusENG() {
        return status.getNameUI();
    }

    @Override
    public Type getType() {
        return null;
    }

    public String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return dateFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
