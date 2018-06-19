package documents.replyMessageModel.additional;

import com.google.gson.annotations.SerializedName;

public class Payload {
    @SerializedName("content")
    private Content content;

    public Payload(long documentId) {
        this.content = new Content(documentId);
    }

    public Content getContent() {
        return content;
    }
}
