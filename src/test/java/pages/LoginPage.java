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

    // ── Ad overlay (appears on the 'Account Created!' page) ───────────────
    // The ad renders as a full-viewport <iframe title="Advertisement"> that
    // sits on top of every element on the page and intercepts all clicks.
    // The dismiss button (div#dismiss-button-element div) lives INSIDE the
    // iframe — we must switch into the iframe context to reach it.
    // NOTE: iframe IDs (aswift_1, aswift_2 …) change every page load.
    //       We locate the iframe by its stable title="Advertisement" attribute.
    private final By adIframe     = By.cssSelector("iframe[title='Advertisement']");
    private final By adDismissBtn = By.cssSelector("div#dismiss-button-element div");

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

    // ── Ad dismiss ────────────────────────────────────────────────────────

    /**
     * Dismisses the Google ad overlay that appears on the 'Account Created!' page.
     *
     * <h3>Why this is needed</h3>
     * <p>After clicking 'Create Account' the site injects a full-viewport ad as
     * {@code <iframe title="Advertisement" style="width:100vw; height:100vh">}.
     * This iframe sits on top of the page at z-index and intercepts every click,
     * which is exactly what causes the {@code ElementClickInterceptedException}
     * on the 'Continue' button.</p>
     *
     * <h3>Why we need to switch into the iframe</h3>
     * <p>The dismiss button ({@code div#dismiss-button-element div}) lives
     * <em>inside</em> the ad iframe. Selenium's DOM is scoped to the current
     * frame context — calling {@code findElement} or {@code clickElement} from
     * the main page context will never find elements inside a child iframe.</p>
     *
     * <h3>Why we use JavaScript click inside the iframe</h3>
     * <p>After switching into the iframe, the ad content is served from a
     * Google domain. Selenium's {@code waitForElementToBeClickable} checks
     * CSS visibility ({@code display}, {@code visibility}, {@code opacity})
     * on the element — but inside a cross-origin ad iframe those checks can
     * time out even when the dismiss button is visually rendered. Using
     * {@code JavascriptExecutor.executeScript("arguments[0].click()")} bypasses
     * the visibility precondition and fires the click directly on the DOM node,
     * which is reliable regardless of the ad's CSS state.</p>
     *
     * <h3>Flow</h3>
     * <ol>
     *   <li>Probe for the ad iframe with a 5 s timeout (fast fail when no ad)</li>
     *   <li>Switch into the iframe via {@code FrameManager.switchToIFrame(By)}</li>
     *   <li>Find the dismiss button inside the iframe</li>
     *   <li>Fire a JavaScript click on it</li>
     *   <li>{@code finally} — always switch back to default content so all
     *       subsequent driver calls work on the main page</li>
     * </ol>
     */
    public void dismissAdIfPresent()
    {
        try
        {
            // Step 1 — probe for the ad iframe from the main page context.
            // Short 5 s timeout: if no ad loads within 5 s we skip dismiss entirely.
            driver.findElement("css", "iframe[title='Advertisement']", 20);

            // Step 2 — switch the WebDriver context INTO the ad iframe.
            // Uses FrameManager.switchToIFrame(By) from the framework.
            driver.switchToIFrame(adIframe);

            // Step 3 — find the dismiss button inside the iframe and JS-click it.
            // We use JavascriptExecutor.click() instead of driver.clickElement()
            // because clickElement() calls waitForElementToBeClickable() which
            // checks CSS visibility — unreliable inside a cross-origin ad iframe.
            org.openqa.selenium.WebElement dismissBtn =
                    driver.findElement("css", "div#dismiss-button-element div");
            ((JavascriptExecutor) driver.getDriver())
                    .executeScript("arguments[0].click();", dismissBtn);
        }
        catch (Exception ignored)
        {
            // Ad iframe not present, already dismissed, or blocked by ad-blocker.
            // Silently continue — the Continue button will be freely clickable.
        }
        finally
        {
            // Step 4 — ALWAYS return to default content.
            // If we leave the context inside the iframe every subsequent
            // driver call will throw NoSuchElementException on main-page elements.
            driver.switchToDefaultContent();
        }
    }

    /**
     * Dismisses any ad overlay first, then clicks the 'Continue' button on the
     * 'Account Created!' confirmation page.
     *
     * <p>Always call this instead of clicking 'Continue' directly — the ad
     * iframe covers 100vw × 100vh and will intercept the click if it has not
     * been dismissed first.</p>
     */
    public void clickContinueAfterCreation()
    {
        dismissAdIfPresent();
        driver.clickElement(continueBtn);
    }
}