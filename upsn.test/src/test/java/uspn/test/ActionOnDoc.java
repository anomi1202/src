package uspn.test;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import uspn.test.Drivers.LoginChrome;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static uspn.test.EnumServer.*;


public class ActionOnDoc {
    private static WebDriver driver;
    private static LoginChrome loginChrome;
    private static boolean isDocInCheckBox;
    private static int timerDefault = 5;
    private static int timerMin = 1;

    public ActionOnDoc(){
        loginChrome = new LoginChrome();
    }

    public void runAction(EnumDocAction docAction, ArrayList<String> docNumList, int typeDoc) {
        driver = loginChrome.login(DEV);
        gotoDocNum(typeDoc);
        try {
            checkBoxDoc(docNumList);
            if (isDocInCheckBox)
                actionOnDoc(docAction);

        }
        catch (Exception e){
            e.printStackTrace();
            exit();
        }
    }

    private void gotoDocNum (int docNum){
        try {
            driver.findElement(By.linkText("Работа с документами")).click();
            WebElement element = driver.findElement(By.linkText("Взаимодействие с НПФ"));
            Actions act = new Actions(driver);
            act.moveToElement(element).build().perform();
            driver.findElement(By.linkText("Входящие документы")).click();

            waitLoadGrid();

            driver.findElement(By.xpath("//button[@type='button']")).click();
            switch (docNum) {
                case 4:
                    driver.findElement(By.xpath("//li[@id='V_SPN_R_SUD']/div")).click();
                    break;
                case 7:
                    driver.findElement(By.xpath("//li[@id='V_UVED_COURT']/div")).click();
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            exit();
        }
        finally {
            waitLoadGrid();
        }
    }

    public void checkBoxDoc(ArrayList<String> docNumList){
        //уменьшаем настройки для таймера поиска. Ожидание = timerMin сек
        driver.manage().timeouts().implicitlyWait(timerMin, TimeUnit.SECONDS);
        isDocInCheckBox = false;
        for (String docNum : docNumList) {
            try {
                driver.findElement(By.xpath("//td[@title='" + docNum + "']")).click();
                if (!isDocInCheckBox)
                    isDocInCheckBox = true;
            } catch (NoSuchElementException e){
//                e.printStackTrace();
                System.out.println("DocNum " + docNum + " is not found!  class ActionOnDoc, row 70");
            }
        }

        //возвращаем настройки таймера для поиска обратно на timerDefault секунд
        driver.manage().timeouts().implicitlyWait(timerDefault, TimeUnit.SECONDS);
    }

    private void waitLoadGrid(){
        WebDriverWait waitgrid = new WebDriverWait(driver, 30);
        waitgrid.until(
                new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        while (d.findElement(By.id("load_idDocumentGrid")).isDisplayed()) {
                            if (d.findElement(By.xpath("//div[@style='display: none;'][1]")).isDisplayed())
                                break;
                        }

                        return d.findElement(By.className("ui-jqgrid-bdiv")).isDisplayed(); // ожидаем загрузки грида со списком док-тов
                    }
                }
        );
    }

    private void actionOnDoc(EnumDocAction act){
        try {
            driver.findElement(By.xpath("(//button[@type='button'])[3]")).click(); // Прочие
            switch (act) {
                case REUPLOAD:
                    driver.findElement(By.id("npfInDocReturnMenu")).click(); // Отзыв
                    driver.findElement(By.id("rollbackWindowConfirmButton")).click(); // Отзыв - OK
//                    driver.findElement(By.id("rollbackWindowCancelButton")).click(); // Отзыв = CANCEL
                    break;
                case DELETE :
                    driver.findElement(By.id("npfInDocRemoveMenu")).click(); // Удаление
                    driver.findElement(By.id("removeWindowConfirmButton")).click(); // Удаление - OK
//                driver.findElement(By.id("removeWindowCancelButton")).click(); // Удаление - CANCEL
                    break;
                case UPLOAD :
                    driver.findElement(By.id("npfInDocReflectInSpBtn")).click(); // Отражение
                    driver.findElement(By.id("reflectWindowConfirmButton"));  // Отражение - OK
//                    driver.findElement(By.id("reflectWindowCancelButton"));  // Отражение - CANCEL
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            exit();
        }
    }

    public void exit(){
        loginChrome.exit(driver);
    }

}
