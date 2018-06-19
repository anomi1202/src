
package documents.soapModel.n1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * NameSpace
 * xmlns:n1="http://vio.pfr.ru/document/up/1.0">
 *
 * Example record
 <soap:Body>
    <n1:DocumentRef id="1159053" contentNumber="1"/>
 </soap:Body>
*/

@XmlRootElement(name = "Body")
@XmlType(name = "Body", propOrder = "documentRef")
public class Body {
    private DocumentRef documentRef;

    public Body(){}

    @XmlElement(name = "DocumentRef")
    public DocumentRef getDocumentRef() {
        return documentRef;
    }

    public void setDocumentRef(DocumentRef documentRef) {
        this.documentRef = documentRef;
    }
}
