package uspn.test.appmanager.service;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.rstyle.integration.ehd.EHDClient;
import com.rstyle.integration.ehd.citeria.ContentRequest;
import com.rstyle.integration.ehd.utils.MimeTypes;
import com.rstyle.integration.vio.VIOClient;
import com.rstyle.integration.vio.model.VioProcessType;
import org.apache.commons.io.IOUtils;
import uspn.test.appmanager.data.ServerType;
import uspn.test.appmanager.data.VioDocTypes;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ibm.msg.client.wmq.common.CommonConstants.*;
import static uspn.test.appmanager.data.Constants.*;


/**
 * Singleton for send VIO notification to WMQ
 * Created by Alexander.Khen on 10.01.2017.
 */
public class MqSenderService {
    private static VIOClient vioClient;
    private static EHDClient ehdClient;
    private static volatile MqSenderService INSTANCE;

    private MqSenderService() {

    }

    public static Map<String, Object> connectServiceVio(ServerType serverType) {
        Map<String, Object> param = new HashMap<>();
        switch (serverType) {
            case QA:
                param.put(EHD_HOST, "10.0.12.235");
                param.put(EHD_PORT, "2222");
                param.put(VIO_HOST, "10.0.2.37");
                param.put(VIO_PORT, 1416);
                param.put(VIO_QUEUE_MANAGER, "WMQ.QA.USPN");
                param.put(VIO_QUEUE_CHANNEL, "SYSTEM.ADMIN.SVRCONN");
                param.put(VIO_USER, "mqm");
                param.put(VIO_IN_QUEUE, "WMQ.QA.USPN.IN.QUEUE");
                param.put(VIO_OUT_QUEUE, "WMQ.QA.USPN.OUT.QUEUE");
                break;
            case DEV:
                param.put(EHD_HOST, "10.0.12.235");
                param.put(EHD_PORT, "2222");
                param.put(VIO_HOST, "10.0.2.37");
                param.put(VIO_PORT, 1414);
                param.put(VIO_QUEUE_MANAGER, "WMQ.LOCAL.USPN");
                param.put(VIO_QUEUE_CHANNEL, "SYSTEM.ADMIN.SVRCONN");
                param.put(VIO_USER, "mqm");
                param.put(VIO_IN_QUEUE, "WMQ.LOCAL.USPN.IN.QUEUE");
                param.put(VIO_OUT_QUEUE, "WMQ.LOCAL.USPN.OUT.QUEUE");
                break;
            case USPNDEV:
                param.put(EHD_HOST, "10.0.12.235");
                param.put(EHD_PORT, "2222");
                param.put(VIO_HOST, "10.0.2.37");
                param.put(VIO_PORT, 1415);
                param.put(VIO_QUEUE_MANAGER, "WMQTest");
                param.put(VIO_QUEUE_CHANNEL, "SYSTEM.ADMIN.SVRCONN");
                param.put(VIO_USER, "mqm");
                param.put(VIO_IN_QUEUE, "QUEUE.IN.USPNDEV");
                param.put(VIO_OUT_QUEUE, "QUEUE.OUT.USPNDEV");
                break;
        }
        return param;
    }

