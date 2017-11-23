package uspn.test;

import uspn.test.Notification.MessageNotification;
import uspn.test.Notification.Notification;

public class Solution {
    private static DocNum docNum;
    private static Notification notificationWindow;
    private static ActionOnDoc actionWindon;
    private static String actionOnDoc;

    public static void main(String[] args) {
        new Solution().run();
    }

    public Solution() {
        docNum = new DocNum();
        actionOnDoc = EnumDocAction.DELETE.toString();
        notificationWindow = new Notification();
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
            notificationWindow.setMessagesNotification(new MessageNotification(EnumDocAction.valueOf(actionOnDoc)));
            actionWindon.runAction(EnumDocAction.valueOf(actionOnDoc), docNum.getDocNumList(), 7);
            notificationWindow.checkAction();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            notificationWindow.exit();
            actionWindon.exit();
        }
    }
}
