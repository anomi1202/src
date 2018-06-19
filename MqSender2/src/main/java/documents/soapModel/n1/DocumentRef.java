package documents.soapModel.n1;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Example record
 * <n1:DocumentRef id="1159053" contentNumber="1"/>
 * */
@XmlRootElement (name = "DocumentRef")
@XmlType(propOrder = {"id", "contentNumber"})
public class DocumentRef {
    private long id;
    private String contentNumber = "1";

    public DocumentRef(){}

    public DocumentRef(long id) {
        this.id = id;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    @XmlAttribute
    public String getContentNumber() {
        return contentNumber;
    }

    public void setId(long id) {
        this.id = id;
    }
}
