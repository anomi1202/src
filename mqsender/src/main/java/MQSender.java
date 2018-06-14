import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class MQSender extends MQClient {
    private Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Parameter(names = {"-p", "-path"}, description = "Path to sender file", required = true)
    private Path filePath;

    public static void main(String args[]) {
        MQSender sender = new MQSender();
        JCommander jCommander = new JCommander(sender);
        try{
            jCommander.parse(args);
            sender.newInstance();
            sender.run();
        } catch (ParameterException e){
            jCommander.usage();
        }
    }

    protected void run() {
        String messageContent;
        String replyMessageText = null;
        try {
            messageContent = new String(Files.readAllBytes(filePath), "UTF-8");
            replyMessageText = sendMessage(messageContent);
        } catch (IOException | JMSException e) {
            logger.error("FAILED", e);
            if (e instanceof IOException) {
                throw new IllegalArgumentException("Could not load a message from file '" + filePath.toString() + "'");
            }
        }


        JsonElement jsonElement = new Gson().fromJson(replyMessageText, JsonElement.class);
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        System.out.println("Message has been successfully sent.");
        System.out.println("Reply message:" + json);

    }
}
