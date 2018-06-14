
package Documents.forJson.common;

import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("shortLabel")
    private String shortLabel;

    @SerializedName("nameUI")
    private String nameUI;

    @SerializedName("form")
    private String form;

    @SerializedName("parentId")
    private String type;



    public String getShortLabel() {
        return shortLabel;
    }

    public String getNameUI() {
        return nameUI;
    }

    public String getForm() {
        return form;
    }

    public String getTypeName() { return type;}
}
