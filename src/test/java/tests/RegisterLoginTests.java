package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import testdata.UserData;
import utils.DataReader;

import java.io.IOException;

import static utils.AllureHelper.saveScreenshot;

/**
 * RegisterLoginTests — Covers TC01 through TC05 (User Registration & Login)
 * plus two additional negative test cases.
 *
 * <p><b>Group label:</b> {@code registration-login}</p>
 *
 * <p>Email convention: {@code asm<4-digit-random>@test.com}<br>
 * Valid username: {@code abdallah}</p>
 *
 * <p><b>Email usage rules:</b></p>
 * <ul>
 *   <li>A unique email is generated ONCE per test and stored in {@code u.email}.</li>
 *   <li>The same {@code u.email} is reused for every step in that test
 *       (registration AND login) — never regenerated mid-test.</li>
 *   <li>TC03 uses a hardcoded wrong email on purpose — that IS the negative input.</li>
 *   <li>TC05 reads a pre-existing email from {@code existingUser.json} on purpose
 *       — that IS the duplicate-email negative input.</li>
 * </ul>
 *
 * @author ASMahrous
 */
@Epic("User Account Management")
public class RegisterLoginTests extends BaseTest
{
    // ── helpers ───────────────────────────────────────────────────────────

    /**
     * Generates a fresh unique email for registration: asm + 4-digit number + @test.com.
     * Call this ONCE at the top of a test and store the result — do not call it again
     * mid-test or you will get a different email at the login step.
     */
    private String uniqueEmail()
    {
        int suffix = 1000 + (int)(Math.random() * 9000);
        return "asm" + suffix + "@test.com";
    }

