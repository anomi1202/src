import EHDComminucation.EhdPageHandler;
import Enums.ESenderDocType;
import JsonHandler.JsonHandler;
import Utils.JsonExtensionChecks;
import Utils.SoapExtensionChecks;
import XmlHandler.XmlHandler;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class MqSender2 {
    private static Logger logger = LoggerFactory.getLogger(MqSender2.class);
    private static final String MQSENDER_PROPERTIES = "MQSender.properties";
    private Path PATH_FILE_JSON;
    private Path PATH_FILE_SOAP;
    private Path PATH_FILE_ARCHIVE_DOC;

    @Parameter(names = {"prop", "-p"}, description = "Path to MQSender2.properties")
    private static String MQSENDER2_PROPERTIES = "MQSender2.properties";

    @Parameter(names = {"browser", "-br"}, description = "Start MQSender using headless browser")
    private static boolean withBrowser = false;

    public static void main(String[] args) {
        MqSender2 mqSender2 = new MqSender2();
        JCommander jCommander = new JCommander(mqSender2);
        try {
            jCommander.parse(args);
            mqSender2.run();
        } catch (ParameterException e) {
            logger.error(String.format("FAILED: %s", e.getLocalizedMessage()));
            jCommander.usage();
        }
    }

    public void run() {
        initDocumentsPath();
        if (PATH_FILE_JSON == null) {
            logger.info("PATH_FILE_JSON is NULL");
            return;
        }

        try {
            JsonHandler jsonHandler = new JsonHandler(PATH_FILE_JSON, PATH_FILE_ARCHIVE_DOC);
            jsonHandler.jsonGenerate();
            initPropForMQSender(ESenderDocType.JSON);
            send(ESenderDocType.JSON);

            if (PATH_FILE_SOAP != null) {
                initPropForMQSender(ESenderDocType.SOAP);
                initSoap(jsonHandler);
                send(ESenderDocType.SOAP);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                Files.delete(Paths.get(MQSENDER_PROPERTIES));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDocumentsPath() {
        Properties propFile = new Properties();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(MQSENDER2_PROPERTIES))) {
            propFile.load(reader);
            PATH_FILE_JSON = Paths.get(propFile.getProperty("doc.json"));
            PATH_FILE_SOAP = Paths.get(propFile.getProperty("doc.soap"));
            PATH_FILE_ARCHIVE_DOC = Paths.get(propFile.getProperty("doc.arch"));

        } catch (IOException e) {
            logger.error("FAILED", e);
        }
    }

    private void initSoap(JsonHandler jsonHandler) throws IOException {
        String docName = jsonHandler.getValueFromTag("documentNumber");
        long documentRef = withBrowser ? new EhdPageHandler().searchDocument(docName).getDocumentId() : askUserAboutID();
        logger.info(String.format("EHD ID of document: %d", documentRef));

        String requestID = jsonHandler.getValueFromTag("requestId");
        if (requestID == null || requestID.isEmpty()) {
            throw new IllegalArgumentException(String.format("Tag name 'requestID' is't exists in JSON '%s'", PATH_FILE_JSON));
        }
        logger.info(String.format("RequestID of document: %s", requestID));

        XmlHandler xmlHandler = new XmlHandler(PATH_FILE_SOAP);
        xmlHandler.setParamToSOAP("DocumentRef::id", String.valueOf(documentRef));
        xmlHandler.setParamToSOAP("RequestId", requestID);
    }

    private long askUserAboutID() throws IOException, NumberFormatException {
        long docID;
        System.out.print("Enter the ID of document in EHD: ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String id = reader.readLine();
            docID = Integer.parseInt(id);

            if (docID <= 0) {
                logger.error(String.format("Incorrect ID: '%s'", id));
                throw new NumberFormatException();
            }
        } catch (IOException e) {
            throw e;
        }

        return docID;
    }

    private void send(ESenderDocType type) {
        switch (type) {
            case JSON:
                logger.info(String.format("Send JSON file: %s", PATH_FILE_JSON));
                new MQSender().run(new String[]{PATH_FILE_JSON.toString()});
                break;
            case SOAP:
                logger.info(String.format("Send SOAP file: %s.", PATH_FILE_SOAP));
                new MQSender().run(new String[]{PATH_FILE_SOAP.toString()});
                break;
            default:
                break;
        }
    }

    private void initPropForMQSender(ESenderDocType type) throws IOException, NullPointerException {
        Properties propFile = new Properties();

        try (InputStream is = Files.newInputStream(Paths.get(MQSENDER2_PROPERTIES))) {
            propFile.load(is);
        } catch (IOException e) {
            logger.error("FAILED", e);
            throw e;
        }

        try (OutputStream os = Files.newOutputStream(Paths.get(MQSENDER_PROPERTIES))) {
            String propDestination = "";
            switch (type) {
                case JSON:
                    propDestination = propFile.getProperty("mq.destinationJSON");
                    break;
                case SOAP:
                    propDestination = propFile.getProperty("mq.destinationSOAP");
                     break;
                default:
                    break;
            }

            propFile.setProperty("mq.destination", propDestination);
            propFile.remove("mq.destinationSOAP");
            propFile.remove("mq.destinationJSON");
            propFile.remove("doc.json");
            propFile.remove("doc.soap");
            propFile.remove("doc.arch");
            propFile.store(os, "Set properties 'mq.destination': " + propDestination);
        } catch (IOException | NullPointerException ex) {
            logger.error("FAILED", ex);
            throw ex;
        }
    }
}
