import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import javax.jms.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class MQSender extends MQClient {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() {
        String messageContent;
        String replyMessageText = null;
        try {
            messageContent = new String(Files.readAllBytes(filePath), "UTF-8");
            TextMessage replyMessage = sendMessage(messageContent);
            replyMessageText = replyMessage != null ? replyMessage.getText() : "No body message!";
        } catch (IOException | JMSException e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                throw new IllegalArgumentException("Could not load a message from file '" + filePath.toString() + "'");
            }
        }


        JsonElement jsonElement = new Gson().fromJson(replyMessageText, JsonElement.class);
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        System.out.println("Message has been successfully sent.");
        System.out.println("Reply message:\r\n" + json);

    }

    public TextMessage sendMessage(String messageContent) throws JMSException {
        MessageProducer producer = null;
        MessageConsumer messageReader = null;
        Message senderMessage = null;
        TextMessage replyMessage = null;

        try {
            producer = session.createProducer(destinationQueue);
            senderMessage = session.createTextMessage();
            messageReader = session.createConsumer(replyQueue);

            // Start the connection and send
            connection.start();
            ((TextMessage) senderMessage).setText(messageContent);
            senderMessage.setJMSReplyTo(replyQueue);
            producer.send(senderMessage);

            //Get reply message with default timeout 15 sec
            replyMessage = (TextMessage) messageReader.receive(waitReply);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (producer != null) {
                try {
                    producer.close();
                } catch (JMSException jmsex) {
                    System.out.println("FAILED! Producer could not be closed.");
                }
            }

            if (messageReader != null) {
                try {
                    messageReader.close();
                } catch (JMSException jmsex) {
                    System.out.println("FAILED! Producer could not be closed.");
                }
            }
        }

        return replyMessage;
    }
}
