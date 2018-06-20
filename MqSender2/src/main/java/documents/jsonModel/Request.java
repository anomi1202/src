package documents.jsonModel;

import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("contentType")
    private String contentType = "application/gzip";
    @SerializedName("documentFrom")
    private String documentFrom = "urn:region:777000:UVKiP";
    @SerializedName("documentTo")
    private String documentTo = "urn:region:777000:USPN";
    @SerializedName("documentReceiver")
    private String documentReceiver = "101-000";
    @SerializedName("mimeType")
    private String mimeType = "application/gzip";
    @SerializedName("documentKind")
    private int documentKind = 1;
    @SerializedName("documentReceiverType")
    private int documentReceiverType = 4;
    @SerializedName("documentSenderType")
    private int documentSenderType = 3;
    @SerializedName("documentStatus")
    private int documentStatus = 1;
    @SerializedName("documentChannelType")
    private int documentChannelType = 2;

    //Параметры зависящие от содержания документа
    @SerializedName("contentBody")
    private String contentBody;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("documentDate")
    private String documentDate;
    @SerializedName("documentAcceptDate")
    private String documentAcceptDate;
    @SerializedName("documentNumber")
    private String documentNumber;
    @SerializedName("documentSender")
    private long documentSender;

    //Справочные параметры
    @SerializedName("contentDescription")
    private String contentDescription;
    @SerializedName("documentDescription")
    private String documentDescription;
    @SerializedName("documentType")
    private int documentType;

    public Request withContentBody(String contentBody) {
        this.contentBody = contentBody;
        return this;
    }

    public Request withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Request withDocumentDate(String documentDate) {
        this.documentDate = documentDate;
        return this;
    }

    public Request withDocumentAcceptDate(String documentAcceptDate) {
        this.documentAcceptDate = documentAcceptDate;
        return this;
    }

    public Request withDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public Request withDocumentSender(long documentSender) {
        this.documentSender = documentSender;
        return this;
    }

    public Request withContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
        return this;
    }

    public Request withDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
        return this;
    }

    public Request withDocumentType(int documentType) {
        this.documentType = documentType;
        return this;
    }
}