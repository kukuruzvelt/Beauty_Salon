package task.seleniumTests.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import task.seleniumTests.core.BaseSeleniumPage;

public class AccountPage extends BaseSeleniumPage {

    @FindBy(xpath = "//h3")
    private WebElement title;

    public AccountPage() {
        PageFactory.initElements(driver,this);
    }

    public String getTitle(){
        return title.getText();
    }
}