    /**
     * init service with param
     *
     * @param params is Map with key from Constants
     * @return instance service
     */
    public static MqSenderService getInstance(Map<String, Object> params) {
        String ehdHost = (String) params.get(EHD_HOST);
        checkParam(ehdHost, "EHD_HOST");
        String ehdPort = (String) params.get(EHD_PORT);
        String vioHost = (String) params.get(VIO_HOST);
        checkParam(vioHost, "VIO_HOST");
        Integer vioPort = (Integer) params.get(VIO_PORT);
        checkParam(vioPort, "VIO_PORT");
        String vioQueueManager = (String) params.get(VIO_QUEUE_MANAGER);
        checkParam(vioQueueManager, "VIO_QUEUE_MANAGER");
        String vioQueueChannel = (String) params.get(VIO_QUEUE_CHANNEL);
        checkParam(vioQueueChannel, "VIO_QUEUE_CHANNEL");
        String vioUser = (String) params.get(VIO_USER);
        String vioPassword = (String) params.get(VIO_PASSWORD);
        String vioInQueue = (String) params.get(VIO_IN_QUEUE);
        checkParam(vioInQueue, "VIO_IN_QUEUE");
        String vioOutQueue = (String) params.get(VIO_OUT_QUEUE);
        checkParam(vioOutQueue, "VIO_OUT_QUEUE");

        MqSenderService localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (MqSenderService.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = new MqSenderService();

                    ehdClient = initEhdClient(ehdHost, ehdPort);
                    vioClient = initVioClient(vioHost, vioPort, vioQueueManager, vioQueueChannel,
                            vioUser, vioPassword, vioInQueue, vioOutQueue);
                }
            }
        }

        return INSTANCE;
    }


    private static void checkParam(String param, String paramName) {
        if(param == null || param.isEmpty()) {
            throw new IllegalArgumentException("Param " + paramName + " can't be empty!");
        }
    }

    private static void checkParam(Integer param, String paramName) {
        if(param == null || param == 0) {
            throw new IllegalArgumentException("Param " + paramName + " can't be empty or 0!");
        }
    }

    private static EHDClient initEhdClient(String host, String port) {
        return new EHDClient(host, port);
    }

    private static VIOClient initVioClient(String host, Integer port, String queueManager, String queueChannel, String user, String password, String inQueue, String outQueue) {
        try {
            JmsFactoryFactory jmsFactoryFactory = JmsFactoryFactory.getInstance(WMQ_PROVIDER);
            JmsConnectionFactory connectionFactory = jmsFactoryFactory.createConnectionFactory();

            // Set the properties
            connectionFactory.setStringProperty(WMQ_HOST_NAME, host);
            connectionFactory.setIntProperty(WMQ_PORT, port);
            connectionFactory.setStringProperty(WMQ_CHANNEL, queueChannel);
            connectionFactory.setIntProperty(WMQ_CONNECTION_MODE, WMQ_CM_CLIENT);
            connectionFactory.setStringProperty(WMQ_QUEUE_MANAGER, queueManager);

            Connection connection = connectionFactory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination in = session.createQueue(inQueue);
            Destination out = session.createQueue(outQueue);

            return new VIOClient(in, out, user, password, connectionFactory);
        } catch (Exception e) {
            throw new AssertionError("Error in init VIO client!", e);
        }
    }

    /**
     * Method for send contract notification to queue
     *
     * @param uppFile file UPP
     * @param contractFile file UNPF
     * @param contractNumber can be empty
     * @return json notification which sends to queue
     */
    public String sendContract(File uppFile, File contractFile, VioDocTypes statementType, String contractNumber) {
        return sendStatementOrContract(uppFile, contractFile, MimeTypes.GZIP, statementType, contractNumber);
    }

    /**
     * General method for statement and contract to send notification
     *
     * @param uppFile file UPP
     * @param statementFile file statement or contract
     * @param statementMimeType mime type statement or contract (xml, gzip)
     * @param statementType statement or contract type
     * @param statementNumber statement or contract number
     * @return json notification which sends to queue
     */
    public String sendStatementOrContract(File uppFile, File statementFile, MimeTypes statementMimeType, VioDocTypes statementType, String statementNumber) {
        try {
            if (statementNumber == null) {
                statementNumber = UUID.randomUUID().toString();
            }
            System.out.println(String.format("%s%s",addSpase("Document name:"), statementFile.getName()));
            ContentRequest baseDocumentRequest = new ContentRequest(MimeTypes.GZIP.getType(), statementType.getEhdId(), uppFile.getName());
            String baseDocumentId = ehdClient.addContent(IOUtils.toByteArray(uppFile.toURI()), baseDocumentRequest);
            System.out.println(String.format("%s%s", addSpase("Base document ID in EHD:"), baseDocumentId));

            ContentRequest uppRequest = new ContentRequest(MimeTypes.GZIP.getType(), VioDocTypes.UPP.getEhdId(), uppFile.getName());

            String uppId = ehdClient.addContent(IOUtils.toByteArray(uppFile.toURI()), uppRequest);
            System.out.println(String.format("%s%s", addSpase("UPP ID in EHD:"), uppId));

            ContentRequest statementRequest = new ContentRequest(statementMimeType.getType(), statementType.getEhdId(),
                    statementFile.getName(), statementNumber);
            String statementId = ehdClient.addContent(IOUtils.toByteArray(statementFile.toURI()), statementRequest);
            System.out.println(String.format("%s%s", addSpase("Statement ID in EHD:"), statementId));

            ehdClient.createLinkWithContain(Long.valueOf(baseDocumentId), Long.valueOf(statementId));
            ehdClient.createLink(Long.valueOf(baseDocumentId), Long.valueOf(uppId));

            return vioClient.sendNotification(uppId, baseDocumentId, 97, VioProcessType.ANSWER_ON_INBOX);

        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Error at send statement!", e);
        }
    }

    private static String addSpase (String text){
        int lenght = new String("Base document ID in EHD:").length();
        for (int i = text.length() - 1; i < lenght; i++) {
            text += " ";
        }

        return text;
    }
}
