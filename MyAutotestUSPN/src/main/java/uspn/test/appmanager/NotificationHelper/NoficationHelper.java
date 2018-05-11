package uspn.test.appmanager.NotificationHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class NoficationHelper {
    private static MessageNotification messagesNotification;
    private static HashMap<String, String> notifMessageMap;
    private static String countNotificationRows = "40";
    private static String dateTime;
    private WebDriver driver;

    public NoficationHelper(WebDriver driver) {
        this.driver = driver;
    }


    private void waitLoadGrid() throws TimeoutException {
        new WebDriverWait(driver, 30).until((d) -> d.findElement(By.className("ui-jqgrid-bdiv")).isDisplayed());
    }

    public void reloadNotificationsGrid() throws NoSuchElementException, InterruptedException {
        driver.findElement(By.id("collapseOneSelector")).click();
        new WebDriverWait(driver, 10).until((d) -> d.findElement(By.id("criteriaNotificationsSubmit")));
        sleep(1000); //усыпляем поток на 1 сек, чтобы прогрузилась анимация
        driver.findElement(By.id("criteriaNotificationsSubmit")).click();
    }

    public boolean findMessage(String findMessage) {
        boolean isFind = false;
        try {
//            login.reloadPages(driver); // обновляем грид
            //получаем список сообщений
            ArrayList<Message> messages = new Message().createMessageList(driver.findElement(By.xpath("//table[@id=\"notificationList\"]")).getText().split("\\n"));
            //оставляем в списке только те сообщения, время которых больше того, когда мы загужали страницу
            Message.filteringMessage(messages, dateTime);
            // ищем нужное нам сообщение
            for (Message mess : messages) {
                isFind = mess.getMessage().equals(findMessage);
                if (isFind)
                    break;
            }

//            // ищем в гриде нужное нам сообщение
//            isFind =  driver.findElement(By.xpath("//td[@title='" + findMessage + "']")).isDisplayed();
        } catch ( NoSuchElementException e) {
//            e.printStackTrace();
            System.out.println("Search error messages. Message: " + findMessage + ". class NotificationHelper, row 70");
        } finally {
            return isFind;
        }
    }

    public void checkAction(){
        notifMessageMap = new HashMap<>();
        for (String message : messagesNotification.getNotificationList()) {
            notifMessageMap.put(message, "not found");
        }
        for (Map.Entry<String, String > pair : notifMessageMap.entrySet()) {
            String message = pair.getKey();
            String checkMessage = pair.getValue();

            try {
                WebDriverWait waitMessage = new WebDriverWait(driver, 30);
                waitMessage.until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        boolean findResult = findMessage(message);

                        if (findResult){
                            pair.setValue("The message was found.");
                            System.out.println("The message was found. Message: " + message);
                        }
                        return findResult;
                    }
                });
            } catch (TimeoutException e){
//                e.printStackTrace();
                System.out.println("The message was NOT found. Message: " + message);
            }
        }
    }

    public void setMessagesNotification(MessageNotification messagesNotification) {
        NoficationHelper.messagesNotification = messagesNotification;
    }
}
