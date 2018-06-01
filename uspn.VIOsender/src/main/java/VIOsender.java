import Enums.DocumentType;
import Service.SendToUspn.SendService;
import Service.SenderServiceBuilder;
import Service.UploadToVio.UploadService;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class VIOsender {
    private final Logger logger = LoggerFactory.getLogger(VIOsender.class);

    @Parameter(names = {"-doc", "-d"}, description = "Path to document to send!", required = true)
    private static Path docToSend;

    @Parameter(names = {"-type", "-t"}, description = "Number if type input documents", required = true)
    private static int docTypeNum;

    public static void main(String[] args) {
        args = new String[]{"-doc", "src/main/resources/04-RNPF-S.zip", "-type", "4"};
        VIOsender sender = new VIOsender();
        JCommander jCommander = new JCommander(sender);
        try {
            jCommander.parse(args);

            String request = sender.send(docToSend);
            System.out.println(request);
        } catch (ParameterException e){
            jCommander.usage();
        }
    }

    private String send(Path docToSend){
        String request = null;
        try {
            SendService vioService = SenderServiceBuilder.create();
            request = vioService.sendToUspn(docToSend.toFile(), DocumentType.getName(docTypeNum));
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return request;
    }
}
