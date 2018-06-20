import Enums.DocumentType;
import Enums.ESenderDocType;
import com.beust.jcommander.Parameter;
import documents.replyMessageModel.ReplyMessage;
import handlers.json.JsonHandler;
import handlers.xml.XmlHandler;
import handlers.xml.interfaces.ISOAPHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

public abstract class AbstractMqSender2 {
    private static Logger logger = LoggerFactory.getLogger(AbstractMqSender2.class);
    protected final Path MQSENDER2_PROPERTIES = Paths.get("MQSender2.properties");
    protected final Path MQSENDER_PROPERTIES = Paths.get("MQSender.properties");
    private Properties propFile = new Properties();

    @Parameter(names = {"-t", "-type", "documentType"}, description = "Document type", required = true)
    private DocumentType SENDER_DOCUMENT_TYPE;

    @Parameter(names = {"-d", "-doc", "document"}, description = "Path to the document to be sent", required = true)
    private Path PATH_FILE_DOCUMENT;


    protected Path preparingSenderFiles (ESenderDocType type, ReplyMessage replyMessage) throws IOException, JAXBException {
        Path generatedFilePath = Paths.get(PATH_FILE_DOCUMENT.getParent() + "/" + SENDER_DOCUMENT_TYPE.name());
        switch (type) {
            case JSON:
                generatedFilePath = Paths.get(generatedFilePath.toString() + ".json");
                Path gzipDocumentPath = compressToGzip(PATH_FILE_DOCUMENT);

                JsonHandler jsonHandler = new JsonHandler(SENDER_DOCUMENT_TYPE, gzipDocumentPath);
                jsonHandler.jsonGenerate().jsonWriteTo(generatedFilePath);

                initPropForMQSender(ESenderDocType.JSON);
                Files.deleteIfExists(gzipDocumentPath);
                break;
            case SOAP:
                generatedFilePath = Paths.get(generatedFilePath.toString() + ".xml");
                ISOAPHandler soapHandler = new XmlHandler(SENDER_DOCUMENT_TYPE, replyMessage);
                soapHandler.soapGenerate().soapWriteTo(generatedFilePath);

                initPropForMQSender(ESenderDocType.SOAP);
                break;
            default:
                break;
        }

        return generatedFilePath;
    }

    private void initPropForMQSender(ESenderDocType type) throws IOException, NullPointerException {
        try (OutputStream os = Files.newOutputStream(MQSENDER_PROPERTIES);
             BufferedReader reader = Files.newBufferedReader(MQSENDER2_PROPERTIES)
        ) {
            propFile.load(reader);

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
            propFile.remove("doc.documentType");
            propFile.remove("doc.document");
            propFile.store(os, "Set properties 'mq.destination': " + propDestination);
        } catch (IOException ex) {
            logger.error("FAILED", ex);
            throw ex;
        }
    }

    public Path compressToGzip(Path filePath) throws IOException {
        Path gzipFilePath = Paths.get(filePath.getParent() + "/" + filePath.getFileName() + ".gz");

        try (GZIPOutputStream gzipWriter = new GZIPOutputStream(Files.newOutputStream(gzipFilePath))) {
            byte[] bytes = Files.readAllBytes(filePath);
            gzipWriter.write(bytes);
        }

        return gzipFilePath;
    }
}
