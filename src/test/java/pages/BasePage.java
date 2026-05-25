package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.AdsHelper;
import utils.ASM_Framework;
import utils.AllureHelper;

import java.io.IOException;

/**
 * BasePage — Parent for all Page Object classes.
 *
 * Every interaction goes through AdsHelper so ads are killed before
 * any click or assertion, matching the logic of the proven RegisterUserTest.
 */
public class BasePage
{
    public ASM_Framework driver;

    public BasePage(ASM_Framework driver) { this.driver = driver; }
    public BasePage(String browserName)   { this.driver = new ASM_Framework(browserName); }

    // ── Convenience accessors ─────────────────────────────────────────────

    protected WebDriver wd()              { return driver.getDriver(); }

    // ── Ad-safe primitives used by every page subclass ────────────────────

    /** Kill ads + JS click — use for ALL navigation / navbar links. */
    protected void jsClick(By locator)    { AdsHelper.jsClick(wd(), locator); }

    /** Kill ads + JS click on a WebElement directly. */
    protected void jsClick(WebElement el) { AdsHelper.jsClick(wd(), el); }

    /**
     * Kill ads + regular click with retry, JS click as last resort.
     * Use for form buttons that are inside the page body (not nav links).
     */
    protected void safeClick(By locator)  { AdsHelper.killAdsAndClick(wd(), locator); }

    /** Kill ads then wait for element — use before every assertion. */
    protected void waitFor(By locator)    { AdsHelper.waitForElement(wd(), locator); }

    /** Kill ads, wait, and return the element. */
    protected WebElement waitAndGet(By locator) { return AdsHelper.waitForElementAndGet(wd(), locator); }

    /** Kill ads only — call before writing into a field. */
    protected void killAds()             { AdsHelper.killAds(wd()); }

    // ── URL / title helpers ───────────────────────────────────────────────

    public String  readPageURL()               { return driver.getCurrentPageURL(); }
    public boolean urlContains(String fragment){ return driver.getCurrentPageURL().contains(fragment); }
    public boolean titleContains(String text)  { return driver.getCurrentPageTitle().contains(text); }
    public void    closePage()                 { driver.closeCurrentTab(); }

    // ── Screenshot ────────────────────────────────────────────────────────

    public void saveScreenshot(String fileName, ASM_Framework driver) throws IOException
    {
        AllureHelper.saveScreenshot(fileName, driver);
    }
}
