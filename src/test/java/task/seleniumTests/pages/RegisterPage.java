package task.seleniumTests.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import task.seleniumTests.core.BaseSeleniumPage;

import static task.seleniumTests.readProperties.ConfigProvider.URL;

public class RegisterPage extends BaseSeleniumPage {

    @FindBy(id = "n")
    private WebElement nameField;

    @FindBy (id = "s")
    private WebElement surnameField;

    @FindBy (id = "e")
    private WebElement emailField;

    @FindBy (id = "p")
    private WebElement passwordField;

    public RegisterPage() {
        driver.get(URL + "register/");
        PageFactory.initElements(driver, this);
    }

    public MainPage register(String name, String surname, String email, String password) {
        nameField.sendKeys(name);
        surnameField.sendKeys(surname);
        emailField.sendKeys(email);
        passwordField.sendKeys(password, Keys.ENTER);
        return new MainPage();
    }

    public RegisterErrorPage registerError(String name, String surname, String email, String password) {
        nameField.sendKeys(name);
        surnameField.sendKeys(surname);
        emailField.sendKeys(email);
        passwordField.sendKeys(password, Keys.ENTER);
        return new RegisterErrorPage();
    }

}
