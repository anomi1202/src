package Service;

import Service.SendToUspn.SendService;
import Service.SendToUspn.SendServiceImpl;
import Service.UploadToVio.UploadService;
import Service.UploadToVio.UploadServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class SenderServiceBuilder {
    private final Logger logger = LoggerFactory.getLogger(SenderServiceBuilder.class);
    private final String VIOSENDER_PROPERTIES = "VIO.properties";
    private static final String DEFAULT_CONTEXT = "vio-emulator";
    private static final int DEFAULT_THREAD_COUNT = 10;

    private String host;
    private int port;
    private String context;
    private int threadCount;

    private SenderServiceBuilder() {
        this.context = DEFAULT_CONTEXT;
        this.threadCount = DEFAULT_THREAD_COUNT;
        initProp();
    }

    public static SendService create() {
        return new SenderServiceBuilder().build();
    }

    private SendService build() {
        if (host != null
                && port > 0 && port < 65536){
            String uri = "http://" + host + ":" + port + "/" + context;
            UploadService uploadService = new UploadServiceImpl(uri, threadCount);
            return new SendServiceImpl(uri, threadCount, uploadService);
        } else {
            return null;
        }
    }

    private void initProp() {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(VIOSENDER_PROPERTIES))) {
            prop.load(is);

            host = prop.getProperty("vio.host");
            port = Integer.parseInt(prop.getProperty("vio.port"));
            context = prop.getProperty("vio.context");
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
        }
    }
}
