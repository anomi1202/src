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

            String partOneDocMessage = "�������� �" + docNum + " ���� " + docDate;
            switch (docActionType) {
                case RETURN:
                    notificationList.add("��������(�) ���������(�) � ������� �� �����");
                    notificationList.add(partOneDocMessage + " ��������� � ������ ��� �� ���������.");
                    notificationList.add(partOneDocMessage + " �������.");
                    break;
                case DELETE:
                    notificationList.add(partOneDocMessage + " ������.");
                    break;
                case UPLOAD:
                    notificationList.add("��������(�) ���������(�) � ������� �� �������� ��");
                    notificationList.add(partOneDocMessage + " ��� ��������.");
                    notificationList.add(partOneDocMessage + " ��� ��������. ������� ������.");
                    notificationList.add(partOneDocMessage + " ��������� � ������ ��� �� ���������.");
                    notificationList.add("�������� �����������. ���: \"��-���\", ����: \"" + currDate + "\", �����: \"" + "\".\"");
                    notificationList.add(partOneDocMessage + " �������.");
                    break;
            }
        }
    }

    public ArrayList<String> getNotificationList() {
        return notificationList;
    }
}
