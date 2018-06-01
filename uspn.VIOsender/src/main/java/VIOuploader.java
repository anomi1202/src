import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DocumentServiceBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class VIOuploader {
    private final Logger logger = LoggerFactory.getLogger(VIOuploader.class);
    private final String VIOSENDER_PROPERTIES = "VIO.properties";
    private DocumentServiceBuilder vioService;
    private String host;
    private int port;
    private String context;

    @Parameter(names = "-doc", description = "Path to document to send!", required = true)
    private static Path docToSend;

    public static void main(String[] args) {
        args = new String[]{"-doc", "src/main/resources/04-RNPF-S.zip"};
        VIOuploader sender = new VIOuploader();
        JCommander jCommander = new JCommander(sender);
        try {
            jCommander.parse(args);

            String request = sender.send(docToSend);
            System.out.println(request);
        } catch (ParameterException e){
            jCommander.usage();
        }
    }

    public VIOuploader() {
        try {
            initProp();
            this.vioService = DocumentServiceBuilder.create()
                    .host(host)
                    .port(port)
                    .context(context);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String send(Path docToSend){
        String request = null;
        try {
            request = vioService.build().upload(docToSend.toFile());
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return request;
    }

    private void initProp() {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(VIOSENDER_PROPERTIES))) {
            prop.load(is);

            host = prop.getProperty("vio.host");
            port = Integer.parseInt(prop.getProperty("vio.port"));
            context = prop.getProperty("vio.upload.context");
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
        }
    }
}
