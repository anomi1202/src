@XmlSchema(
        namespace = "http://www.w3.org/2005/08/addressing",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix="wsa", namespaceURI="http://www.w3.org/2005/08/addressing"),
        }
)

package documents.soapModel.wsa;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;