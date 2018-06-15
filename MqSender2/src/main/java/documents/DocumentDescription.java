package documents;

import com.google.gson.annotations.SerializedName;

public class DocumentDescription {

    @SerializedName("operation")
    private String operation;

    @SerializedName("request")
    private Request request;

    @SerializedName("requestId")
    private String requestId;

    @SerializedName("systemName")
    private String systemName;

    public String getContentBody() {
        return request.getContentBody();
    }

    public void setContentBody(String contentBody) {
        this.request.setContentBody(contentBody);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}