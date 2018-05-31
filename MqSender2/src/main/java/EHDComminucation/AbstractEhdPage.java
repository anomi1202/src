package EHDComminucation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEhdPage {
    private Logger logger = LoggerFactory.getLogger(AbstractEhdPage.class);
    protected WebDriver driver;
    protected final By LOADING_BANNER = By.xpath("//div[contains(@id, 'loadmask') and contains(concat(' ',@class,' '), 'x-mask-msg') and not(contains(@style, 'display: none'))]");
    protected final By DOC_NUM_INPUT_FIELD = By.xpath("//tr[./td//label[text()='Номер документа:']]/td//input[@type='text' and @name='documentNumber']");
    protected final By viewButton = By.xpath("//a[contains(@id, 'button')]//span[text()='Просмотреть']");
    protected final By searchButton = By.xpath("//a[contains(@id, 'DefaultButton')]//span[text()='Поиск']");
    private final By documentsGrid = By.xpath("//div[contains(@id, 'gridview')]//tr[contains(@id, 'gridview') and contains(@class, 'x-grid-row')]");


    protected void waitLoadElementDisappeared(){
        boolean elementIsVisible = false;
        for (int steps = 0; steps < 5; steps++) {
            elementIsVisible = initFluentWait(30, 3).until(
                    (driver) -> {
                        WebElement element = driver.findElement(LOADING_BANNER);
                        if (!element.getText().equals("")) {
                            logger.info(String.format("Waiting for the process to finish: %s", element.getText()));
                        }
                        return !element.isDisplayed();
                    }
            );
        }

        if (!elementIsVisible){
            logger.error("FAILED! The EHD page load failed!");
            throw new RuntimeException("The EHD page load failed!");
        }
    }

    protected WebElement findElement(By by){
        return initFluentWait(30, 3).until( d -> driver.findElement(by));
    }

    protected List<WebElement> getDocumentTable(){
        return driver.findElements(documentsGrid);
    }

    protected void clickButton(WebElement element){
        element.click();
        waitLoadElementDisappeared();
    }

    private Wait<WebDriver> initFluentWait(long timeOut, long polittingEvety){
        return new FluentWait<>(driver)
                .withTimeout(timeOut, TimeUnit.SECONDS)
                .pollingEvery(polittingEvety, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    protected AbstractEhdPage setDocNumFilter(String docNum){
        driver.findElement(DOC_NUM_INPUT_FIELD).clear();
        driver.findElement(DOC_NUM_INPUT_FIELD).sendKeys(docNum);
        logger.info(String.format("Search document with number: %s", docNum));

        return this;
    }
}
