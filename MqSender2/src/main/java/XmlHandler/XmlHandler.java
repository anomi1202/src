package XmlHandler;

import XmlHandler.interfaces.ISOAPHandler;
import XmlHandler.interfaces.IXMLHandler;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class XmlHandler implements ISOAPHandler, IXMLHandler {
    private Logger logger = LoggerFactory.getLogger(XmlHandler.class);
    private Path xmlFilePath;
    private ReplyMessage replyJsonMessage;

    public XmlHandler(Path xmlFilePath, ReplyMessage replyJsonMessage) {
        this.xmlFilePath = xmlFilePath;
        this.replyJsonMessage = replyJsonMessage;
    }

    @Override
    public void soapGenerate(){
        long documentRef = replyJsonMessage.getDocumentId();
        logger.info(String.format("EHD ID of document: %d", documentRef));

        String requestID = replyJsonMessage.getRequestId();
        logger.info(String.format("RequestID of document: %s", requestID));

        setParam("DocumentRef::id", String.valueOf(documentRef));
        setParam("RequestId", requestID);
    }

    @Override
    public void xmlGenerate() {
        String docNumber = getParam("Номер");
        logger.info(String.format("Old number of document: %s", docNumber));

        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String newDocNumber = docNumber.replaceAll("nagr-\\d\\d-\\d\\d-\\d\\d\\d\\d", "nagr-" + currentDate);
        logger.info(String.format("New number of document: %s", newDocNumber));
        setParam("Реквизиты-Номер", newDocNumber);
    }

    private void setParam(String replaceTagName, String replaceTagValue) {
        Path tempScrFilePath = null;
        try {
            tempScrFilePath = File.createTempFile(xmlFilePath.getFileName().toString(), null, xmlFilePath.getParent().toFile()).toPath();
            setParam(replaceTagName, replaceTagValue, tempScrFilePath);
        }
        catch (IOException | XMLStreamException e) {
            logger.error("FAILED", e);
        } finally {
            if (tempScrFilePath != null) {
                try {
                    Files.delete(xmlFilePath);
                    Files.move(tempScrFilePath, xmlFilePath);
                } catch(IOException e){
                    logger.error("FAILED", e);
                }
            }
        }
    }

    private void setParam(String replaceTagName, String replaceTagValue, Path tempScrFilePath) throws XMLStreamException, IOException {
        boolean isAttribute = replaceTagName.contains("::");
        String[] replaceTagNameParts = isAttribute ? replaceTagName.split("::") : new String[]{replaceTagName};

        try (XmlHandlerReader xmlHandlerReader = new XmlHandlerReader(Files.newInputStream(xmlFilePath));
             XmlHandlerWriter xmlHandlerWriter = new XmlHandlerWriter(Files.newOutputStream(tempScrFilePath))
        ) {
            XMLEventReader xmlEventReader = xmlHandlerReader.getReader();
            XMLEventWriter xmlEventWriter = xmlHandlerWriter.getWriter();

            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String tagName = startElement.getName().getLocalPart();

                    if (isAttribute && tagName.contains(replaceTagNameParts[0])) {
                        xmlEvent = setValueByAttrSoap(replaceTagNameParts[1], replaceTagValue, startElement);
                    } else if (tagName.equals(replaceTagNameParts[0])) {
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

    private String getParam(String tagName){
        String[] replaceTagNameParts = tagName.contains("::") ? tagName.split("::") : new String[]{tagName};

        try (XmlHandlerReader xmlHandlerReader = new XmlHandlerReader(Files.newInputStream(xmlFilePath))) {
            XMLEventReader xmlEventReader = xmlHandlerReader.getReader();

            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String l_tagName = startElement.getName().getLocalPart();

                    if (l_tagName.contains(replaceTagNameParts[0])) {
                        xmlEvent = xmlEventReader.nextEvent();
                        if (xmlEvent.isCharacters()) {
                            return xmlEvent.asCharacters().getData();
                        }
                    }
                }

            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
