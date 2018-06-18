package XmlHandler;

import XmlHandler.XML_IO_Handler.XmlHandlerReader;
import XmlHandler.XML_IO_Handler.XmlHandlerWriter;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractXmlHandler implements ISOAPHandler, IXMLHandler {
    private Logger logger = LoggerFactory.getLogger(AbstractXmlHandler.class);
    protected Path xmlFilePath;
    protected ReplyMessage replyJsonMessage;

    public AbstractXmlHandler(Path xmlFilePath, ReplyMessage replyJsonMessage) {
        this.xmlFilePath = xmlFilePath;
        this.replyJsonMessage = replyJsonMessage;
    }

    protected void setParam(String tagName, String tagValue) throws IOException, XMLStreamException {
        Path tempScrFilePath = null;
        try {
            tempScrFilePath = File.createTempFile(xmlFilePath.getFileName().toString(), null, xmlFilePath.getParent().toFile()).toPath();
            setParam(tagName, tagValue, tempScrFilePath);
        } finally {
            if (tempScrFilePath != null) {
                try {
                    Files.copy(tempScrFilePath, xmlFilePath, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(tempScrFilePath);
                } catch(IOException e){
                    logger.error("FAILED", e);
                }
            }
        }
    }

    private void setParam(String tagName, String replaceTagValue, Path tempScrFilePath) throws XMLStreamException, IOException {
        boolean isAttribute = tagName.contains("::");
        boolean isInternalTag = tagName.contains("-");

        String[] tagNameParts = isAttribute ? tagName.split("::") : new String[]{tagName};
        tagNameParts = isInternalTag ? tagName.split("-") : tagNameParts;

        try (XmlHandlerReader xmlHandlerReader = new XmlHandlerReader(Files.newInputStream(xmlFilePath));
             XmlHandlerWriter xmlHandlerWriter = new XmlHandlerWriter(Files.newOutputStream(tempScrFilePath))
        ) {
            XMLEventReader xmlEventReader = xmlHandlerReader.getReader();
            XMLEventWriter xmlEventWriter = xmlHandlerWriter.getWriter();

            boolean isInternalTagFinded = false;
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String l_tagName = startElement.getName().getLocalPart();
                    boolean isTagContainsTagName = l_tagName.contains(tagNameParts[0]);

                    xmlEvent = isAttribute && isTagContainsTagName ? setValueByAttr(tagNameParts[1], replaceTagValue, startElement) : xmlEvent;

                    isInternalTagFinded = isTagContainsTagName || isInternalTagFinded;
                    if(isInternalTagFinded) {
                        xmlEventWriter.add(xmlEvent);
                        xmlEvent = xmlEventReader.nextEvent();
                        if (l_tagName.equals(tagNameParts[1]) && xmlEvent.isCharacters()) {
                            isInternalTagFinded = false;
                            xmlEvent = XMLEventFactory.newInstance().createCharacters(replaceTagValue);
                        }
                    }
                }

                xmlEventWriter.add(xmlEvent);
            }
            xmlEventWriter.flush();
        }
    }

    private XMLEvent setValueByAttr(String attrName, String newValue, StartElement startElement) {
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

    protected String getParam(String tagName) throws IOException, XMLStreamException {
        StringBuilder finderValue = new StringBuilder();
        boolean isAttribute = tagName.contains("::");
        boolean isInternalTag = tagName.contains("-");

        String[] tagNameParts = isAttribute ? tagName.split("::") : new String[]{tagName};
        tagNameParts = isInternalTag ? tagName.split("-") : tagNameParts;

        try(XmlHandlerReader xmlHandlerReader = new XmlHandlerReader(Files.newInputStream(xmlFilePath))) {
            XMLEventReader xmlEventReader = xmlHandlerReader.getReader();

            boolean isInternalTagFinder = false;
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String l_tagName = startElement.getName().getLocalPart();

                    String l_attrValue = isAttribute && l_tagName.contains(tagNameParts[0]) ? getValueByAttr(tagNameParts[1], startElement) : "";
                    finderValue.append(l_attrValue);

                    isInternalTagFinder = isInternalTag && l_tagName.contains(tagNameParts[0]) || isInternalTagFinder;
                    if(isInternalTagFinder) {
                        xmlEvent = xmlEventReader.nextEvent();
                        if (l_tagName.equals(tagNameParts[1]) && xmlEvent.isCharacters()) {
                            isInternalTagFinder = false;
                            finderValue.append(xmlEvent.asCharacters().getData());
                        }
                    }
                }
            }
        }

        return finderValue.toString();
    }

    private String getValueByAttr(String attrName, StartElement startElement) {
        StringBuilder finderAttrValue = new StringBuilder();

        Iterator iterator = startElement.getAttributes();
        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.next();
            if (attribute.getName().getLocalPart().equals(attrName)) {
                finderAttrValue.append(attribute.getValue());
            }
        }
        return finderAttrValue.toString();
    }
}