    /**
     * Shared registration helper used by TC02, TC04, and the checkout tests.
     *
     * <p>Starting from the home page (already open), this method:</p>
     * <ol>
     *   <li>Clicks 'Signup / Login'</li>
     *   <li>Fills the signup name + email form (step 1)</li>
     *   <li>Fills the full account information form (step 2)</li>
     *   <li>Dismisses the ad overlay on the 'Account Created!' page if present</li>
     *   <li>Clicks 'Continue' — the site then auto-logs the user in</li>
     * </ol>
     *
     * <p><b>Do NOT call {@code home.clickSignupLogin()} before this</b> — that
     * click is performed internally here and calling it again would navigate
     * to the wrong page.</p>
     *
     * @param home  the HomePage instance (already on automationexercise.com)
     * @param login the LoginPage instance
     * @param u     UserData with {@code u.email} already set to the unique email
     *              for this run — this value must NOT be regenerated after calling
     *              this method
     */
    static void registerAndContinue(HomePage home, LoginPage login, UserData u)
    {
        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(),
                "PRECONDITION: 'New User Signup!' heading must be visible");
        login.fillSignupForm(u.name, u.email);
        Assert.assertTrue(login.isEnterAccountInfoVisible(),
                "PRECONDITION: 'ENTER ACCOUNT INFORMATION' must appear after signup step 1");
        login.fillAccountInformation(u);
        Assert.assertTrue(login.isAccountCreatedVisible(),
                "PRECONDITION: 'ACCOUNT CREATED!' heading must be visible");
        login.clickContinueAfterCreation();
    }

    // =====================================================================
    // TC01 — Register User
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("TC01 — Register a brand-new user with a unique email, verify 'ACCOUNT CREATED!', "
            + "verify logged-in navbar, then delete the account.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC01_registerNewUser() throws IOException
    {
        // uniqueEmail() called ONCE — used ONLY for registration.
        // TC01 has NO separate login step at all; the site automatically logs the
        // user in after clicking 'Continue' on the 'Account Created!' page.
        // The email is never reused for a login call anywhere in this test.
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(),
                "'New User Signup!' should be visible");

        login.fillSignupForm(u.name, u.email);
        Assert.assertTrue(login.isEnterAccountInfoVisible(),
                "'ENTER ACCOUNT INFORMATION' should be visible");

        login.fillAccountInformation(u);
        Assert.assertTrue(login.isAccountCreatedVisible(),
                "'ACCOUNT CREATED!' should be visible");

        // Continue — site auto-logs in; no separate login step needed
        login.clickContinueAfterCreation();
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear in navbar");

        saveScreenshot("TC01_LoggedIn", getDriver());

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(),
                "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC02 — Login with correct credentials
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Login User")
    @Description("TC02 — Register with a unique email, log out, then log back in with "
            + "the SAME email and password to verify login works.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC02_loginWithValidCredentials() throws IOException
    {
        // uniqueEmail() is called ONCE here, at the top, and stored in u.email.
        // That single value is used for BOTH steps:
        //   Step 1 — register a new account with u.email
        //   Step 2 — log in with that exact same u.email
        // uniqueEmail() is NOT called again before the login step;
        // doing so would generate a different address and login would fail.
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        // Step 1 — Register (creates the account we will test login against).
        // registerAndContinue handles clickSignupLogin + both signup steps +
        // dismissing the ad overlay + clicking Continue internally.
        registerAndContinue(home, login, u);
        home.clickLogout();

        // Step 2 — Login using the SAME u.email that was just registered above.
        // Do NOT generate a new email here — u.email is already set and is the
        // only valid credential that exists for this run.
        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(),
                "'Login to your account' should be visible");

        login.loginWith(u.email, u.password);   // ← reuses the registration email, not a new one
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear after login");

        saveScreenshot("TC02_LoggedIn", getDriver());

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(),
                "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC03 — Login with incorrect credentials
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Login User")
    @Description("TC03 — Attempt login with a wrong email and wrong password; "
            + "verify the error message 'Your email or password is incorrect!'")
    @Severity(SeverityLevel.NORMAL)
    public void TC03_loginWithInvalidCredentials()
    {
        // No registration needed — we deliberately use credentials that do not
        // belong to any account. The hardcoded wrong email is intentional here;
        // this is a negative test, not a registration test.
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(),
                "'Login to your account' should be visible");

        login.loginWith("asm0000@test.com", "WrongPass@999");
        Assert.assertTrue(login.isLoginErrorVisible(),
                "'Your email or password is incorrect!' error should appear");
    }

    // =====================================================================
    // TC04 — Logout User
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Logout User")
    @Description("TC04 — Register with a unique email (auto-logs in), then click Logout "
            + "and verify the browser redirects to the login page.")
    @Severity(SeverityLevel.NORMAL)
    public void TC04_logoutUser() throws IOException
    {
        // uniqueEmail() called ONCE — used ONLY for registration.
        // TC04 has NO separate login step. After registration the site auto-logs
        // the user in, and we immediately test the logout from that session.
        // The email is never passed to loginWith() anywhere in this test.
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        // registerAndContinue handles clickSignupLogin + both signup steps +
        // ad dismiss + Continue internally — do NOT call home.clickSignupLogin()
        // before this; that would cause a double-click and break the flow.
        registerAndContinue(home, login, u);

        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "User should be logged in as abdallah before testing logout");

        home.clickLogout();
        Assert.assertTrue(login.urlContains("login"),
                "User should be redirected to /login after clicking Logout");
    }

    // =====================================================================
    // TC05 — Register with existing email
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("TC05 — Attempt signup using the email from existingUser.json (already registered); "
            + "verify error 'Email Address already exist!'")
    @Severity(SeverityLevel.NORMAL)
    public void TC05_registerWithExistingEmail() throws IOException
    {
        // No uniqueEmail() here — we intentionally use the pre-existing email
        // from existingUser.json to trigger the duplicate-email error.
        UserData existing = DataReader.read("existingUser.json", UserData.class);

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(),
                "'New User Signup!' should be visible");

        login.fillSignupForm(existing.name, existing.email);
        Assert.assertTrue(login.isSignupErrorVisible(),
                "'Email Address already exist!' error should appear");
    }

    // =====================================================================
    // NEGATIVE TC-N01 — Login with empty email and password
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Login User")
    @Description("NEGATIVE TC-N01 — Submit login form with both fields empty; "
            + "HTML5 validation or server error must prevent login.")
    @Severity(SeverityLevel.MINOR)
    public void NEGATIVE_TC_N01_loginWithEmptyCredentials()
    {
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(),
                "'Login to your account' should be visible");

        login.loginWith("", "");
        Assert.assertFalse(home.isLoggedIn(),
                "User must NOT be logged in when both email and password are empty");
    }

    // =====================================================================
    // NEGATIVE TC-N02 — Register with invalid email format
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("NEGATIVE TC-N02 — Attempt signup with a malformed email (no @ or domain); "
            + "HTML5 validation should block the form and keep the user on /login.")
    @Severity(SeverityLevel.MINOR)
    public void NEGATIVE_TC_N02_registerWithInvalidEmailFormat()
    {
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(),
                "'New User Signup!' should be visible");

        // Malformed email — no @ symbol, no domain
        login.fillSignupForm("abdallah", "notAnEmail");

        // HTML5 email validation must block the form; page must stay on /login
        Assert.assertTrue(login.urlContains("login"),
                "Page should stay on /login when signup email format is invalid");
    }
}