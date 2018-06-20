package handlers.xml.interfaces;

import handlers.xml.XmlHandler;

import javax.xml.bind.JAXBException;
import java.nio.file.Path;

public interface ISOAPHandler {
    /**
     * Геренация SOAP-квитанции
     * */
    ISOAPHandler soapGenerate();

    /**
     * Запись SOAP-квитанции в файл по указанному пути
     */
    void soapWriteTo(Path filePath) throws JAXBException;
}
