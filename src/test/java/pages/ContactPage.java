package pages;

import org.openqa.selenium.By;
import utils.ASM_Framework;

import java.io.File;

/**
 * ContactPage — Represents the /contact_us page.
 *
 * <p>Covers: TC06.</p>
 *
 * @author ASMahrous
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

    // =====================================================================

    public ContactPage(ASM_Framework driver) { super(driver); }
    public ContactPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/contact_us"); }

    public boolean isGetInTouchVisible()
    {
        return driver.validateElementIsDisplayed(
                driver.findElement("xpath", "//h2[text()='Get In Touch']"));
    }

    public void fillContactForm(String name, String email, String subject, String message)
    {
        driver.writeInElement(nameField,    name);
        driver.writeInElement(emailField,   email);
        driver.writeInElement(subjectField, subject);
        driver.writeInElement(messageField, message);
    }

    /**
     * Uploads a file via the hidden file input.
     * The path is resolved to an absolute path so Selenium can locate it.
     *
     * @param relativePath path relative to the project root
     *                     (e.g. "src/test/resources/testdata/upload_sample.txt")
     */
    public void uploadFile(String relativePath)
    {
        String absolutePath = System.getProperty("user.dir")
                + File.separator + relativePath;
        driver.findElement("name", "upload_file").sendKeys(absolutePath);
    }

    public void clickSubmit()
    {
        driver.clickElement(submitButton);
    }

    public void acceptConfirmationAlert()
    {
        driver.acceptAlert();
    }

    public String getSuccessMessageText()
    {
        return driver.getElementText(successMessage);
    }

    public void clickHomeButton()
    {
        driver.clickElement(homeButton);
    }
}