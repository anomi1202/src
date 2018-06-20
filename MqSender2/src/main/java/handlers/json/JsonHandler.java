package handlers.json;

import Enums.DocumentType;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import documents.jsonModel.DocumentDescription;
import documents.jsonModel.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;


public class JsonHandler {
    private Logger logger = LoggerFactory.getLogger(JsonHandler.class);
    private Path PATH_FILE_ARCHIVE_DOC;
    private DocumentType documentType;
    private DocumentDescription document;

    public JsonHandler(DocumentType documentType, Path PATH_FILE_ARCHIVE_DOC){
        this.documentType = documentType;
        this.PATH_FILE_ARCHIVE_DOC = PATH_FILE_ARCHIVE_DOC;

    }

    public JsonHandler jsonGenerate() throws IOException {
        String requestId = UUID.randomUUID().toString().replace("-", "");
        String archDocBase64 = archDocEncodeToBase64(PATH_FILE_ARCHIVE_DOC);
        long INNDocumentSender = Long.valueOf("7726486023");
        String documentAcceptDate = "2018-05-04T10:00:00.000+03:00";
        String documentDate = "2018-05-04T10:00:00.000+03:00";
        String documentNumber = "nagr-20-06-2018";

        Request requestBody = new Request()
                .withContentBody(archDocBase64)
                .withContentDescription(documentType.contentDescription())
                .withDocumentDescription(documentType.documentDescription())
                .withDocumentAcceptDate(documentAcceptDate)
                .withDocumentDate(documentDate)
                .withDocumentNumber(documentNumber)
                .withDocumentSender(INNDocumentSender)
                .withDocumentType(documentType.documentTypeNum())
                .withFileName(PATH_FILE_ARCHIVE_DOC.getFileName().toString());

        document = new DocumentDescription()
                .withRequestId(requestId)
                .withRequestBody(requestBody);

        logger.info(String.format("Generate JSON with:\r\n\t" +
                "requestId: %s\r\n\t" +
                "contentBody: %s",
                requestId, archDocBase64));

        return this;
    }

    public void jsonWriteTo(Path filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)){
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(document));
        }
    }

    private String archDocEncodeToBase64(Path archFilePath) throws IOException {
        byte[] byteFile = Base64.getEncoder().encode(Files.readAllBytes(archFilePath));

        return new String(byteFile);
    }
}
