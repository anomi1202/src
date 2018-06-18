package XmlHandler;

import documents.ReplyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class XmlHandler extends AbstractXmlHandler {
    private Logger logger = LoggerFactory.getLogger(XmlHandler.class);

    public XmlHandler(Path xmlFilePath, ReplyMessage replyJsonMessage) {
        super(xmlFilePath, replyJsonMessage);
    }

    @Override
    public Path soapGenerate() throws IOException, XMLStreamException {
        long documentRef = replyJsonMessage.getDocumentId();
        logger.info(String.format("EHD ID of document: %d", documentRef));

        String requestID = replyJsonMessage.getRequestId();
        logger.info(String.format("RequestID of document: %s", requestID));

        setParam("DocumentRef::id", String.valueOf(documentRef));
        setParam("RequestId", requestID);

        return xmlFilePath;
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
