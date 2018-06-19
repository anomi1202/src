package documents.replyMessageModel;

import com.google.gson.annotations.SerializedName;
import documents.replyMessageModel.additional.Result;

public class ReplyMessage {
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("result")
    private Result result;

    public ReplyMessage(String requestId, long documentId) {
        this.requestId = requestId;
        this.result = new Result(documentId);
    }

    public String getRequestId() {
        return requestId;
    }

    public long getDocumentId() {
        return result.getResponse().getPayload().getContent().getDocumentId();
    }
}