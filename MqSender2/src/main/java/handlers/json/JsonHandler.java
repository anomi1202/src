package handlers.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import documents.jsonModel.DocumentDescription;
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
    private Path PATH_FILE_ARCHIVE_DOC;

    public JsonHandler(Path PATH_FILE_JSON, Path PATH_FILE_ARCHIVE_DOC){
        this.PATH_FILE_JSON = PATH_FILE_JSON;
        this.PATH_FILE_ARCHIVE_DOC = PATH_FILE_ARCHIVE_DOC;
    }

    public void jsonGenerate() throws IOException {
        DocumentDescription documentJson = new Gson().fromJson(Files.newBufferedReader(PATH_FILE_JSON), DocumentDescription.class);

        String requestId = UUID.randomUUID().toString().replace("-", "");
        documentJson.setRequestId(requestId);
        logger.info(String.format("Generate JSON with requestId: %s", requestId));

        String archDocBase64 = archDocEncodeToBase64(PATH_FILE_ARCHIVE_DOC);
        documentJson.setContentBody(archDocBase64);
        logger.info(String.format("Generate JSON with contentBody: %s", archDocBase64));

        try (BufferedWriter writer = Files.newBufferedWriter(PATH_FILE_JSON)){
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(documentJson));
        }


    }

    private String archDocEncodeToBase64(Path archFilePath) throws IOException {
        byte[] byteFile = Base64.getEncoder().encode(Files.readAllBytes(archFilePath));

        return new String(byteFile);
    }
}
