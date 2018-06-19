package documents.soapModel;

import documents.soapModel.n1.Body;
import documents.soapModel.n1.DocumentRef;
import documents.soapModel.wsa.Header;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Example record
     <soap:SoapXml
        xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
         <soap:Header>
             <wsa:MessageID>6661f6e66dd666e6b000000000ad6661</wsa:MessageID>
             <wsa:From>
                 <wsa:Address>urn:region:777000:UVKiP</wsa:Address>
             </wsa:From>
             <wsa:To>urn:region:777000:UVKiP</wsa:To>
             <wsa:Action>urn:process:5:1.0</wsa:Action>
             <ns1:RequestId>2f3897bbd88b4624917568f74e3b738b</ns1:RequestId>
         </soap:Header>
         <soap:Body>
             <n1:DocumentRef id="1159053" contentNumber="1"/>
         </soap:Body>
     </soap:SoapXml>

 * NameSpaces
 * xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
 * xmlns:wsa="http://www.w3.org/2005/08/addressing"
 * xmlns:ns1="http://r-style.com/2014/routing"
 * xmlns:n1="http://vio.pfr.ru/document/up/1.0"
 * */

@XmlRootElement(name = "Envelope")
@XmlType(propOrder = {"header", "body"})
public class SoapXml {
    private Header header = new Header();
    private Body body = new Body();

    public SoapXml() {
    }

    @XmlElement(name = "Body")
    public Body getBody() {
        return body;
    }

    @XmlElement(name = "Header")
    public Header getHeader() {
        return header;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public SoapXml withMessageID(String messageID){
        header.setMessageID(messageID);

        return this;
    }

    public SoapXml withRequestId(String requestIdID){
        header.setRequestId(requestIdID);

        return this;
    }

    public SoapXml withDocumentRefId(long id){
        body.setDocumentRef(new DocumentRef(id));

        return this;
    }
}
