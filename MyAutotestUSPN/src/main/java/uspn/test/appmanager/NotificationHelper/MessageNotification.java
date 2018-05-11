package uspn.test.appmanager.NotificationHelper;

import uspn.test.DocNum;
import uspn.test.appmanager.data.DocActionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MessageNotification {
    private static ArrayList<String> notificationList;

    public MessageNotification(DocActionType docActionType){
        notificationList = new ArrayList<>();
        insertToList(docActionType);
    }

    private void insertToList(DocActionType docActionType){
        HashMap<String, String> docMap = DocNum.getDocMap();
        String currDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        for (HashMap.Entry<String, String> pair : docMap.entrySet()) {
            String docNum = pair.getKey();
            String docDate = pair.getValue();

            String partOneDocMessage = "Документ №" + docNum + " дата " + docDate;
            switch (docActionType) {
                case RETURN:
                    notificationList.add("Документ(ы) поставлен(ы) в очередь на отзыв");
                    notificationList.add(partOneDocMessage + " отправлен в сервис СПУ на обработку.");
                    notificationList.add(partOneDocMessage + " отозван.");
                    break;
                case DELETE:
                    notificationList.add(partOneDocMessage + " удален.");
                    break;
                case UPLOAD:
                    notificationList.add("Документ(ы) поставлен(ы) в очередь на проверку БК");
                    notificationList.add(partOneDocMessage + " был проверен.");
                    notificationList.add(partOneDocMessage + " был проверен. Имеются ошибки.");
                    notificationList.add(partOneDocMessage + " отправлен в сервис СПУ на обработку.");
                    notificationList.add("Документ сформирован. Тип: \"СК-ПФР\", Дата: \"" + currDate + "\", Номер: \"" + "\".\"");
                    notificationList.add(partOneDocMessage + " отражен.");
                    break;
            }
        }
    }

    public ArrayList<String> getNotificationList() {
        return notificationList;
    }
}
