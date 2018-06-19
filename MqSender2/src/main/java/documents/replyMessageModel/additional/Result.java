package documents.replyMessageModel.additional;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("response")
    private Response response;

    public Result(long documentId) {
        this.response = new Response(documentId);
    }

    public Response getResponse() {
        return response;
    }
}
