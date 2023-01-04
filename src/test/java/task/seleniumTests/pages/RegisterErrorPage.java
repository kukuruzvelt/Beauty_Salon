package task.seleniumTests.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import task.seleniumTests.core.BaseSeleniumPage;

public class RegisterErrorPage extends BaseSeleniumPage {

    @FindBy(id = "errorMessage")
    private WebElement errorMessage;

    public RegisterErrorPage() {
        PageFactory.initElements(driver, this);
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

}

