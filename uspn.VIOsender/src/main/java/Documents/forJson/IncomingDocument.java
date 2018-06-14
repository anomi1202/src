package Documents.forJson;

import Documents.forJson.common.Status;
import Documents.forJson.common.Type;
import Documents.forJson.interfaces.IDocuments;
import com.google.gson.annotations.SerializedName;

public class IncomingDocument implements IDocuments{

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

    @Override
    public long getId() {
        return id;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public Integer getDocId() {
        return docId;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getStatusRUS() {
        return status.getStatus();
    }

    @Override
    public String getStatusENG() {
        return status.getNameUI();
    }

    public String getFilename() {
        return filename;
    }

    @Override
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
