package handlers.xml.interfaces;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;

public interface ISOAPHandler {
    /**
     * Генерация SOAP-квитанции на основе шаблона
     * */
    Path soapGenerate() throws IOException, XMLStreamException, JAXBException;
}
