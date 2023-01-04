package task.seleniumTests.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import task.seleniumTests.core.BaseSeleniumPage;

import static task.seleniumTests.readProperties.ConfigProvider.URL;

public class LoginPage extends BaseSeleniumPage {

    @FindBy(id = "username")
    private WebElement loginField;

    @FindBy (id = "password")
    private WebElement passwordField;

    public LoginPage() {
        driver.get(URL + "login/");
        PageFactory.initElements(driver, this);
    }

    public AccountPage login(String login, String password){
        loginField.sendKeys(login);
        passwordField.sendKeys(password, Keys.ENTER);
        return new AccountPage();
    }

}
