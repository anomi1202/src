package JsonHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import documents.DocumentDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;


public class JsonHandler {
    private Logger logger = LoggerFactory.getLogger(JsonHandler.class);
    private Path PATH_FILE_JSON;
    private DocumentDescription documentJson;
    private String archDocBase64;
    private String requestId;

    public JsonHandler(Path PATH_FILE_JSON, Path PATH_FILE_ARCHIVE_DOC) throws IOException {
        this.PATH_FILE_JSON = PATH_FILE_JSON;
        this.documentJson = new Gson().fromJson(Files.newBufferedReader(PATH_FILE_JSON), DocumentDescription.class);
        this.requestId = UUID.randomUUID().toString().replace("-", "");

        if (PATH_FILE_ARCHIVE_DOC != null) {
            this.archDocBase64 = archDocEncodeToBase64(PATH_FILE_ARCHIVE_DOC);
        }
    }

    public void jsonGenerate() {
        documentJson.setRequestId(requestId);
        logger.info(String.format("Generate JSON with requestId: %s", requestId));

        if (archDocBase64 != null) {
            documentJson.setContentBody(archDocBase64);
            logger.info(String.format("Generate JSON with contentBody: %s", archDocBase64));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(PATH_FILE_JSON)){
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(documentJson));
        } catch (IOException e) {
            logger.error("FAILED", e);
        }
    }

    private String archDocEncodeToBase64(Path archFilePath) throws IOException {
        try {
            byte[] byteFile = Base64.getEncoder().encode(Files.readAllBytes(archFilePath));
            return new String(byteFile);
        } catch (IOException e) {
            logger.error("FAILED", e);
            throw e;
        }
    }
}
