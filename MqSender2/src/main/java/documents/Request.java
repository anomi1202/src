package documents;

import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("contentBody")
    private String contentBody;
    @SerializedName("contentDescription")
    private String contentDescription;
    @SerializedName("contentType")
    private String contentType;
    @SerializedName("documentChannelType")
    private long documentChannelType;
    @SerializedName("documentDate")
    private String documentDate;
    @SerializedName("documentAcceptDate")
    private String documentAcceptDate;
    @SerializedName("documentDescription")
    private String documentDescription;
    @SerializedName("documentFrom")
    private String documentFrom;
    @SerializedName("documentTo")
    private String documentTo;
    @SerializedName("documentKind")
    private long documentKind;
    @SerializedName("documentNumber")
    private String documentNumber;
    @SerializedName("documentReceiver")
    private String documentReceiver;
    @SerializedName("documentReceiverType")
    private long documentReceiverType;
    @SerializedName("documentSender")
    private long documentSender;
    @SerializedName("documentSenderType")
    private long documentSenderType;
    @SerializedName("documentStatus")
    private long documentStatus;
    @SerializedName("documentType")
    private long documentType;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("mimeType")
    private String mimeType;

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }
}