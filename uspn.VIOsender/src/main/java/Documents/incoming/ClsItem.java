
package Documents.incoming;

import com.google.gson.annotations.SerializedName;

public class ClsItem {
    @SerializedName("label")
    private String label;

    public String getLabel() {
        return label;
    }
}
