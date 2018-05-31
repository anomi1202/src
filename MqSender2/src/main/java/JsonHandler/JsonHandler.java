package JsonHandler;

import Common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;


public class JsonHandler extends AbstractJsonHandler{
    private Logger logger = LoggerFactory.getLogger(JsonHandler.class);
    private Path PATH_FILE_ARCHIVE_DOC;
    private String archDocBase64;
    private String requestId;

    public JsonHandler(Path PATH_FILE_JSON, Path PATH_FILE_ARCHIVE_DOC) throws IOException {
        super(PATH_FILE_JSON);
        this.requestId = UUID.randomUUID().toString().replace("-", "");

        if (PATH_FILE_ARCHIVE_DOC != null) {
            this.PATH_FILE_ARCHIVE_DOC = PATH_FILE_ARCHIVE_DOC;
            archDocEncodeToBase64(PATH_FILE_ARCHIVE_DOC);
        }
    }

    public void jsonGenerate() {
        setValueToTag(PATH_FILE_JSON, "requestId", requestId);
        logger.info(String.format("Generate JSON with requestId: %s", requestId));

        if (archDocBase64 != null) {
            setValueToTag(PATH_FILE_JSON, "contentBody", archDocBase64);
            setValueToTag(PATH_FILE_JSON, "fileName", PATH_FILE_ARCHIVE_DOC.getFileName().toString());
            logger.info(String.format("Generate JSON with contentBody: %s", archDocBase64));
        }
    }

    private void archDocEncodeToBase64(Path archFilePath) throws IOException {
        try {
            byte[] byteFile = Base64.getEncoder().encode(FileUtils.loadFileData(archFilePath.toFile()));
            archDocBase64 = new String(byteFile);

        } catch (IOException e) {
            logger.error("FAILED", e);
            throw e;
        }
    }
}
