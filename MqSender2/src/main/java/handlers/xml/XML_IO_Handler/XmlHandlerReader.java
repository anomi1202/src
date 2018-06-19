package handlers.xml.XML_IO_Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

public class XmlHandlerReader implements AutoCloseable{
    private final Logger logger = LoggerFactory.getLogger(XmlHandlerReader.class);
    private final XMLInputFactory FACTORY = XMLInputFactory.newInstance();
    private final XMLEventReader reader;
    private final InputStream is;

    public XmlHandlerReader(InputStream is) throws XMLStreamException {
        this.is = is;
        reader = FACTORY.createXMLEventReader(is);
    }

    public XMLEventReader getReader() {
        return reader;
    }

    @Override
    public void close() {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e){
                logger.error("FAILED", e);
            }
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e){
                logger.error("FAILED", e);
            }
        }
    }
}
