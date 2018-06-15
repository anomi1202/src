import Enums.ESenderDocType;
import JsonHandler.JsonHandler;
import XmlHandler.XmlHandler;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import documents.ReplyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MqSender2 {
    private static Logger logger = LoggerFactory.getLogger(MqSender2.class);
    private static final String MQSENDER_PROPERTIES = "MQSender.properties";
    private Path PATH_FILE_JSON;
    private Path PATH_FILE_SOAP;
    private Path PATH_FILE_ARCHIVE_DOC;

    @Parameter(names = {"prop", "-p"}, description = "Path to MQSender2.properties")
    private static String MQSENDER2_PROPERTIES = "MQSender2.properties";

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
            ReplyMessage replyJsonMessage = send(ESenderDocType.JSON);
            if (replyJsonMessage == null){
                throw new Exception("Reply from JSON message is NULL!");
            }

            if (PATH_FILE_SOAP != null) {
                XmlHandler xmlHandler = new XmlHandler(PATH_FILE_SOAP, replyJsonMessage);
                xmlHandler.xmlGenerate();
                send(ESenderDocType.SOAP);
            }

        } catch (Exception e) {
            logger.error("FAILED", e);
        } finally {
            try{
                Files.delete(Paths.get(MQSENDER_PROPERTIES));
            } catch (IOException e) {
                logger.error("FAILED", e);
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

    private ReplyMessage send(ESenderDocType type) {
        String replyMessageText = null;
        TextMessage replyMessage = null;
        MQSender sender = new MQSender();

        try {
            logger.info(String.format("Sending %s file: %s",type.name(), PATH_FILE_JSON));
            switch (type) {
                case JSON:
                    initPropForMQSender(ESenderDocType.JSON);
                    sender.newInstance();
                    replyMessage = sender.sendMessage(new String(Files.readAllBytes(PATH_FILE_JSON), "UTF-8"));
                    replyMessageText = replyMessage != null ? replyMessage.getText() : null;
                    break;
                case SOAP:
                    initPropForMQSender(ESenderDocType.SOAP);
                    sender.newInstance();
                    sender.setWaitReply(100);
                    replyMessage = sender.sendMessage(new String(Files.readAllBytes(PATH_FILE_SOAP), "UTF-8"));
                    replyMessageText = replyMessage != null ? replyMessage.getText() : null;
                    break;
                default:
                    break;
            }
            logger.info(String.format("%s send successful!", type.name()));
        } catch (IOException | JMSException e) {
            logger.error("FAILED", e);
        }



        return new Gson().fromJson(replyMessageText, ReplyMessage.class);
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
