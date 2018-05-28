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
    private String archDocBase64;
    private String requestId;

    public JsonHandler(Path jsonFilePath, Path pathFileArchiveDoc) {
        super(jsonFilePath);
        this.requestId = UUID.randomUUID().toString().replace("-", "");

        if (pathFileArchiveDoc != null) {
            archDocEncodeToBase64(pathFileArchiveDoc);
        }
    }

    public void jsonGenerate() {
        setValueToTag(jsonFilePath, "requestId", requestId);
        logger.info(String.format("Generate JSON with requestId: %s", requestId));

        if (archDocBase64 != null) {
            setValueToTag(jsonFilePath, "contentBody", archDocBase64);
            logger.info(String.format("Generate JSON with contentBody: %s", archDocBase64));
        }
    }

    private void archDocEncodeToBase64(Path archFilePath) {
        try {
            byte[] byteFile = Base64.getEncoder().encode(FileUtils.loadFileData(archFilePath.toFile()));
            archDocBase64 = new String(byteFile);

        } catch (IOException e) {
            logger.error("FAILED", e);
        }
    }
}
