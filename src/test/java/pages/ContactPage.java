package pages;

import org.openqa.selenium.By;
import utils.ASM_Framework;
import java.io.File;

/**
 * ContactPage — /contact_us page. Covers TC06.
 */
public class ContactPage extends BasePage
{
    private final By getInTouchHeading = By.xpath("//h2[text()='Get In Touch']");
    private final By nameField         = By.cssSelector("[data-qa='name']");
    private final By emailField        = By.cssSelector("[data-qa='email']");
    private final By subjectField      = By.cssSelector("[data-qa='subject']");
    private final By messageField      = By.id("message");
    private final By uploadFileInput   = By.name("upload_file");
    private final By submitButton      = By.cssSelector("[data-qa='submit-button']");
    private final By successMessage    = By.cssSelector(".status.alert.alert-success");
    private final By homeButton        = By.cssSelector(".btn.btn-success");

    public ContactPage(ASM_Framework driver) { super(driver); }
    public ContactPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/contact_us"); }

    public boolean isGetInTouchVisible()
    {
        waitFor(getInTouchHeading);
        return wd().findElement(getInTouchHeading).isDisplayed();
    }

    public void fillContactForm(String name, String email, String subject, String message)
    {
        killAds();
        driver.writeInElement(nameField,    name);
        driver.writeInElement(emailField,   email);
        driver.writeInElement(subjectField, subject);
        driver.writeInElement(messageField, message);
    }

    public void uploadFile(String relativePath)
    {
        String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;
        wd().findElement(uploadFileInput).sendKeys(absolutePath);
    }

    public void clickSubmit()             { safeClick(submitButton); }
    public void acceptConfirmationAlert() { driver.acceptAlert(); }

    public String getSuccessMessageText()
    {
        waitFor(successMessage);
        return wd().findElement(successMessage).getText();
    }

    public void clickHomeButton()         { jsClick(homeButton); }
}
