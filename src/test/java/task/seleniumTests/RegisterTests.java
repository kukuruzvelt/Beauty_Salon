package task.seleniumTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.seleniumTests.core.BaseSeleniumTest;
import task.seleniumTests.pages.MainPage;
import task.seleniumTests.pages.RegisterErrorPage;
import task.seleniumTests.pages.RegisterPage;

import static task.seleniumTests.readProperties.ConfigProvider.*;

public class RegisterTests extends BaseSeleniumTest {

    @Test
    public void registerNewUserTest() {
        MainPage mainPage = new RegisterPage().register(NEWUSER_NAME, NEWUSER_SURNAME, NEWUSER_EMAIL, NEWUSER_PASSWORD);
        Assertions.assertTrue(mainPage.getMessage().contains(NEWUSER_MESSAGE));
    }

    @Test
    public void registerAlreadyLoggedUserTest() {
        RegisterErrorPage registerErrorPage = new RegisterPage().registerError(NEWUSER_NAME, NEWUSER_SURNAME, NEWUSER_EMAIL, NEWUSER_PASSWORD);
        Assertions.assertTrue(registerErrorPage.getErrorMessage().contains(EMAIL_TAKEN_ERROR_MESSAGE));
    }

    @Test
    public void checkFillingAllFields() {
        RegisterErrorPage registerErrorPage = new RegisterPage().registerError(NEWUSER_NAME, NEWUSER_SURNAME, NEWUSER_EMAIL, "");
        Assertions.assertTrue(registerErrorPage.getErrorMessage().contains(FILLING_ALL_FIELDS_ERROR_MESSAGE));
    }
}
