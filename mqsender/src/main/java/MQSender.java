import Common.FileUtils;
import Common.MQClient;

import javax.jms.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;


public class MQSender extends MQClient {

    static SimpleDateFormat formatDateTime = new SimpleDateFormat("HH:mm:ss:SSS");

    public MQSender() {
    }

    protected void doRun(String args[]) {
        if (args.length == 0) {
            System.out.println("Usage: " + this.getClass().getSimpleName() + " filename.ext [encoding (for text only)] [count] [delay ms]");
            return;
        }
        final String fileName = args[0];

        String messageContent;
        int count = 1;
        int sleep = 0;

        String encoding = (args.length >= 2) ? args[1].trim() : "UTF-8";

        try {
            count = (args.length >= 3) ? Long.valueOf(args[2].trim()).intValue() : 1;
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            sleep = (args.length >= 4) ? Long.valueOf(args[3].trim()).intValue() : 0;
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            messageContent = FileUtils.loadFile(fileName, encoding);
        } catch (IOException e) {
            System.out.println(e);
            throw new IllegalArgumentException("Could not load a message from file '" + fileName + "'");
        }

        System.out.println("Message count: " + count);
        System.out.println("Delay (ms): " + sleep);

        MessageProducer producer = null;
        Message message = null;

        try {
            producer = session.createProducer(dest);

            message = array ? (Message) session.createBytesMessage() : (Message) session.createTextMessage();

            // Start the connection
            connection.start();

            for (int i = 0; i < count; i++) {
                String text = messageContent;

                if (array)
                    ((BytesMessage) message).writeBytes(text.getBytes(encoding));
                else
                    ((TextMessage) message).setText(text);

                for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
                    String property = (String) e.nextElement();
                    if (!property.startsWith("mq."))
                        message.setStringProperty(property, properties.getProperty(property)
                                .replaceAll("\\$\\{filename\\}", fileName)
                                .replaceAll("\\$\\{encoding\\}", encoding)
                                .replaceAll("\\$\\{iterator\\}", "" + (i + 1)));
                }

                // And, send the message
                producer.send(message);

                Thread.sleep(sleep);

                message.clearProperties();
                message.clearBody();

                if (i % 10 == 0 || i == count) {
                    System.out.println("Iteration: " + i + ", time: " + formatDateTime.format(new Date()));
                }
            }

            System.out.println("Message has been successfully sent.");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (producer != null)
                try {
                    producer.close();
                } catch (JMSException jmsex) {
                    System.out.println("Producer could not be closed.");
                }
        }
    }

    public static void main(String args[]) {
        (new MQSender()).run(args);
    }
}
