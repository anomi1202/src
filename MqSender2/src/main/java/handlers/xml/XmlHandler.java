package handlers.xml;

import Enums.DocumentType;
import documents.replyMessageModel.ReplyMessage;
import documents.soapModel.SoapXml;
import handlers.xml.XML_IO_Handler.XmlHandlerReader;
import handlers.xml.interfaces.ISOAPHandler;
import handlers.xml.interfaces.IXMLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlHandler implements ISOAPHandler, IXMLHandler {
    private Logger logger = LoggerFactory.getLogger(XmlHandler.class);
    private SoapXml soap;
    private DocumentType senderDocumentType;
    private ReplyMessage replyJsonMessage;
    private Path xmlFilePath;

    public XmlHandler(DocumentType senderDocumentType, ReplyMessage replyJsonMessage) {
        this.senderDocumentType = senderDocumentType;
        this.replyJsonMessage = replyJsonMessage;
    }

    public XmlHandler(Path xmlFilePath){
        this.xmlFilePath = xmlFilePath;
    }

    @Override
    public XmlHandler soapGenerate() {
        long documentRefId = replyJsonMessage.getDocumentId();
        String requestID = replyJsonMessage.getRequestId();
        String messageID = senderDocumentType.getMessageID();

        logger.info(String.format("Generate SOAP with:\r\n\t" +
                        "DocumentRef Id: %d\r\n\t" +
                        "RequestID: %s\r\n\t" +
                        "MessageID: %s", documentRefId, requestID, messageID));

        soap = new SoapXml()
                .withDocumentRefId(documentRefId)
                .withMessageID(messageID)
                .withRequestId(requestID);

        return this;
    }

    @Override
    public void soapWriteTo(Path filePath) throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(SoapXml.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        marshaller.marshal(soap, filePath.toFile());
    }

    @Override
    public String getParam(String tagsPair) throws IOException, XMLStreamException {
        String finderValue = null;
        String[] tagNameParts = tagsPair.split("::");
        String parentTag = tagNameParts[0];
        String childrenTag = tagNameParts[1];
        if (tagNameParts.length > 2){
            throw new IllegalArgumentException("Tags to search more than 2");
        }

        try(XmlHandlerReader xmlHandlerReader = new XmlHandlerReader(Files.newInputStream(xmlFilePath))) {
            XMLEventReader xmlEventReader = xmlHandlerReader.getReader();

            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()
                        && xmlEvent.asStartElement().getName().getLocalPart().equals(parentTag)){
                    Attribute attr = xmlEvent.asStartElement().getAttributeByName(QName.valueOf(childrenTag));
                    finderValue = attr != null ? attr.getValue() : getChildrenTagValue(xmlEventReader, childrenTag);
                    break;
                }

                if (xmlEvent.isEndElement()
                        && xmlEvent.asEndElement().getName().getLocalPart().equals(parentTag)){
                    throw new IllegalArgumentException(String.format("Value of tags pair '%s' is not found!", tagsPair));
                }
            }
        }

        return finderValue;
    }

    private String getChildrenTagValue(XMLEventReader xmlEventReader, String childrenTag) throws XMLStreamException {
        String value = null;
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if (xmlEvent.isStartElement() && xmlEvent.asStartElement().getName().getLocalPart().equals(childrenTag)) {
                xmlEvent = xmlEventReader.nextEvent();
                value = xmlEvent.isCharacters() ? xmlEvent.asCharacters().getData() : null;
                break;
            }
        }

        return value;
    }
}
