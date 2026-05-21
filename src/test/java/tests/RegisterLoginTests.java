package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import testdata.UserData;
import utils.DataReader;

import java.io.IOException;
import java.util.UUID;

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
 * @author ASMahrous
 */
@Epic("User Account Management")
public class RegisterLoginTests extends BaseTest
{
    // ── helpers ───────────────────────────────────────────────────────────

    /** Generates a unique email: asm + 4-digit number + @test.com */
    private String uniqueEmail()
    {
        int suffix = 1000 + (int)(Math.random() * 9000);
        return "asm" + suffix + "@test.com";
    }

    /**
     * Registers a fresh account using the given email, then returns to home.
     * Shared by TC14, TC15, TC23, TC24 via static helper visibility.
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
    @Description("TC01 — Register a brand-new user, verify account created, then delete the account.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC01_registerNewUser() throws IOException
    {
        UserData u    = DataReader.read("user.json", UserData.class);
        u.email       = uniqueEmail();   // always fresh
        u.name        = "abdallah";

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

        login.clickContinueAfterCreation();
        Assert.assertTrue(home.getLoggedInUsername().equalsIgnoreCase("abdallah"),
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
    @Description("TC02 — Login with valid email & password, verify logged-in state, then delete account.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC02_loginWithValidCredentials() throws IOException
    {
        // First register so we have a fresh account to delete after
        UserData u    = DataReader.read("user.json", UserData.class);
        u.email       = uniqueEmail();
        u.name        = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        // Register
        registerAndContinue(home, login, u);
        home.clickLogout();

        // Now login with the same credentials
        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(),
                "'Login to your account' should be visible");

        login.loginWith(u.email, u.password);
        Assert.assertTrue(home.getLoggedInUsername().equalsIgnoreCase("abdallah"),
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
    @Description("TC03 — Attempt login with wrong email & password; verify error message.")
    @Severity(SeverityLevel.NORMAL)
    public void TC03_loginWithInvalidCredentials()
    {
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
    @Description("TC04 — Login, then logout and verify redirect to login page.")
    @Severity(SeverityLevel.NORMAL)
    public void TC04_logoutUser() throws IOException
    {
        UserData u  = DataReader.read("user.json", UserData.class);
        u.email     = uniqueEmail();
        u.name      = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        home.clickSignupLogin();
        registerAndContinue(home, login, u);

        Assert.assertTrue(home.getLoggedInUsername().equalsIgnoreCase("abdallah"),
                "User should be logged in before logout test");

        home.clickLogout();
        Assert.assertTrue(login.urlContains("login"),
                "User should be redirected to login page after logout");
    }

    // =====================================================================
    // TC05 — Register with existing email
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("TC05 — Attempt signup with an already-registered email; verify error message.")
    @Severity(SeverityLevel.NORMAL)
    public void TC05_registerWithExistingEmail() throws IOException
    {
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
    @Description("NEGATIVE TC-N01 — Submit login form with both fields empty; error should be shown.")
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
        // Either an HTML5 validation prevents submit, or the server returns an error.
        // Either way we must NOT land on the home page.
        Assert.assertFalse(home.isLoggedIn(),
                "User must NOT be logged in when credentials are empty");
    }

    // =====================================================================
    // NEGATIVE TC-N02 — Register with invalid email format
    // =====================================================================

    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("NEGATIVE TC-N02 — Attempt signup with a malformed email address; form should reject it.")
    @Severity(SeverityLevel.MINOR)
    public void NEGATIVE_TC_N02_registerWithInvalidEmailFormat()
    {
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(),
                "'New User Signup!' should be visible");

        // Malformed email — no domain
        login.fillSignupForm("abdallah", "notAnEmail");

        // HTML5 validation should block the form; we must still be on /login
        Assert.assertTrue(login.urlContains("login"),
                "Page should stay on /login when signup email is invalid");
    }
}