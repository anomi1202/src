package uspn.test;

import org.apache.logging.log4j.Logger;
import uspn.test.Enum.EnumDocAction;
import uspn.test.Notification.MessageNotification;
import uspn.test.Notification.Notification;

import org.apache.logging.log4j.LogManager;

import java.util.Map;


public class Solution {
    static final Logger logger = LogManager.getLogger(Solution.class);

    private static DocNum docNum;
    private static Notification notificationWindow;
    private static ActionOnDoc actionWindon;
    private static String actionOnDoc;
    private static int typeDoc;

    public static void main(String[] args) {
        logger.info("\n");
        new Solution().run();
    }

    public Solution() {
        typeDoc = 7;

        docNum = new DocNum();
        logger.info("Create docNumList");
        for (Map.Entry<String, String> pair : docNum.getDocMap().entrySet()) {
            logger.info("Document number/date: {}/{}", pair.getKey(), pair.getValue());
        }

        actionOnDoc = EnumDocAction.DELETE.toString();
        logger.info("Start {} documents", actionOnDoc);

        logger.info("Open browser: notification windows");
        notificationWindow = new Notification();

        logger.info("Create notification message");
        notificationWindow.setMessagesNotification(new MessageNotification(EnumDocAction.valueOf(actionOnDoc)));

        logger.info("Open browser: document grid windows");
        actionWindon = new ActionOnDoc();
    }

    private static void run(){
        try {

            /**
             * arg0 - action on doc = actionOnDoc = EnumDocAction.DELETE.toString() - delete docs
             *                                      EnumDocAction.REUPLOAD.toString() - reupload docs
             *                                      EnumDocAction.UPLOAD.toString() - upload docs
             * arg1 - docList
             * arg2 - type of doc = 7 doc (UOPS-S)
             *                      4 doc (RNPF-S)
             *                      6 doc (RNPF-UM)
             * */

            logger.info("Run action on documents with param: action = {}, typeDoc = {}", actionOnDoc, typeDoc);
            actionWindon.runAction(EnumDocAction.valueOf(actionOnDoc), docNum.getDocNumList(), typeDoc);

            logger.info("Check information message on users cabinet");
            notificationWindow.checkAction();
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        finally {
            logger.info("Exit from all browser");
            notificationWindow.exit();
            actionWindon.exit();
        }
    }
}
