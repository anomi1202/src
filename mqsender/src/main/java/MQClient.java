import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class MQClient {
    private final String MQSENDER_PROPERTIES = "MQSender.properties";

    private String host = null;
    private int port = 1414;
    private String manager = null;
    private String channel = null;
    private String destination = null;
    private String reply = null;

    protected Connection connection = null;
    protected Session session = null;
    protected Destination destinationQueue = null;
    protected Destination replyQueue = null;
    private long waitReply = 15000;

    public void newInstance() {
        initProp();
        try {
            createSession();
        } catch (Exception e) {
            closeSession();
            e.printStackTrace();
        }
    }

    public void setWaitReply(long waitReply){
        this.waitReply = waitReply == 0 ? 10 : waitReply;
    }

    protected void createSession() throws Exception {
        // Create a connection factory
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();

        // Set the properties
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
        cf.setIntProperty(WMQConstants.WMQ_PORT, port);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, manager);

        // Create JMS objects
        connection = cf.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destinationQueue = session.createQueue(destination);
        replyQueue = new MQQueue(reply);
    }

    protected void closeSession() {
        try{
            if (session != null){
                session.close();
            }
        } catch (JMSException jmsex) {
            System.out.println("Session could not be closed.");
        }

        try{
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException jmsex) {
            System.out.println("Session could not be closed.");
        }
    }

    protected void initProp(){
        Properties propFile = new Properties();
        try(InputStream is = Files.newInputStream(Paths.get(MQSENDER_PROPERTIES))){
            propFile.load(is);


            host = propFile.getProperty("mq.host", "localhost");
            port = Integer.parseInt(propFile.getProperty("mq.port", "1414"));

            manager = propFile.getProperty("mq.manager");
            if (manager == null || manager.equals("")){
                throw new IllegalArgumentException("Queue manager name is not specified.");
            }

            channel = propFile.getProperty("mq.channel", "SYSTEM.DEF.SVRCONN");

            destination = propFile.getProperty("mq.destination");
            if (destination == null || destination.equals("")) {
                throw new IllegalArgumentException("Destination name is not specified.");
            }

            reply = propFile.getProperty("mq.reply");
            if (reply == null || reply.equals("")) {
                throw new IllegalArgumentException("Reply name is not specified.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String messageContent) throws JMSException {
        MessageProducer producer = null;
        MessageConsumer messageReader = null;
        Message senderMessage;
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

        return replyMessage.getText();
    }
}
