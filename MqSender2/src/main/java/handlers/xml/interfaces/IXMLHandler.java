package handlers.xml.interfaces;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;

public interface IXMLHandler {
    /**
     * Генерация XML-документа на основе шаблона
     * */
    Path xmlGenerate() throws IOException, XMLStreamException;


    /**
     * Генерация Gzip-архива
     * */
    Path compressToGzip(Path filePath) throws IOException;
}
