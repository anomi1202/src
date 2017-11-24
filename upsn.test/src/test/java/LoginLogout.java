import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginLogout {
    private static WebDriver driver;
    private static WebDriver driverUnit;
    private static WebDriver driverNotification;
    private static String docPages = "https://mini-ecasa/uspn/forms/npf/incomingDocuments";
    private static String notificationPages = "https://mini-ecasa/uspn/forms/cabinet/home";

    private static void login(WebDriver driver) {
        driver.get("https://mini-ecasa/uspn");
        driver.findElement(By.id("username")).sendKeys("uspn_rkosih");
        driver.findElement(By.id("password")).sendKeys("120291Anomi");
        driver.findElement(By.xpath("//input[@value='Вход в систему']")).click();
    }

    private static void exitFromPages(WebDriver driver) {
        driver.findElement(By.linkText("Выход")).click();
    }

    private static void gotoDoc(WebDriver driver) {
        driver.get("https://mini-ecasa/uspn/forms/npf/incomingDocuments");
        driver.findElement(By.xpath("//li[@id='workDocMenu']/a/span")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Входящие\n                            документы')]")).click();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\MyJavaProject\\upsn.test\\src\\test\\java\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        login(driver);
        driver.get(docPages);
        driver.findElement(By.xpath("//button[@type='button']")).click();
        driver.findElement(By.xpath("//li[@id='V_SPN_R_SUD']/div")).click();

        driverNotification = new ChromeDriver();
        driverNotification.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        login(driverNotification);
        driverNotification.get(notificationPages);
        driverNotification.findElement(By.id("notifyNavTabs")).click();
    }

    @Test
    public void checkLogin() throws Exception {
        assertEquals(driver.findElement(By.id("user")).getText(), "Косых Руслан");
        assertEquals(driverNotification.findElement(By.id("user")).getText(), "Косых Руслан");
    }

    @Test
    public void checkGotoDocPages() throws Exception {
        StringBuilder text = new StringBuilder();
        text.append(driver.findElement(By.xpath("//div/div/div/ul/li")).getText());
        text.append("/" + driver.findElement(By.xpath("//div/div/div/ul/li[2]")).getText());
        text.append("/" + driver.findElement(By.xpath("//div/div/div/ul/li[3]")).getText());
        assertEquals(text.toString(), "Входящие документы/Реестры/Реестр решение суда");
    }

    @Test
    public void checkGotoNotifPages() throws Exception {
        assertEquals(driverNotification.findElement(By.id("jqgh_notificationList_startDate")).getText(), "Дата сообщения");
        assertEquals(driverNotification.findElement(By.id("jqgh_notificationList_title")).getText(), "Тема сообщения");
        assertEquals(driverNotification.findElement(By.id("jqgh_notificationList_notificationMessageType")).getText(), "Тип сообщения");
        assertEquals(driverNotification.findElement(By.id("jqgh_notificationList_none")).getText(), "Информация");
    }

    @Test
    public void exitPages() {
        exitFromPages(driver);
        exitFromPages(driverNotification);
        assertEquals(driver.findElement(By.id("title")).getText(), "Выход из системы выполнен успешно");
        assertEquals(driverNotification.findElement(By.id("title")).getText(), "Выход из системы выполнен успешно");
    }


    @AfterClass
    public static void tearDown() throws Exception {
        exitFromPages(driver);
        driver.quit();
        driverNotification.quit();
    }

}
