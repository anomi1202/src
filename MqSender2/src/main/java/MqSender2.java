import Enums.ESenderDocType;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import documents.replyMessageModel.ReplyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MqSender2 extends AbstractMqSender2{
    private static Logger logger = LoggerFactory.getLogger(MqSender2.class);

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
        Path PATH_FILE_JSON = null;
        Path PATH_FILE_SOAP = null;

        try {
            PATH_FILE_JSON = preparingSenderFiles(ESenderDocType.JSON, null);
            ReplyMessage replyJsonMessage = send(PATH_FILE_JSON);
            if (replyJsonMessage == null){
                throw new Exception("Reply from JSON message is NULL!");
            }

            PATH_FILE_SOAP = preparingSenderFiles(ESenderDocType.SOAP, replyJsonMessage);
            send(PATH_FILE_SOAP);
        } catch (Exception e) {
            logger.error("FAILED", e);
        } finally {
            clearTemplateFile(PATH_FILE_JSON);
            clearTemplateFile(PATH_FILE_SOAP);
            clearTemplateFile(MQSENDER_PROPERTIES);
        }
    }

    private ReplyMessage send(Path senderFilePath) {
        String replyMessageText = null;
        MQSender sender = new MQSender();

        try {
            logger.info(String.format("Sending file: %s", senderFilePath));
            sender.newInstance();

            if (senderFilePath.getFileName().toString().endsWith(".json")) {
                sender.setWaitReply(15000);
            } else {
                sender.setWaitReply(100);
            }
            TextMessage replyMessage = sender.sendMessage(new String(Files.readAllBytes(senderFilePath), "UTF-8"));
            replyMessageText = replyMessage != null ? replyMessage.getText() : null;
            logger.info("Send successful!");
        } catch (IOException | JMSException e) {
            logger.error("FAILED", e);
        }

        return new Gson().fromJson(replyMessageText, ReplyMessage.class);
    }

    private void clearTemplateFile(Path filePath){
        try {
            if (filePath != null){
                Files.deleteIfExists(filePath);
            } else {
                throw new Exception("PATH_FILE_SOAP is NULL!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
