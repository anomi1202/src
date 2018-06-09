package Documents.forJson.payment;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("id")
    private long id;

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

    public Payment withNumber(String number){
        this.number = number;
        return this;
    }

    public Payment withDate(String date){
        this.date = date;
        return this;
    }

    public Payment withAmount(Integer amount){
        this.amount = amount;
        return this;
    }

    public long getId() {
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
