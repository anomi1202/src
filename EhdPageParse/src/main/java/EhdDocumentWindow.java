import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EhdDocumentWindow {
    Logger logger = LoggerFactory.getLogger(EhdDocumentWindow.class);
    private WebDriver driver;

    @FindBy(xpath = ".//div[contains(@id, 'window')']//span[@text()='Карточка документа']")
    private WebElement docCard;

    @FindBy(xpath = ".//tr[./td//label[text()='Статус:']]/td//input[@type='text' and @name='status' and @readonly='readonly']")
    private WebElement docStatus;

    @FindBy(xpath = ".//tr[./td//label[text()='ID документа:']]/td//input[@type='text' and @name='id']")
    private WebElement ehdDocId;


    public String getStatus() {
        return docStatus.getAttribute("value");
    }

    public long getEhdDocId() {
        long id = 0;
        try {
            id = Long.parseLong(ehdDocId.getAttribute("value"));
        } catch (RuntimeException e){
            logger.error("FAILED", e);
        }

        return id;
    }
}
