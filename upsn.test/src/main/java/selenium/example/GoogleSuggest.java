package selenium.example;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GoogleSuggest {
    public static void main(String[] args) throws Exception {
        // Драйвер Firefox поддерживает javascript
        System.setProperty("webdriver.chrome.driver", "D:\\MyJavaProject\\upsn.test\\src\\test\\java\\uspn\\test\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // Открываем страничку Google
        driver.get("http://www.google.com.ua/webhp?complete=1&hl=ru");

        WebElement query = driver.findElement(By.name("q"));
        // Вводим ключевое слово
        query.sendKeys("гладиолус");

        // Ждем 5 секунд, или пока не появился div с подсказками
        long end = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < end) {
            WebElement resultsDiv = driver.findElement(By.className("sbdd_b"));

            // Если div отобразился выходим из цикла
            if (resultsDiv.isDisplayed()) {
                break;
            }
        }

        // Получаем список подсказок
        List<WebElement> allSuggestions = driver.findElements(By.xpath("//ul[@class='sbsb_b']"));
        // А затем выводим их в консоль
        for (WebElement suggestion : allSuggestions) {
            System.out.println(suggestion.getText());
        }
    }
}