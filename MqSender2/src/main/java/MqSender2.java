import Enums.ESenderDocType;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import documents.ReplyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MqSender2 extends AbstractMqSender2{
    private static Logger logger = LoggerFactory.getLogger(MqSender2.class);

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
            preparingDocument(ESenderDocType.JSON, null);
//            ReplyMessage replyJsonMessage = send(ESenderDocType.JSON);
//            if (replyJsonMessage == null){
//                throw new Exception("Reply from JSON message is NULL!");
//            }
//
//            if (PATH_FILE_SOAP != null) {
//                preparingDocument(ESenderDocType.SOAP, replyJsonMessage);
//                send(ESenderDocType.SOAP);
//            }
        } catch (Exception e) {
            logger.error("FAILED", e);
        } finally {
            try{
                Files.deleteIfExists(Paths.get(MQSENDER_PROPERTIES));
            } catch (IOException e) {
                logger.error("FAILED", e);
            }
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
                    sender.newInstance();
                    replyMessage = sender.sendMessage(new String(Files.readAllBytes(PATH_FILE_JSON), "UTF-8"));
                    replyMessageText = replyMessage != null ? replyMessage.getText() : null;
                    break;
                case SOAP:
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

    private void initDocumentsPath() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(MQSENDER2_PROPERTIES))) {
            propFile.load(reader);
            PATH_FILE_JSON = Paths.get(propFile.getProperty("doc.json"));

            String soapProperties = propFile.getProperty("doc.soap");
            PATH_FILE_SOAP = soapProperties != null ? Paths.get(soapProperties) : null;

            String archProperties = propFile.getProperty("doc.arch");
            PATH_FILE_DOCUMENT = archProperties != null ? Paths.get(archProperties) : null;
        } catch (IOException e) {
            logger.error("FAILED", e);
        }
    }
}
