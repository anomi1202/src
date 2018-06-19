@XmlSchema(
        namespace = "http://vio.pfr.ru/document/up/1.0",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix="n1", namespaceURI="http://vio.pfr.ru/document/up/1.0")
        }
)

package documents.soapModel.n1;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;