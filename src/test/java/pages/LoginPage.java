package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import testdata.UserData;
import utils.ASM_Framework;

/**
 * LoginPage — Represents the /login page that contains both the
 * "Login to your account" form and the "New User Signup!" form.
 *
 * <p>Covers: TC01–TC05.</p>
 *
 * @author ASMahrous
 */
public class LoginPage extends BasePage
{
    // ── Login form ────────────────────────────────────────────────────────
    private final By loginHeading       = By.xpath("//h2[text()='Login to your account']");
    private final By loginEmailField    = By.cssSelector("[data-qa='login-email']");
    private final By loginPasswordField = By.cssSelector("[data-qa='login-password']");
    private final By loginButton        = By.cssSelector("[data-qa='login-button']");
    private final By loginErrorMsg      = By.xpath("//p[text()='Your email or password is incorrect!']");

    // ── Signup form ───────────────────────────────────────────────────────
    private final By signupHeading    = By.xpath("//h2[text()='New User Signup!']");
    private final By signupNameField  = By.cssSelector("[data-qa='signup-name']");
    private final By signupEmailField = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton     = By.cssSelector("[data-qa='signup-button']");
    private final By signupErrorMsg   = By.xpath("//p[text()='Email Address already exist!']");

    // ── Account Information form (after clicking Signup) ──────────────────
    private final By enterAccountInfoHeading = By.xpath("//b[text()='Enter Account Information']");
    private final By titleMrRadio     = By.id("id_gender1");
    private final By titleMrsRadio    = By.id("id_gender2");
    private final By nameField        = By.id("name");
    private final By emailField       = By.id("email");
    private final By passwordField    = By.id("password");
    private final By dayDropdown      = By.id("days");
    private final By monthDropdown    = By.id("months");
    private final By yearDropdown     = By.id("years");
    private final By newsletterCheck  = By.id("optin");
    private final By offersCheck      = By.id("newsletter");

    // ── Address details (same form, scrolled down) ────────────────────────
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
    private final By createAccountBtn = By.cssSelector("[data-qa='create-account']");

    // ── Post-creation ─────────────────────────────────────────────────────
    private final By accountCreatedHeading = By.xpath("//b[text()='Account Created!']");
    private final By continueBtn           = By.cssSelector("[data-qa='continue-button']");

    // =====================================================================

    public LoginPage(ASM_Framework driver) { super(driver); }
    public LoginPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/login"); }

    // ── Heading visibility ────────────────────────────────────────────────

    public boolean isLoginHeadingVisible()
    {
        return driver.validateElementIsDisplayed(driver.findElement("xpath",
                "//h2[text()='Login to your account']"));
    }

    public boolean isSignupHeadingVisible()
    {
        return driver.validateElementIsDisplayed(driver.findElement("xpath",
                "//h2[text()='New User Signup!']"));
    }

    public boolean isEnterAccountInfoVisible()
    {
        return driver.validateElementIsDisplayed(driver.findElement("xpath",
                "//b[text()='Enter Account Information']"));
    }

    /**
     * Returns true if the 'ACCOUNT CREATED!' heading is visible on the page.
     *
     * <p><b>This method ONLY checks visibility — it never clicks anything.</b>
     * It is used as an assertion checkpoint in {@code registerAndContinue()}
     * BEFORE the caller decides to proceed with {@code clickContinueAfterCreation()}.
     * Never add any click or navigation call inside this method.</p>
     */
    public boolean isAccountCreatedVisible()
    {
        return driver.validateElementIsDisplayed(driver.findElement("xpath",
                "//b[text()='Account Created!']"));
    }

    // ── Login actions ─────────────────────────────────────────────────────

    public void loginWith(String email, String password)
    {
        driver.writeInElement(loginEmailField, email);
        driver.writeInElement(loginPasswordField, password);
        driver.clickElement(loginButton);
    }

    public String getLoginErrorText()
    {
        return driver.getElementText(loginErrorMsg);
    }

    public boolean isLoginErrorVisible()
    {
        try {
            return driver.validateElementIsDisplayed(driver.findElement("xpath",
                    "//p[text()='Your email or password is incorrect!']"));
        } catch (Exception e) { return false; }
    }

    // ── Signup step 1 ─────────────────────────────────────────────────────

    public void fillSignupForm(String name, String email)
    {
        driver.writeInElement(signupNameField, name);
        driver.writeInElement(signupEmailField, email);
        driver.clickElement(signupButton);
    }

    public String getSignupErrorText()
    {
        return driver.getElementText(signupErrorMsg);
    }

    public boolean isSignupErrorVisible()
    {
        try {
            return driver.validateElementIsDisplayed(driver.findElement("xpath",
                    "//p[text()='Email Address already exist!']"));
        } catch (Exception e) { return false; }
    }

    // ── Signup step 2 — account information ──────────────────────────────

    public void fillAccountInformation(UserData u)
    {
        // Title radio
        if ("Mr".equalsIgnoreCase(u.title))
            driver.selectRadioButton(titleMrRadio);
        else
            driver.selectRadioButton(titleMrsRadio);

        // Password and DOB (name/email are pre-filled from step 1)
        driver.writeInElement(passwordField, u.password);
        driver.selectFromDropDownMenu(dayDropdown,   "visible", u.dayOfBirth);
        driver.selectFromDropDownMenu(monthDropdown, "visible", u.monthOfBirth);
        driver.selectFromDropDownMenu(yearDropdown,  "visible", u.yearOfBirth);

        // Checkboxes
        driver.checkCheckbox(newsletterCheck);
        driver.checkCheckbox(offersCheck);

        // Address
        driver.scrollToElement(firstNameField);
        driver.writeInElement(firstNameField,  u.firstName);
        driver.writeInElement(lastNameField,   u.lastName);
        driver.writeInElement(companyField,    u.company);
        driver.writeInElement(address1Field,   u.address1);
        driver.writeInElement(address2Field,   u.address2);
        driver.selectFromDropDownMenu(countryDropdown, "visible", u.country);
        driver.writeInElement(stateField,      u.state);
        driver.writeInElement(cityField,       u.city);
        driver.writeInElement(zipcodeField,    u.zipcode);
        driver.writeInElement(mobileField,     u.mobileNumber);

        driver.clickElement(createAccountBtn);
    }


    /**
     * Clicks the 'Continue' button on the 'ACCOUNT CREATED!' page and waits
     * for the logged-in navbar label to confirm the redirect completed.
     *
     * <p>Checks instantly (zero blocking wait) whether an ad overlay is present:
     * <ul>
     *   <li><b>Ad found</b> — dismisses it, scrolls to Continue (which may have
     *       shifted under the ad), then clicks it.</li>
     *   <li><b>No ad</b> — clicks Continue directly, no scrolling or waiting needed.</li>
     * </ul>
     * Either way, waits up to 15 s for the logged-in navbar label to confirm
     * the home-page redirect completed before returning.</p>
     */
    public void clickContinueAfterCreation()
    {
        if (dismissAdIfPresent())
        {
            // Ad was present and dismissed — scroll to make sure Continue
            // is visible now that the overlay is gone, then click.
            driver.scrollToElement(continueBtn);
            driver.clickElement(continueBtn);
        }
        else
        {
            // No ad — Continue button is fully visible and clickable right now.
            driver.clickElement(continueBtn);
        }

        // Wait for the logged-in navbar label — confirms the redirect to home
        // completed. getLoggedInUsername() is called immediately after this.
        driver.setExplicitWait(
                By.xpath("//a[contains(.,'Logged in as')]"), 15);
    }

}