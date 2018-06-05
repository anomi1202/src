
package Documents.incoming;

import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("shortLabel")
    private String shortLabel;

    @SerializedName("nameUI")
    private String nameUI;

    @SerializedName("form")
    private String form;

    public String getShortLabel() {
        return shortLabel;
    }

    public String getNameUI() {
        return nameUI;
    }

    public String getForm() {
        return form;
    }
}
