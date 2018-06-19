package documents.replyMessageModel.additional;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("payload")
    private Payload payload;

    public Response(long documentId) {
        this.payload = new Payload(documentId);
    }

    public Payload getPayload() {
        return payload;
    }
}
