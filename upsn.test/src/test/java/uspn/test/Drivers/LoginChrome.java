package uspn.test.Drivers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import uspn.test.EnumServer;

import java.util.concurrent.TimeUnit;

public class LoginChrome {
    private static WebDriver driver;

    public LoginChrome(){
        System.setProperty("webdriver.chrome.driver", "D:\\MyJavaProject\\upsn.test\\src\\test\\java\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public WebDriver login(EnumServer server){
        try {
            switch (server) {
                case DEV:
                    loginDEV(driver);
                    break;
                case QA:
                    loginQA(driver);
                    break;
            }
        }
        catch (NoSuchElementException | NullPointerException e){
            e.printStackTrace();
            driver.quit();
        }
        return driver;
    }

    private void loginDEV(WebDriver driver){
        driver.get("http://10.0.2.37:9181/uspn/");
        driver.findElement(By.name("submit")).click();
    }

    private void loginQA(WebDriver driver){
        driver.get("https://mini-ecasa/uspn");
        driver.findElement(By.id("username")).sendKeys("uspn_rkosih");
        driver.findElement(By.id("password")).sendKeys("120291Anomi");
        driver.findElement(By.xpath("//input[@value='Вход в систему']")).click();
    }

    public void exit(WebDriver driver) {
        try {
            driver.findElement(By.linkText("Выход")).click();
            driver.quit();
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
            driver.quit();
        }
    }

    public void reloadPages(WebDriver driver) throws NoSuchElementException, InterruptedException {
        driver.findElement(By.id("collapseOneSelector")).click();
        WebDriverWait waitgrid = new WebDriverWait(driver, 10);
        waitgrid.until(
                new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        boolean buttonFindIsDisplayed;
                        // ожидаем появления кнопки "Поиск"
                        switch (driver.getTitle()) {
                            case "УСПН | Кабинет специалиста" :
                                buttonFindIsDisplayed =  d.findElement(By.id("criteriaNotificationsSubmit")).isDisplayed();
                                break;
                            case "УСПН | Работа с документами | Взаимодействие с НПФ | Входящие документы" :
                                buttonFindIsDisplayed =  d.findElement(By.id("criteriaSubmit")).isDisplayed();
                                break;
                            default: buttonFindIsDisplayed = false;
                        }

                        return buttonFindIsDisplayed;
                    }
                }
        );
        Thread.sleep(1000); //усыпляем поток на 1 сек, чтобы прогрузилась анимация
        driver.findElement(By.id("criteriaNotificationsSubmit")).click();
    }
}
