import Enums.ESenderDocType;
import XmlHandler.XmlHandler;
import XmlHandler.interfaces.ISOAPHandler;
import XmlHandler.interfaces.IXMLHandler;
import documents.ReplyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class AbstractMqSender2 {
    private static Logger logger = LoggerFactory.getLogger(AbstractMqSender2.class);
    protected final String MQSENDER_PROPERTIES = "MQSender.properties";
    protected Path PATH_FILE_JSON;
    protected Path PATH_FILE_SOAP;
    protected Path PATH_FILE_DOCUMENT;
    protected Properties propFile = new Properties();

    protected void preparingDocument(ESenderDocType type, ReplyMessage replyMessage) throws IOException, XMLStreamException {
        switch (type) {
            case JSON:
                IXMLHandler xmlHandler = new XmlHandler(PATH_FILE_DOCUMENT, null);
                Path gzipDocumentPath = xmlHandler.generateGzip();
//                JsonHandler jsonHandler = new JsonHandler(PATH_FILE_JSON, PATH_FILE_DOCUMENT);
//                jsonHandler.jsonGenerate();
//                initPropForMQSender(ESenderDocType.JSON);
                break;
            case SOAP:
                ISOAPHandler soapHandler = new XmlHandler(PATH_FILE_SOAP, replyMessage);
                soapHandler.soapGenerate();
                initPropForMQSender(ESenderDocType.SOAP);
                break;
            default:
                break;
        }
    }

    private void initPropForMQSender(ESenderDocType type) throws IOException, NullPointerException {
        try (OutputStream os = Files.newOutputStream(Paths.get(MQSENDER_PROPERTIES))) {
            String propDestination = "";
            switch (type) {
                case JSON:
                    propDestination = propFile.getProperty("mq.destinationJSON");
                    break;
                case SOAP:
                    propDestination = propFile.getProperty("mq.destinationSOAP");
                     break;
                default:
                    break;
            }

            propFile.setProperty("mq.destination", propDestination);
            propFile.remove("mq.destinationSOAP");
            propFile.remove("mq.destinationJSON");
            propFile.remove("doc.json");
            propFile.remove("doc.soap");
            propFile.remove("doc.arch");
            propFile.store(os, "Set properties 'mq.destination': " + propDestination);
        } catch (IOException | NullPointerException ex) {
            logger.error("FAILED", ex);
            throw ex;
        }
    }
}
