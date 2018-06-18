package XmlHandler.interfaces;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;

public interface IXMLHandler {
    /**
     * Генерация XML-документа на основе шаблона
     * */
    Path generateGzip() throws IOException, XMLStreamException;
}
