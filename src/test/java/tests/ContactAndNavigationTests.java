package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactPage;
import pages.HomePage;
import testdata.ContactData;
import utils.DataReader;

import java.io.IOException;

/**
 * ContactAndNavigationTests — TC06 and TC07.
 */
@Epic("Contact and Navigation")
public class ContactAndNavigationTests extends BaseTest
{
    // =====================================================================
    // TC06 — Contact Us Form
    // =====================================================================
    @Test
    @Story("Contact Us Form")
    @Description("TC06 — Fill Contact Us form, upload file, submit, accept alert, verify success, click Home.")
    @Severity(SeverityLevel.NORMAL)
    public void TC06_submitContactUsForm() throws IOException
    {
        ContactData c = DataReader.read("contact.json", ContactData.class);

        HomePage    home    = new HomePage(getDriver());
        ContactPage contact = new ContactPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickContactUs();
        Assert.assertTrue(contact.isGetInTouchVisible(), "'GET IN TOUCH' should be visible");

        contact.fillContactForm(c.name, c.email, c.subject, c.message);
        contact.uploadFile(c.filePath);
        contact.clickSubmit();
        contact.acceptConfirmationAlert();

        String successText = contact.getSuccessMessageText();
        Assert.assertTrue(successText.contains("Success"),
                "Success message should appear. Got: " + successText);

        contact.clickHomeButton();
        Assert.assertTrue(home.isHomePageVisible(),
                "Should land on home page after clicking Home button");
    }

    // =====================================================================
    // TC07 — Verify Test Cases Page
    // =====================================================================
    @Test
    @Story("Navigation")
    @Description("TC07 — Click 'Test Cases' in navbar, verify URL contains 'test_cases'.")
    @Severity(SeverityLevel.MINOR)
    public void TC07_navigateToTestCasesPage()
    {
        HomePage home = new HomePage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickTestCases();
        Assert.assertTrue(home.urlContains("test_cases"),
                "URL should contain 'test_cases' after clicking Test Cases link");
    }
}
