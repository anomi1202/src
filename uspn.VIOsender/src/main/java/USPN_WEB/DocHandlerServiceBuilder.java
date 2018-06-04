package USPN_WEB;

import USPN_WEB.documentHandlers.DisposalHandlerServiceImpl;
import USPN_WEB.documentHandlers.IncomingDocHandlerServiceImpl;
import USPN_WEB.documentHandlers.PaymentHandlerServiceImpl;
import USPN_WEB.documentHandlers.interfaces.DisposalHandlerService;
import USPN_WEB.documentHandlers.interfaces.IncomingDocHandlerService;
import USPN_WEB.documentHandlers.interfaces.PaymentHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DocHandlerServiceBuilder {
    private final Logger logger = LoggerFactory.getLogger(DocHandlerServiceBuilder.class);
    private final String USPN_PROPERTIES = "USPN.properties";
    private final String DEFAULT_CONTEXT = "uspn/forms";
    private String host;
    private int port;
    private String context;
    private IncomingDocHandlerService inDocService;
    private DisposalHandlerService disposalService;
    private PaymentHandlerService paymentService;

    private DocHandlerServiceBuilder() {
        this.context = DEFAULT_CONTEXT;
        initProp();
    }

    public static DocHandlerServiceBuilder create() {
        return new DocHandlerServiceBuilder().build();
    }

    private DocHandlerServiceBuilder build() {
        if (host != null
                && port > 0 && port < 65536){
            String uri = "http://" + host + ":" + port + "/" + context + "/";
            logger.info(String.format("Generate URL link to webApp: %s", uri));

            inDocService = new IncomingDocHandlerServiceImpl(uri);
            disposalService = new DisposalHandlerServiceImpl(uri);
            paymentService = new PaymentHandlerServiceImpl(uri);

            return this;
        } else {
            return null;
        }
    }

    public IncomingDocHandlerService inDocHandlerExecute(){
        return inDocService;
    }

    public DisposalHandlerService disposalHandlerExecute(){
        return disposalService;
    }

    public PaymentHandlerService paymentHandlerExecute(){
        return paymentService;
    }

    private void initProp() {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(USPN_PROPERTIES))) {
            prop.load(is);

            host = prop.getProperty("uspn.host");
            port = Integer.parseInt(prop.getProperty("uspn.port"));
            context = prop.getProperty("uspn.context");

            logger.info(String.format("Read properties file: %s" +
                            "\r\n\thost: %s" +
                            "\r\n\tport: %s" +
                            "\r\n\tcontext: %s"
                    , USPN_PROPERTIES, host, port, context));
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
        }
    }
}
