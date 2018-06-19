package handlers.xml;

import Enums.DocumentType;
import documents.replyMessageModel.ReplyMessage;
import documents.soapModel.SoapXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class XmlHandler extends AbstractXmlHandler {
    private Logger logger = LoggerFactory.getLogger(XmlHandler.class);

    public XmlHandler(Path xmlFilePath) {
        super(xmlFilePath);
    }

    public XmlHandler(DocumentType senderDocumentType, ReplyMessage replyJsonMessage) {
        super(senderDocumentType, replyJsonMessage);
    }

    @Override
    public Path soapGenerate() throws JAXBException, IOException {
        long documentRefId = replyJsonMessage.getDocumentId();
        logger.info(String.format("EHD ID of document: %d", documentRefId));

        String requestID = replyJsonMessage.getRequestId();
        logger.info(String.format("RequestID of document: %s", requestID));

        String messageID = senderDocumentType.getMessageID();
        logger.info(String.format("MessageID of SOAP: %s", messageID));

        Marshaller marshaller = JAXBContext.newInstance(SoapXml.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        SoapXml soap = new SoapXml()
                .withDocumentRefId(documentRefId)
                .withMessageID(messageID)
                .withRequestId(requestID);
        Path tempSoapPath = Files.createTempFile(Paths.get("."), "tempSoapFile_", ".xml" );

        marshaller.marshal(soap, tempSoapPath.toFile());

        return tempSoapPath;
    }

    @Override
    public Path xmlGenerate() throws IOException, XMLStreamException {
        String docNumber = getParam("Реквизиты-Номер");
        logger.info(String.format("Old number of document: %s", docNumber));

        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String newDocNumber = docNumber.replaceAll("nagr-\\d\\d-\\d\\d-\\d\\d\\d\\d", "nagr-" + currentDate);
        logger.info(String.format("New number of document: %s", newDocNumber));
        setParam("Реквизиты-Номер", newDocNumber);

        return xmlFilePath;
    }

    @Override
    public Path compressToGzip(Path filePath) throws IOException {
        Path gzipFilePath = Paths.get(xmlFilePath.getParent() + "/" + xmlFilePath.getFileName() + ".gz");

        try (GZIPOutputStream gzipWriter = new GZIPOutputStream(Files.newOutputStream(gzipFilePath))) {
            byte[] bytes = Files.readAllBytes(filePath);
            gzipWriter.write(bytes);
        }

        return gzipFilePath;
    }
}
