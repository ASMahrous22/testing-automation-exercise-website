package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import testdata.UserData;
import utils.ASM_Framework;
import utils.AdsHelper;

/**
 * LoginPage — /login page (login + signup + account-info forms).
 */
public class LoginPage extends BasePage
{
    // ── Login form ────────────────────────────────────────────────────────
    private final By loginHeading       = By.cssSelector("div.login-form h2");
    private final By loginEmailField    = By.cssSelector("[data-qa='login-email']");
    private final By loginPasswordField = By.cssSelector("[data-qa='login-password']");
    private final By loginButton        = By.cssSelector("[data-qa='login-button']");
    private final By loginErrorMsg      = By.xpath("//p[text()='Your email or password is incorrect!']");

    // ── Signup form ───────────────────────────────────────────────────────
    private final By signupHeading    = By.cssSelector("div.signup-form h2");
    private final By signupNameField  = By.name("name");
    private final By signupEmailField = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton     = By.cssSelector("button[data-qa='signup-button']");
    private final By signupErrorMsg   = By.xpath("//p[text()='Email Address already exist!']");

    // ── Account Information form ──────────────────────────────────────────
    private final By enterAccountInfoHeading = By.xpath("//b[contains(text(),'Enter Account Information')]");
    private final By titleMrRadio     = By.id("id_gender1");
    private final By titleMrsRadio    = By.id("id_gender2");
    private final By passwordField    = By.id("password");
    private final By dayDropdown      = By.id("days");
    private final By monthDropdown    = By.id("months");
    private final By yearDropdown     = By.id("years");
    private final By newsletterCheck  = By.id("newsletter");
    private final By offersCheck      = By.id("optin");
    private final By firstNameField   = By.id("first_name");
    private final By lastNameField    = By.id("last_name");
    private final By companyField     = By.id("company");
    private final By address1Field    = By.id("address1");
    private final By address2Field    = By.id("address2");
    private final By countryDropdown  = By.id("country");
    private final By stateField       = By.id("state");
    private final By cityField        = By.id("city");
    private final By zipcodeField     = By.id("zipcode");
    private final By mobileField      = By.id("mobile_number");
    private final By createAccountBtn = By.cssSelector("button[data-qa='create-account']");

    // ── Post-creation ─────────────────────────────────────────────────────
    private final By accountCreatedHeading = By.cssSelector("div h2[data-qa='account-created'] b");
    private final By continueBtn           = By.cssSelector("div a[data-qa='continue-button']");
    private final By loggedInLabel         = By.xpath("//a[contains(.,'Logged in as')]");

    // =====================================================================

    public LoginPage(ASM_Framework driver) { super(driver); }
    public LoginPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/login"); }

    // ── Heading visibility ────────────────────────────────────────────────

    public boolean isLoginHeadingVisible()
    {
        waitFor(loginHeading);
        return wd().findElement(loginHeading).isDisplayed();
    }

    public boolean isSignupHeadingVisible()
    {
        waitFor(signupHeading);
        return wd().findElement(signupHeading).isDisplayed();
    }

    public boolean isEnterAccountInfoVisible()
    {
        waitFor(enterAccountInfoHeading);
        return wd().findElement(enterAccountInfoHeading).isDisplayed();
    }

    public boolean isAccountCreatedVisible()
    {
        waitFor(accountCreatedHeading);
        return wd().findElement(accountCreatedHeading).isDisplayed();
    }

    // ── Login actions ─────────────────────────────────────────────────────

    public void loginWith(String email, String password)
    {
        killAds();
        driver.writeInElement(loginEmailField,    email);
        driver.writeInElement(loginPasswordField, password);
        safeClick(loginButton);
    }

    public boolean isLoginErrorVisible()
    {
        try
        {
            waitFor(loginErrorMsg);
            return wd().findElement(loginErrorMsg).isDisplayed();
        }
        catch (Exception e) { return false; }
    }

    // ── Signup step 1 ─────────────────────────────────────────────────────

    public void fillSignupForm(String name, String email)
    {
        killAds();
        driver.writeInElement(signupNameField,  name);
        driver.writeInElement(signupEmailField, email);
        safeClick(signupButton);
    }

    public boolean isSignupErrorVisible()
    {
        try
        {
            waitFor(signupErrorMsg);
            return wd().findElement(signupErrorMsg).isDisplayed();
        }
        catch (Exception e) { return false; }
    }

    // ── Signup step 2 — account information ──────────────────────────────

    public void fillAccountInformation(UserData u)
    {
        killAds();

        // Title radio
        if ("Mr".equalsIgnoreCase(u.title)) wd().findElement(titleMrRadio).click();
        else                                wd().findElement(titleMrsRadio).click();

        // Password + DOB
        driver.writeInElement(passwordField, u.password);
        new Select(wd().findElement(dayDropdown))  .selectByValue(u.dayOfBirth);
        new Select(wd().findElement(monthDropdown)).selectByVisibleText(u.monthOfBirth);
        new Select(wd().findElement(yearDropdown)) .selectByValue(u.yearOfBirth);

        // Checkboxes
        wd().findElement(newsletterCheck).click();
        wd().findElement(offersCheck).click();

        // Address
        driver.scrollToElement(firstNameField);
        driver.writeInElement(firstNameField, u.firstName);
        driver.writeInElement(lastNameField,  u.lastName);
        driver.writeInElement(companyField,   u.company);
        driver.writeInElement(address1Field,  u.address1);
        driver.writeInElement(address2Field,  u.address2);
        new Select(wd().findElement(countryDropdown)).selectByVisibleText(u.country);
        driver.writeInElement(stateField,     u.state);
        driver.writeInElement(cityField,      u.city);
        driver.writeInElement(zipcodeField,   u.zipcode);
        driver.writeInElement(mobileField,    u.mobileNumber);

        // Create Account — safeClick with retry
        safeClick(createAccountBtn);
    }

    /**
     * Clicks Continue on the "ACCOUNT CREATED!" page using JS click
     * (same approach as the proven RegisterUserTest — bypasses google_vignette).
     * Then waits for the logged-in navbar label to confirm the redirect.
     */
    public void clickContinueAfterCreation()
    {
        AdsHelper.dismissBrowserPopups(wd());
        // JS click — identical to the working test's jsClick(By.cssSelector("div a[data-qa='continue-button']"))
        jsClick(continueBtn);
        // Wait for logged-in label to confirm home-page redirect completed
        waitFor(loggedInLabel);
    }
}
