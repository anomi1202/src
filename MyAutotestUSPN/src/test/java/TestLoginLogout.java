import TestManager.TestBase;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static uspn.test.appmanager.data.ServerType.DEV;
import static uspn.test.appmanager.data.ServerType.QA;
import static uspn.test.appmanager.data.ServerType.USPNDEV;

public class TestLoginLogout extends TestBase{


    @Test (enabled = false, priority = 0)
    public void testLoginQA() {
        app.loginTo(QA);
        assertEquals(app.findElement(By.id("user")), "Косых Руслан");
    }

    @Test (enabled = false, priority = 0, dependsOnMethods = "testLoginQA")
    public void testExitQA() {
        app.exit();
        assertEquals(app.findElement(By.id("title")).getText(), "Выход из системы выполнен успешно");
    }


    @Test (priority = 1, groups = {"testLoginDEV"})
    public void testLoginDEV() {
        app.loginTo(DEV);
        assertEquals(app.findElement(By.id("user")), "admin");
    }

    @Test (priority = 1, dependsOnMethods = "testLoginDEV")
    public void testExitDEV() {
        app.exit();
        assertTrue(app.findElement(By.cssSelector("input[name='username']")).isDisplayed());
        assertTrue(app.findElement(By.cssSelector("input[name='password']")).isDisplayed());
    }


    @Test (priority = 2, groups = {"testLoginUSPNDEV"})
    public void testLoginUSPNDEV() {
        app.loginTo(USPNDEV);
        assertEquals(app.findElement(By.id("user")), "admin");
    }

    @Test (priority = 2, dependsOnMethods = "testLoginUSPNDEV")
    public void testExitUSPNDEV() {
        app.exit();
        assertTrue(app.findElement(By.cssSelector("input[name='username']")).isDisplayed());
        assertTrue(app.findElement(By.cssSelector("input[name='password']")).isDisplayed());
    }


}
