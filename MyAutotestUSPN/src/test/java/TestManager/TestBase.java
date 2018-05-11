package TestManager;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import uspn.test.appmanager.ApplicationManager;


public class TestBase {
    protected final ApplicationManager app = new ApplicationManager();

    @BeforeSuite (alwaysRun = true)
    public void init(){
        app.initApp();
    }

    @AfterSuite (alwaysRun = true)
    public void stop(){
        app.stopApp();
    }
}
