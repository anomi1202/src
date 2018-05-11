package uspn.test.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.Thread.sleep;

public class GridHelper {
    private WebDriver driver;

    public GridHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void reloadDocsGrid() throws NoSuchElementException, InterruptedException {
        driver.findElement(By.id("collapseOneSelector")).click();
        new WebDriverWait(driver, 10).until((d) -> d.findElement(By.id("criteriaSubmit")));
        sleep(1000); //усыпляем поток на 1 сек, чтобы прогрузилась анимация
        driver.findElement(By.id("criteriaSubmit")).click();

    }
}
