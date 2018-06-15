package XmlHandler;

import documents.ReplyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class XmlHandler {
    private Logger logger = LoggerFactory.getLogger(XmlHandler.class);
    private Path soapFilePath;
    private ReplyMessage replyJsonMessage;

    public XmlHandler(Path soapFilePath, ReplyMessage replyJsonMessage) {
        this.soapFilePath = soapFilePath;
        this.replyJsonMessage = replyJsonMessage;
    }

    public void xmlGenerate(){
        long documentRef = replyJsonMessage.getDocumentId();
        logger.info(String.format("EHD ID of document: %d", documentRef));

        String requestID = replyJsonMessage.getRequestId();
        logger.info(String.format("RequestID of document: %s", requestID));

        setParamToSOAP("DocumentRef::id", String.valueOf(documentRef));
        setParamToSOAP("RequestId", requestID);
    }

    private XMLEvent setValueByAttrSoap(String attrName, String newValue, StartElement startElement) {
        Iterator iterator = startElement.getAttributes();
        Set<Attribute> attrSet = new HashSet<>();
        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.next();
            QName qName = attribute.getName();
            if (attribute.getName().getLocalPart().equals(attrName)) {
                attribute = XMLEventFactory.newInstance().createAttribute(qName, newValue);
            }

            attrSet.add(attribute);
        }
        return XMLEventFactory.newInstance().createStartElement(startElement.getName(), attrSet.iterator(), null);
    }

    private void setParamToSOAP(String replaceTagName, String replaceTagValue) {
        Path tempScrFilePath = null;
        try {
            tempScrFilePath = File.createTempFile(soapFilePath.getFileName().toString(), null, soapFilePath.getParent().toFile()).toPath();
            setParamToSOAP(replaceTagName, replaceTagValue, tempScrFilePath);
        }
        catch (IOException | XMLStreamException e) {
            logger.error("FAILED", e);
        } finally {
            if (tempScrFilePath != null) {
                try {
                    Files.copy(tempScrFilePath, soapFilePath, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(tempScrFilePath);
                } catch(IOException e){
                    logger.error("FAILED", e);
                }
            }
        }
    }

    private void setParamToSOAP(String replaceTagName, String replaceTagValue, Path tempScrFilePath) throws XMLStreamException, IOException {
        String[] replaceTagNameParts = replaceTagName.contains("::") ? replaceTagName.split("::") : new String[]{replaceTagName};

        try (XmlHandlerReader xmlHandlerReader = new XmlHandlerReader(Files.newInputStream(soapFilePath));
             XmlHandlerWriter xmlHandlerWriter = new XmlHandlerWriter(Files.newOutputStream(tempScrFilePath))
        ) {
            XMLEventReader xmlEventReader = xmlHandlerReader.getReader();
            XMLEventWriter xmlEventWriter = xmlHandlerWriter.getReader();

            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String tagName = startElement.getName().getLocalPart();

                    if (replaceTagNameParts.length == 2 && tagName.contains(replaceTagNameParts[0])) {
                        xmlEvent = setValueByAttrSoap(replaceTagNameParts[1], replaceTagValue, startElement);
                    } else if (tagName.contains(replaceTagNameParts[0])) {
                        xmlEventWriter.add(xmlEvent);
                        xmlEvent = xmlEventReader.nextEvent();
                        if (xmlEvent.isCharacters()) {
                            xmlEvent = XMLEventFactory.newInstance().createCharacters(replaceTagValue);
                        }
                    }
                }

                xmlEventWriter.add(xmlEvent);
            }
            xmlEventWriter.flush();
        }
    }
}
