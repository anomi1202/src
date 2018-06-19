@XmlSchema(
        namespace = "http://www.w3.org/2003/05/soap-envelope",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix="soap", namespaceURI="http://www.w3.org/2003/05/soap-envelope"),
                @XmlNs(prefix="ns1", namespaceURI="http://r-style.com/2014/routing"),
        }
)
package documents.soapModel;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;