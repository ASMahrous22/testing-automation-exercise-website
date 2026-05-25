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
 * RegisterLoginTests — TC01–TC05 plus two negative cases.
 */
@Epic("User Account Management")
public class RegisterLoginTests extends BaseTest
{
    private String uniqueEmail()
    {
        return "asm" + System.currentTimeMillis() + "@test.com";
    }

    /**
     * Shared registration helper — called by TC02, TC04, CheckoutTests.
     * Handles clickSignupLogin → fillSignupForm → fillAccountInformation
     * → isAccountCreatedVisible assertion → clickContinueAfterCreation.
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
    @Description("TC01 — Register a brand-new user, verify 'ACCOUNT CREATED!', verify logged-in navbar, delete account.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC01_registerNewUser() throws IOException
    {
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(), "'New User Signup!' should be visible");

        login.fillSignupForm(u.name, u.email);
        Assert.assertTrue(login.isEnterAccountInfoVisible(), "'ENTER ACCOUNT INFORMATION' should be visible");

        login.fillAccountInformation(u);
        Assert.assertTrue(login.isAccountCreatedVisible(), "'ACCOUNT CREATED!' should be visible");

        login.clickContinueAfterCreation();
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear in navbar");

        saveScreenshot("TC01_LoggedIn", getDriver());

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC02 — Login with correct credentials
    // =====================================================================
    @Test(groups = "registration-login")
    @Story("Login User")
    @Description("TC02 — Register, log out, log back in with same credentials, delete account.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC02_loginWithValidCredentials() throws IOException
    {
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        registerAndContinue(home, login, u);
        home.clickLogout();

        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(), "'Login to your account' should be visible");

        login.loginWith(u.email, u.password);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear after login");

        saveScreenshot("TC02_LoggedIn", getDriver());

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC03 — Login with incorrect credentials
    // =====================================================================
    @Test(groups = "registration-login")
    @Story("Login User")
    @Description("TC03 — Attempt login with wrong credentials; verify error message.")
    @Severity(SeverityLevel.NORMAL)
    public void TC03_loginWithInvalidCredentials()
    {
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(), "'Login to your account' should be visible");

        login.loginWith("asm0000@test.com", "WrongPass@999");
        Assert.assertTrue(login.isLoginErrorVisible(),
                "'Your email or password is incorrect!' error should appear");
    }

    // =====================================================================
    // TC04 — Logout User
    // =====================================================================
    @Test(groups = "registration-login")
    @Story("Logout User")
    @Description("TC04 — Register (auto-logs in), click Logout, verify redirect to login page.")
    @Severity(SeverityLevel.NORMAL)
    public void TC04_logoutUser() throws IOException
    {
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        registerAndContinue(home, login, u);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "User should be logged in before testing logout");

        home.clickLogout();
        Assert.assertTrue(login.urlContains("login"),
                "User should be redirected to /login after clicking Logout");
    }

    // =====================================================================
    // TC05 — Register with existing email
    // =====================================================================
    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("TC05 — Attempt signup with pre-existing email; verify duplicate-email error.")
    @Severity(SeverityLevel.NORMAL)
    public void TC05_registerWithExistingEmail() throws IOException
    {
        UserData existing = DataReader.read("existingUser.json", UserData.class);

        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(), "'New User Signup!' should be visible");

        login.fillSignupForm(existing.name, existing.email);
        Assert.assertTrue(login.isSignupErrorVisible(),
                "'Email Address already exist!' error should appear");
    }

    // =====================================================================
    // NEGATIVE TC-N01 — Login with empty credentials
    // =====================================================================
    @Test(groups = "registration-login")
    @Story("Login User")
    @Description("NEGATIVE TC-N01 — Submit login form with empty fields; must not log in.")
    @Severity(SeverityLevel.MINOR)
    public void NEGATIVE_TC_N01_loginWithEmptyCredentials()
    {
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(), "'Login to your account' should be visible");

        login.loginWith("", "");
        Assert.assertFalse(home.isLoggedIn(),
                "User must NOT be logged in when both fields are empty");
    }

    // =====================================================================
    // NEGATIVE TC-N02 — Register with invalid email format
    // =====================================================================
    @Test(groups = "registration-login")
    @Story("Register User")
    @Description("NEGATIVE TC-N02 — Signup with malformed email; HTML5 validation should block.")
    @Severity(SeverityLevel.MINOR)
    public void NEGATIVE_TC_N02_registerWithInvalidEmailFormat()
    {
        HomePage  home  = new HomePage(getDriver());
        LoginPage login = new LoginPage(getDriver());

        home.open();
        home.clickSignupLogin();
        Assert.assertTrue(login.isSignupHeadingVisible(), "'New User Signup!' should be visible");

        login.fillSignupForm("abdallah", "notAnEmail");
        Assert.assertTrue(login.urlContains("login"),
                "Page should stay on /login when signup email format is invalid");
    }
}
