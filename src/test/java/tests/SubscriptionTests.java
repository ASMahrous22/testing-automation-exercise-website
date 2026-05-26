package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;

/**
 * SubscriptionTests — TC10 and TC11.
 */
@Epic("Subscription")
public class SubscriptionTests extends BaseTest
{
    // =====================================================================
    // TC10 — Verify Subscription in home page
    // =====================================================================
    @Test
    @Story("Home Page Subscription")
    @Description("TC10 — Scroll to footer on home page, verify SUBSCRIPTION text, subscribe, verify success.")
    @Severity(SeverityLevel.NORMAL)
    public void TC10_subscriptionOnHomePage()
    {
        HomePage home = new HomePage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.scrollToFooter();
        Assert.assertTrue(home.isSubscriptionHeadingVisible(),
                "'SUBSCRIPTION' text should be visible in footer");

        home.subscribeWithEmail("testsubscription" + System.currentTimeMillis() + "@test.com");

        String successText = home.getSubscriptionSuccessText();
        Assert.assertTrue(successText.contains("You have been successfully subscribed"),
                "Subscription success message should appear. Got: " + successText);
    }

    // =====================================================================
    // TC11 — Verify Subscription in Cart page
    // =====================================================================
    @Test
    @Story("Cart Page Subscription")
    @Description("TC11 — Navigate to Cart, scroll to footer, verify SUBSCRIPTION, subscribe, verify success.")
    @Severity(SeverityLevel.NORMAL)
    public void TC11_subscriptionOnCartPage()
    {
        HomePage home = new HomePage(getDriver());
        CartPage cart = new CartPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickCart();
        cart.scrollToFooter();
        Assert.assertTrue(cart.isSubscriptionHeadingVisible(),
                "'SUBSCRIPTION' text should be visible on Cart page");

        cart.subscribeWithEmail("testsubscription" + System.currentTimeMillis() + "@test.com");

        String successText = cart.getSubscriptionSuccessText();
        Assert.assertTrue(successText.contains("You have been successfully subscribed"),
                "Subscription success message should appear. Got: " + successText);
    }
}
