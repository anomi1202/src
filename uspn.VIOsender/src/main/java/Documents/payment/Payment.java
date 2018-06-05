
package Documents.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("id")
    private String id;

    @SerializedName("number")
    private String number;

    @SerializedName("date")
    private String date;

    @SerializedName("amount")
    private Integer amount;

    @SerializedName("description")
    private String description;

    @SerializedName("createDate")
    private String createDate;

    @SerializedName("changeDate")
    private String changeDate;

    public Payment(String number, String date, Integer amount) {
        this.id = "";
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.description = "";
        this.createDate = "";
        this.changeDate = "";
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getChangeDate() {
        return changeDate;
    }
}
