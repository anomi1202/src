import com.google.gson.annotations.SerializedName;

public class ReplyMessage {

    @SerializedName("requestId")
    private String requestId;
    @SerializedName("result")
    private Result result;

    public String getRequestId() {
        return requestId;
    }

    public long getDocumentId() {
        return result.getResponse().getPayload().getContent().getDocumentId();
    }

    class Content {
        @SerializedName("documentId")
        private long documentId;

        public long getDocumentId() {
            return documentId;
        }
    }

    class Payload {
        @SerializedName("content")
        private Content content;

        public Content getContent() {
            return content;
        }
    }

    class Response {
        @SerializedName("payload")
        private Payload payload;

        public Payload getPayload() {
            return payload;
        }
    }

    class Result {
        @SerializedName("response")
        private Response response;

        public Response getResponse() {
            return response;
        }

    }
}