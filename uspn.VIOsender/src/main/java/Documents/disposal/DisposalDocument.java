
package Documents.disposal;

import com.google.gson.annotations.SerializedName;

public class DisposalDocument {
    @SerializedName("id")
    private Integer id;

    @SerializedName("number")
    private String number;

    @SerializedName("status")
    private Status status;

    public Integer getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status.getStatus();
    }
}
