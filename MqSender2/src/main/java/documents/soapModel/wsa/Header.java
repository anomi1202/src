package documents.soapModel.wsa;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * NameSpace
 * xmlns:wsa="http://www.w3.org/2005/08/addressing"
 * xmlns:ns1="http://r-style.com/2014/routing"
 *
 *
 * Example record
 <soap:Header>
     <wsa:MessageID>6661f6e66dd666e6b000000000ad6661</wsa:MessageID>
     <wsa:From>
         <wsa:Address>urn:region:777000:UVKiP</wsa:Address>
     </wsa:From>
     <wsa:To>urn:region:777000:UVKiP</wsa:To>
     <wsa:Action>urn:process:5:1.0</wsa:Action>
     <ns1:RequestId>2f3897bbd88b4624917568f74e3b738b</ns1:RequestId>
 </soap:Header>
 * */

@XmlRootElement(name = "Header")
@XmlType(propOrder = {"messageID", "from", "to", "action", "requestId"})
public class Header {
    private final String ns1 = "http://r-style.com/2014/routing";

    private String messageID;
    private String requestId;
    private final From from = new From();
    private final String to = "urn:region:777000:UVKiP";
    private final String action = "urn:process:5:1.0";

    public Header() {
    }

    @XmlElement(name = "MessageID")
    public String getMessageID() {
        return messageID;
    }

    @XmlElement(name = "From")
    public From getFrom() {
        return from;
    }

    @XmlElement(name = "To")
    public String getTo() {
        return to;
    }

    @XmlElement(name = "Action")
    public String getAction() {
        return action;
    }

    @XmlElement(name = "RequestId", namespace = ns1)
    public String getRequestId() {
        return requestId;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
