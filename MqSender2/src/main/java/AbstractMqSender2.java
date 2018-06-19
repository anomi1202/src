import Enums.DocumentType;
import Enums.ESenderDocType;
import com.beust.jcommander.Parameter;
import documents.replyMessageModel.ReplyMessage;
import handlers.json.JsonHandler;
import handlers.xml.XmlHandler;
import handlers.xml.interfaces.ISOAPHandler;
import handlers.xml.interfaces.IXMLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class AbstractMqSender2 {
    @Parameter(names = {"prop", "-p"}, description = "Path to MQSender2.properties")
    protected static String MQSENDER2_PROPERTIES = "MQSender2.properties";

    private static Logger logger = LoggerFactory.getLogger(AbstractMqSender2.class);
    protected final String MQSENDER_PROPERTIES = "MQSender.properties";
    protected Path PATH_FILE_JSON;
    protected Path PATH_FILE_SOAP;
    protected DocumentType SENDER_DOCUMENT_TYPE;
    protected Path PATH_FILE_DOCUMENT;
    protected Properties propFile = new Properties();


    protected Path preparingSenderFiles (ESenderDocType type, ReplyMessage replyMessage) throws IOException, XMLStreamException, JAXBException {
        Path generatedFilePath = null;
        switch (type) {
            case JSON:
                IXMLHandler xmlHandler = new XmlHandler(PATH_FILE_DOCUMENT);
                Path gzipDocumentPath = xmlHandler.compressToGzip(xmlHandler.xmlGenerate());

                JsonHandler jsonHandler = new JsonHandler(PATH_FILE_JSON, gzipDocumentPath);
                jsonHandler.jsonGenerate();

                initPropForMQSender(ESenderDocType.JSON);
                Files.deleteIfExists(gzipDocumentPath);
                break;
            case SOAP:
                ISOAPHandler soapHandler = new XmlHandler(SENDER_DOCUMENT_TYPE, replyMessage);
                generatedFilePath = soapHandler.soapGenerate();

                initPropForMQSender(ESenderDocType.SOAP);
                break;
            default:
                break;
        }

        return generatedFilePath;
    }

    private void initPropForMQSender(ESenderDocType type) throws IOException, NullPointerException {
        try (OutputStream os = Files.newOutputStream(Paths.get(MQSENDER_PROPERTIES))) {
            loadPropertiesFrom(Paths.get(MQSENDER2_PROPERTIES));

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
            propFile.remove("doc.soapType");
            propFile.remove("doc.arch");
            propFile.store(os, "Set properties 'mq.destination': " + propDestination);
        } catch (IOException ex) {
            logger.error("FAILED", ex);
            throw ex;
        }
    }

    protected void initDocumentsPath() {
        loadPropertiesFrom(Paths.get(MQSENDER2_PROPERTIES));
        PATH_FILE_JSON = Paths.get(propFile.getProperty("doc.json"));

        String soapTypeProperties = propFile.getProperty("doc.soapType");
        SENDER_DOCUMENT_TYPE = soapTypeProperties != null ? DocumentType.valueOf(soapTypeProperties) : null;

        String archProperties = propFile.getProperty("doc.arch");
        PATH_FILE_DOCUMENT = archProperties != null ? Paths.get(archProperties) : null;
    }

    private void loadPropertiesFrom(Path propFilePath){
        try (BufferedReader reader = Files.newBufferedReader(propFilePath)) {
            propFile.load(reader);
        } catch (IOException e) {
            logger.error("FAILED", e);
        }
    }
}
