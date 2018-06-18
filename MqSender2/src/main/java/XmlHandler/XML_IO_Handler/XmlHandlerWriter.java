package XmlHandler.XML_IO_Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;

public class XmlHandlerWriter implements AutoCloseable{
    private final Logger logger = LoggerFactory.getLogger(XmlHandlerWriter.class);
    private final XMLOutputFactory FACTORY = XMLOutputFactory.newInstance();
    private final XMLEventWriter writer;
    private final OutputStream os;

    public XmlHandlerWriter(OutputStream os) throws XMLStreamException {
        this.os = os;
        writer = FACTORY.createXMLEventWriter(os);
    }

    public XMLEventWriter getWriter() {
        return writer;
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                os.close();
                writer.close();
            } catch (XMLStreamException | IOException e){
                logger.error("FAILED", e);
            }
        }
    }
}
