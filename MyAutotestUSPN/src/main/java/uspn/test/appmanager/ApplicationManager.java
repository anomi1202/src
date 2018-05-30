package uspn.test.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import uspn.test.appmanager.data.ServerType;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.*;
import static uspn.test.appmanager.data.ServerType.*;

public class ApplicationManager {
    protected WebDriver driver;
    private Properties propertiesFile = new Properties();
    private NavigationHelper navigationHelper;

    public void initApp(){
        if (driver == null) {
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }

    public void stopApp(){
        if (driver != null) {
            driver.quit();
        }
    }

    public void loginTo(ServerType serverType) {
        try(FileReader propFile = new FileReader("src\\main\\resources\\local_config.properties")) {
            propertiesFile.load(propFile);
            if (propertiesFile.containsKey(String.format("url%s", serverType))) {
                driver.get(propertiesFile.getProperty(String.format("url%s", serverType)));
            } else {
                driver.get(propertiesFile.getProperty(String.format("url%s", DEV)));
            }

            switch (serverType) {
                case QA:
                    inputText(By.id("username"), propertiesFile.getProperty("loginQA"));
                    inputText(By.id("password"), propertiesFile.getProperty("passwordQA"));
                    click(By.cssSelector("input[type='submit']"));
                    break;
                default:
                    click(By.name("submit"));
                    break;
            }
        }
        catch (IOException e){
            e.printStackTrace();
            stopApp();
        }
    }

    public void click(By button) {
        findElement(button).click();
    }

    public WebElement findElement(By locator) {
        WebElement element = new WebDriverWait(driver, 10).until((d) -> driver.findElement(locator));
        // ??? ????????? ????????, ???????? ?????
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return element;
    }

    public void inputText(By field, String text){
        driver.findElement(field).clear();
        driver.findElement(field).sendKeys(text);
    }

    public void exit() {
        click(By.cssSelector("a[href='/uspn/forms/logout']"));
    }


    public NavigationHelper navigateTo(){
        if (navigationHelper == null){
            navigationHelper = new NavigationHelper(driver);
        }
        return navigationHelper;
    }
}
