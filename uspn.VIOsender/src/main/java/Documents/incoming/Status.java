
package Documents.incoming;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("clsItem")
    private ClsItem clsItem;

    @SerializedName("nameUI")
    private String nameUI;

    public String getStatus() {
        return clsItem.getLabel();
    }

    public String getNameUI() {
        return nameUI;
    }
}
