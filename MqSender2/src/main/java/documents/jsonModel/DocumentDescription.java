package documents.jsonModel;

import com.google.gson.annotations.SerializedName;

public class DocumentDescription {
    @SerializedName("operation")
    private String operation = "saveDocumentAndContent";
    @SerializedName("systemName")
    private String systemName = "NPF";
    @SerializedName("request")
    private Request request;
    @SerializedName("requestId")
    private String requestId;

    public void setContentBody(String contentBody) {
        this.request.withContentBody(contentBody);
    }

    public DocumentDescription withRequestBody(Request request){
        this.request = request;
        return this;
    }

    public DocumentDescription withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

}