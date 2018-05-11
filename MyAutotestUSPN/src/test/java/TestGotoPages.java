import TestManager.TestBase;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.testng.Assert.assertEquals;
import static uspn.test.appmanager.data.ServerType.*;

public class TestGotoPages extends TestBase{



    @Test (enabled = false, priority = 1)
    public void checkGotoDocPages() {
        StringBuilder text = new StringBuilder();
        text.append(app.findElement(By.xpath("//div/div/div/ul/li")).getText());
        text.append("/" + app.findElement(By.xpath("//div/div/div/ul/li[2]")).getText());
        text.append("/" + app.findElement(By.xpath("//div/div/div/ul/li[3]")).getText());
        assertEquals(text.toString(), "Входящие документы/Реестры/Реестр решение суда");
    }

    @Test (enabled = true, priority = 1)
    public void checkGotoNotifPages() {
        app.loginTo(DEV);
        app.navigateTo().notificationPages();
        assertTrue(app.findElement(By.id("notificationList_startDate")).isDisplayed());
        assertThat(app.findElement(By.id("notificationList_startDate")).getText().trim(), is("Дата сообщения"));
        assertTrue(app.findElement(By.id("notificationList_shortTitle")).isDisplayed());
        assertThat(app.findElement(By.id("notificationList_shortTitle")).getText().trim(), is("Тема сообщения"));
        assertTrue(app.findElement(By.id("notificationList_notificationMessageType")).isDisplayed());
        assertThat(app.findElement(By.id("notificationList_notificationMessageType")).getText().trim(), is("Тип сообщения"));
        assertTrue(app.findElement(By.id("notificationList_none")).isDisplayed());
        assertThat(app.findElement(By.id("notificationList_none")).getText().trim(), is("Информация"));
    }


}
