package task.seleniumTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.seleniumTests.pages.AccountPage;
import task.seleniumTests.core.BaseSeleniumTest;
import task.seleniumTests.pages.LoginPage;

import static task.seleniumTests.readProperties.ConfigProvider.*;

public class LoginTests extends BaseSeleniumTest {

    @Test
    public void checkAdminLogin() {
        AccountPage accountPage = new LoginPage().login(ADMIN_LOGIN, ADMIN_PASSWORD);
        Assertions.assertEquals(ADMIN_TITLE, accountPage.getTitle());
    }

    @Test
    public void checkMasterLogin() {
        AccountPage accountPage = new LoginPage().login(MASTER_LOGIN, MASTER_PASSWORD);
        Assertions.assertEquals(MASTER_TITLE, accountPage.getTitle());
    }

    @Test
    public void checkUserLogin() {
        AccountPage accountPage = new LoginPage().login(USER_LOGIN, USER_PASSWORD);
        Assertions.assertEquals(USER_TITLE, accountPage.getTitle());
    }
}
