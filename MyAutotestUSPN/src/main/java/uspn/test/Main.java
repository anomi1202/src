package uspn.test;

import org.slf4j.Logger;
import uspn.test.appmanager.data.DocActionType;
import uspn.test.appmanager.NotificationHelper.MessageNotification;


import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


public class Main {
    private static final Logger logger = getLogger(Main.class);

    private static DocNum docNum;
//    private static Notification notificationWindow;
    private static ActionOnDoc actionWindon;
    private static String actionOnDoc;
    private static int typeDoc;

    public static void main(String[] args) {
        logger.info("\n");
        new Main();
        run();
    }

    private Main() {
        typeDoc = 7;

        docNum = new DocNum();
        logger.info("Create docNumList");
        for (Map.Entry<String, String> pair : DocNum.getDocMap().entrySet()) {
            logger.info("Document number/date: {}/{}", pair.getKey(), pair.getValue());
        }

        actionOnDoc = DocActionType.DELETE.toString();
        logger.info("Start {} documents", actionOnDoc);

        logger.info("Open browser: notification windows");
//        notificationWindow = new Notification();

        logger.info("Create notification message");
//        notificationWindow.setMessagesNotification(new MessageNotification(DocActionType.valueOf(actionOnDoc)));

        logger.info("Open browser: document grid windows");
        actionWindon = new ActionOnDoc();
    }

    private static void run(){
        try {

            /**
             * arg0 - action on doc = actionOnDoc = DocActionType.DELETE.toString() - delete docs
             *                                      DocActionType.RETURN.toString() - reupload docs
             *                                      DocActionType.UPLOAD.toString() - upload docs
             * arg1 - docList
             * arg2 - type of doc = 7 doc (UOPS-S)
             *                      4 doc (RNPF-S)
             *                      6 doc (RNPF-UM)
             *
             */

            logger.info("Run action on documents with param: action = {}, typeDoc = {}", actionOnDoc, typeDoc);
            actionWindon.runAction(DocActionType.valueOf(actionOnDoc), docNum.getDocNumList(), typeDoc);

            logger.info("Check information message on users cabinet");
//            notificationWindow.checkAction();
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        finally {
            logger.info("Exit from all browser");
//            notificationWindow.exit();
            actionWindon.exit();
        }
    }
}
