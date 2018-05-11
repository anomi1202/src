package uspn.test.appmanager.NotificationHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Message {
    private String dateTime;
    private String message;
    private String typeMessage;
    private static ArrayList<Message> messageList;

    public Message(String dateTime, String message, String typeMessage) {
        this.dateTime = dateTime;
        this.message = message;
        this.typeMessage = typeMessage;
    }

    public Message() {

    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Message> createMessageList(String[] arrayMessages){
        messageList = new ArrayList<>();
        for (int i = 0; i < arrayMessages.length; i++) {

            String[] parts = arrayMessages[i].split("\\s");
            dateTime = parts[0] + " " + parts[1];
            message = parts[2];

            for (int j = 3; j < parts.length-1; j++) {
                message += " " + parts[j];
            }
            typeMessage = parts[parts.length - 1];
            messageList.add(new Message(dateTime, message, typeMessage));
        }
        return messageList;
    }

    public static void filteringMessage(ArrayList<Message> messageList, String filterDateTime){
        for (int i = 0; i < messageList.size();) {
            Message mess = messageList.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            try {
                //удаляем те строки, где дата и время больше заданных в filterDateTime
                if (dateFormat.parse(mess.dateTime).getTime() <= dateFormat.parse(filterDateTime).getTime()) {
                    messageList.remove(mess);
                }
                else {
                    i++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
