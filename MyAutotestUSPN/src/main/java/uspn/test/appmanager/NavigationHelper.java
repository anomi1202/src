package uspn.test.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationHelper {
    private WebDriver driver;

    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void notificationPages(){
        driver.findElement(By.id("linkCabinet")).click();
        driver.findElement(By.id("notifyNavTabs")).click();
    }

    public void inDocWorkMenu(){
        driver.findElement(By.id("workDocMenu")).click();
        driver.findElement(By.id("npfDocMenu")).click();
    }

}
