package tests;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.AdsHelper;

/**
 * ScrollTests — TC25 and TC26.
 */
@Epic("UI Scroll Behaviour")
public class ScrollTests extends BaseTest
{
    // =====================================================================
    // TC25 — Verify Scroll Up using 'Arrow' button and Scroll Down
    // =====================================================================
    @Test
    @Story("Scroll Up Arrow")
    @Description("TC25 — Scroll to footer, verify SUBSCRIPTION, click scroll-up arrow, verify hero text.")
    @Severity(SeverityLevel.MINOR)
    public void TC25_scrollUpWithArrowButton()
    {
        HomePage home = new HomePage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.scrollToBottom();
        Assert.assertTrue(home.isSubscriptionHeadingVisible(),
                "'SUBSCRIPTION' text should be visible in footer");

        home.clickScrollUpArrow();

        Assert.assertTrue(home.isHeroTextVisible(),
                "'Full-Fledged practice website for Automation Engineers' should be visible after scroll up");
    }

    // =====================================================================
    // TC26 — Verify Scroll Up without 'Arrow' button and Scroll Down
    // =====================================================================
    @Test
    @Story("Scroll Up Without Arrow")
    @Description("TC26 — Scroll to footer, verify SUBSCRIPTION, JS scroll-to-top, verify hero text.")
    @Severity(SeverityLevel.MINOR)
    public void TC26_scrollUpWithoutArrowButton()
    {
        HomePage home = new HomePage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.scrollToBottom();
        Assert.assertTrue(home.isSubscriptionHeadingVisible(),
                "'SUBSCRIPTION' text should be visible in footer");

        // Scroll to top via JS — no arrow button
        home.scrollUpWithJS();

        Assert.assertTrue(home.isHeroTextVisible(),
                "'Full-Fledged practice website for Automation Engineers' should be visible after scroll up");
    }
}
