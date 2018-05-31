package EHDComminucation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class EhdPageHandler extends AbstractEhdPage{
    private Logger logger = LoggerFactory.getLogger(EhdPageHandler.class);
    private final String loginUser = "aistest_fa";
    private final String loginPass = "Qwer1234";
    private final String ehdUrl = "http://10.0.12.235:2222/uds-admin/";

    public EhdPageHandler() {
        System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        FirefoxOptions firefoxOptions = new FirefoxOptions().addArguments("--headless", "--no-sandbox");
        driver = new FirefoxDriver(firefoxOptions);
        logger.info("Инициализация скрытого браузера");

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(ehdUrl.replaceFirst("://", String.format("://%s:%s@", loginUser, loginPass)));
        logger.info(String.format("Загрузка страницы: %s", ehdUrl));
        waitLoadElementDisappeared();
    }

    private EhdDocumentWindow readDocumentCard(){
        return PageFactory.initElements(driver, EhdDocumentWindow.class);
    }

    private void search(){
        clickButton(driver.findElement(searchButton));
    }

    private EhdPageHandler selectFirstDocumentFromGridWithIndex(){
        List<WebElement> docList = getDocumentTable();
        logger.info("Считывание списка последних документов");
        if (docList.size() > 0) {
            docList.get(0).findElements(By.xpath("child::td")).get(0).click();
        } else {
            throw new NoSuchElementException("Не найден ни один подходящий документ");
        }

        return this;
    }

    private void openDocumentCard(){
        clickButton(driver.findElement(viewButton));
        logger.info("Открытие карточки выбранного документа");
    }

    private void stop(){
        driver.quit();
        logger.info("Закрытие скрытого браузера");
    }

    public EhdPageHandler searchDocument(String docNum){
        setDocNumFilter(docNum);
        search();
        return this;
    }

    public long getDocumentId(){
        long docId;
        try {
            selectFirstDocumentFromGridWithIndex().openDocumentCard();
            docId = readDocumentCard().getEhdDocId();
        } catch (NoSuchElementException e){
            logger.error("FAILED!", e);
            throw e;
        } finally {
            stop();
        }
        return docId;
    }
}
