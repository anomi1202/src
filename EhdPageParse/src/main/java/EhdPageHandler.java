import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class EhdPageHandler extends AbstractEhdPage {
    private Logger logger = LoggerFactory.getLogger(EhdPageHandler.class);
    private Path EHDGETTER_PROPERTIES = Paths.get("EHDgetter.properties");
    private EhdDocumentWindow documentCard;
    private String loginUser;
    private String loginPass;
    private String ehdUrl;

    @Parameter(names = "id", description = "Get ID of document from EHD", required = true)
    private boolean getId = false;

    @Parameter(names = {"status", "sts"}, description = "Get status of document from EHD")
    private boolean getStatus = false;

    @Parameter(names = {"-d", "-docnum"}, description = "Number of document", required = true)
    private String docNum = "";

    public static void main(String[] args) {
        EhdPageHandler ehdPage = new EhdPageHandler();
        JCommander jCommander = new JCommander(ehdPage);
        try {
            jCommander.parse(args);
            ehdPage.initHiddenBrowser();
            ehdPage.doRun();
        } catch (ParameterException e){
            jCommander.usage();
        }
    }

    public void doRun(){
        searchDocument();
        if (getId){
            logger.info(String.format("\r\nThe ID of document with number '%s': %d", docNum, getDocumentId()));
        }

        if (getStatus){
            logger.info(String.format("\r\nThe status of document with number '%s': %s", docNum, getDocumentStatus()));
        }
    }

    private void searchDocument(){
        try {
            setDocNumFilter(docNum).search();
            selectFirstDocumentFromGridWithIndex();
            documentCard = openDocumentCard();
        } catch (NoSuchElementException e){
            logger.error("FAILED!", e);
            stop();
            throw e;
        }
    }

    private long getDocumentId(){
        return documentCard.getEhdDocId();
    }

    private String getDocumentStatus(){
        return documentCard.getStatus();
    }

    private void initHiddenBrowser(){
        try {
            initProperties();
            System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
            FirefoxOptions firefoxOptions = new FirefoxOptions().addArguments("--headless", "--no-sandbox");
            driver = new FirefoxDriver(firefoxOptions);
            logger.info("Initialize the hidden browser");

            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.get(ehdUrl.replaceFirst("://", String.format("://%s:%s@", loginUser, loginPass)));
            logger.info(String.format("Load page: %s", ehdUrl));
            waitLoadElementDisappeared();
        } catch (Exception e){
            logger.error("FAILED!", e);
            stop();
            throw e;
        }
    }

    private void initProperties() {
        Properties prop = new Properties();
        try(InputStream is = Files.newInputStream(EHDGETTER_PROPERTIES)){
            prop.load(is);

            loginUser = prop.getProperty("login");
            loginPass = prop.getProperty("password");
            ehdUrl = prop.getProperty("host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop(){
        if (driver != null) {
            driver.quit();
        }
        logger.info("Close the hidden browser");
    }
}
