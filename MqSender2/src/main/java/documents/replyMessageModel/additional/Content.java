package documents.replyMessageModel.additional;

import com.google.gson.annotations.SerializedName;

public class Content {
    @SerializedName("documentId")
    private long documentId;

    public Content(long documentId) {
        this.documentId = documentId;
    }

    public long getDocumentId() {
        return documentId;
    }
}
