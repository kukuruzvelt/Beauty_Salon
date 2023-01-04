package task.seleniumTests.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import task.seleniumTests.core.BaseSeleniumPage;

public class MainPage extends BaseSeleniumPage {

    @FindBy(xpath = "//body")
    private WebElement message;

    public MainPage() {
        PageFactory.initElements(driver,this);
    }

    public String getMessage() {
        return message.getText();
    }
}
