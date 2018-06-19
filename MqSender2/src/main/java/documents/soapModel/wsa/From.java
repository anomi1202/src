package documents.soapModel.wsa;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Example record
 <wsa:From>
     <wsa:Address>urn:region:777000:UVKiP</wsa:Address>
 </wsa:From>
 * */

@XmlRootElement(name = "From")
@XmlType(propOrder = "address")
public class From {
    private String address = "urn:region:777000:UVKiP";

    public From() {
    }

    @XmlElement(name = "Address")
    public String getAddress() {
        return address;
    }
}
