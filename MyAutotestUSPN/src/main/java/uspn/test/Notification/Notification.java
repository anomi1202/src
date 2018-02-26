package uspn.test.Notification;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import uspn.test.Drivers.LoginChrome;
import java.util.*;

import static uspn.test.Enum.EnumServer.DEV;

public class Notification {
    private static WebDriver driver;
    private static LoginChrome login;
    private static MessageNotification messagesNotification;
    private static HashMap<String, String> notifMessageMap;
    private static String countNotificationRows = "40";
    private static String dateTime;

    public Notification(){
        login = new LoginChrome();
//        login = new LoginChrome();
        driver = login.login(DEV);
        gotoNotifyPages();
    }

    private void gotoNotifyPages(){
        try {
            driver.findElement(By.id("linkCabinet")).click();
            driver.findElement(By.id("notifyNavTabs")).click();
            waitLoadGrid();

            //фиксируем время последненго сообщения
            String lastMessage = driver.findElement(By.xpath("//table[@id=\"notificationList\"]")).getText().split("\\n")[0];
            dateTime = lastMessage.split("\\s")[0] + " " + lastMessage.split("\\s")[1];

            //выставляем кол-во сообщений в гриде = 40
            Select countGridRows = new Select(driver.findElement(By.xpath("//select")));
            countGridRows.selectByValue(countNotificationRows);
        } catch (Exception e){
            e.printStackTrace();
            login.exit(driver);
        }
    }

    private void waitLoadGrid() throws TimeoutException {
        WebDriverWait waitgrid = new WebDriverWait(driver, 30);
        waitgrid.until(
                new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        // ожидаем загрузки грида со списком
                        return d.findElement(By.className("ui-jqgrid-bdiv")).isDisplayed();
                    }
                }
        );
    }

    public boolean findMessage(String findMessage) {
        boolean isFind = false;
        try {
            login.reloadPages(driver); // обновляем грид
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
        } catch (InterruptedException | NoSuchElementException e) {
//            e.printStackTrace();
            System.out.println("Search error messages. Message: " + findMessage + ". class Notification, row 70");
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
        Notification.messagesNotification = messagesNotification;
    }

    public void exit(){
        login.exit(driver);
    }
}
