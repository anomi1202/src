package Documents.forJson.incoming;

import com.google.gson.annotations.SerializedName;

public class IncomingDocument {

    @SerializedName("id")
    private long id;

    @SerializedName("archiveName")
    private String archiveName;

    @SerializedName("docId")
    private Integer docId;

    @SerializedName("type")
    private Type type;

    @SerializedName("status")
    private Status status;

    @SerializedName("filename")
    private String filename;

    @SerializedName("number")
    private String number;

    @SerializedName("amount")
    private Integer amount;

    @SerializedName("datechange")
    private String datechange;

    @SerializedName("datecreate")
    private String datecreate;

    @SerializedName("linkdocument")
    private String linkdocument;

    public long getId() {
        return id;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public Integer getDocId() {
        return docId;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public String getFilename() {
        return filename;
    }

    public String getNumber() {
        return number;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDatechange() {
        return datechange;
    }

    public String getDatecreate() {
        return datecreate;
    }

    public String getLinkdocument() {
        return linkdocument;
    }
}
