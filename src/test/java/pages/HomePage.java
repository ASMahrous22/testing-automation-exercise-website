package pages;

import org.openqa.selenium.By;
import utils.ASM_Framework;

/**
 * HomePage — Represents the AutomationExercise home page.
 *
 * <p>Covers: home-page visibility checks, navigation bar links,
 * footer subscription widget, recommended items section, and
 * scroll-up arrow button (TC10, TC22, TC25, TC26).</p>
 *
 * @author ASMahrous
 */
public class HomePage extends BasePage
{
    // ── Navigation bar ────────────────────────────────────────────────────
    private final By signupLoginLink  = By.cssSelector("a[href='/login']");
    private final By logoutLink       = By.cssSelector("a[href='/logout']");
    private final By deleteAccountLink= By.cssSelector("a[href='/delete_account']");
    private final By productsLink     = By.cssSelector("a[href='/products']");
    private final By cartLink         = By.cssSelector("a[href='/view_cart']");
    private final By testCasesLink    = By.cssSelector("a[href='/test_cases']");
    private final By contactUsLink    = By.cssSelector("a[href='/contact_us']");
    private final By loggedInAsLabel  = By.cssSelector("a[href='/account_info'] b");

    // ── Home page hero ────────────────────────────────────────────────────
    private final By heroText         = By.cssSelector(".active .item h2");

    // ── Footer subscription ───────────────────────────────────────────────
    private final By subscriptionTitle   = By.id("susbscribe_email");
    private final By subscriptionHeading = By.xpath("//h2[text()='Subscription']");
    private final By subscriptionEmail   = By.id("susbscribe_email");
    private final By subscriptionBtn     = By.id("subscribe");
    private final By subscriptionSuccess = By.id("success-subscribe");

    // ── Recommended items ─────────────────────────────────────────────────
    private final By recommendedSection   = By.xpath("//h2[text()='recommended items']");
    private final By recommendedAddToCart = By.xpath(
            "(//div[@class='recommended_items']//a[contains(@class,'add-to-cart')])[1]");

    // ── Scroll-up arrow ───────────────────────────────────────────────────
    private final By scrollUpArrow = By.id("scrollUp");

    // ── Category sidebar ──────────────────────────────────────────────────
    private final By categorySidebar        = By.cssSelector(".left-sidebar");
    private final By womenCategoryLink      = By.xpath("//a[@href='#Women']");
    private final By womenDressLink         = By.xpath(
            "//div[@id='Women']//a[contains(@href,'dress')]");
    private final By menCategoryLink        = By.xpath("//a[@href='#Men']");
    private final By menTshirtLink          = By.xpath(
            "//div[@id='Men']//a[contains(@href,'tshirts')]");
    private final By categoryPageHeading    = By.cssSelector(".title.text-center");

    // ── Product quick-view on homepage ────────────────────────────────────
    private final By firstViewProductLink   = By.xpath(
            "(//div[@class='productinfo text-center']//a[@href])[1]");

    // ── Account deleted ───────────────────────────────────────────────────
    private final By accountDeletedHeading  = By.xpath("//h2[@data-qa='account-deleted']");
    private final By continueBtn            = By.xpath("//a[@data-qa='continue-button']");

    // =====================================================================

    public HomePage(ASM_Framework driver) { super(driver); }
    public HomePage(String browserName)  { super(browserName); }

    // ── Navigation ────────────────────────────────────────────────────────

    public void open()
    {
        driver.goToURL("https://automationexercise.com");
    }

    public boolean isHomePageVisible()
    {
        return driver.getCurrentPageURL().contains("automationexercise.com");
    }

    public void clickSignupLogin()      { driver.clickElement(signupLoginLink); }
    public void clickLogout()           { driver.clickElement(logoutLink); }
    public void clickDeleteAccount()    { driver.clickElement(deleteAccountLink); }
    public void clickProducts()         { driver.clickElement(productsLink); }
    public void clickCart()             { driver.clickElement(cartLink); }
    public void clickTestCases()        { driver.clickElement(testCasesLink); }
    public void clickContactUs()        { driver.clickElement(contactUsLink); }

    public String getLoggedInUsername()
    {
        return driver.getElementText(loggedInAsLabel);
    }

    public boolean isLoggedIn()
    {
        try { return driver.findElement("css", "a[href='/account_info'] b") != null; }
        catch (Exception e) { return false; }
    }

    // ── Account Deleted confirmation ──────────────────────────────────────

    public boolean isAccountDeletedVisible()
    {
        return driver.getElementText(accountDeletedHeading).equalsIgnoreCase("Account Deleted!");
    }

    public void clickContinueAfterDeletion() { driver.clickElement(continueBtn); }

    // ── Footer subscription ───────────────────────────────────────────────

    public void scrollToFooter()
    {
        driver.scrollToElement(subscriptionHeading);
    }

    public boolean isSubscriptionHeadingVisible()
    {
        return driver.getElementText(subscriptionHeading).toUpperCase().contains("SUBSCRIPTION");
    }

    public void subscribeWithEmail(String email)
    {
        driver.writeInElement(subscriptionEmail, email);
        driver.clickElement(subscriptionBtn);
    }

    public String getSubscriptionSuccessText()
    {
        return driver.getElementText(subscriptionSuccess);
    }

    // ── Recommended items ─────────────────────────────────────────────────

    public void scrollToRecommendedItems()
    {
        driver.scrollToElement(recommendedSection);
    }

    public boolean isRecommendedSectionVisible()
    {
        return driver.validateElementIsDisplayed(
                driver.findElement("xpath", "//h2[text()='recommended items']"));
    }

    public void addFirstRecommendedItemToCart()
    {
        driver.clickElement(recommendedAddToCart);
    }

    // ── Scroll-up arrow ───────────────────────────────────────────────────

    public void scrollToBottom()
    {
        driver.scrollToElement(subscriptionHeading);
    }

    public void clickScrollUpArrow()
    {
        driver.clickElement(scrollUpArrow);
    }

    public void scrollUpPage()
    {
        driver.scrollToElement(heroText);
    }

    public boolean isHeroTextVisible()
    {
        return driver.getElementText(heroText)
                .contains("Full-Fledged practice website for Automation Engineers");
    }

    // ── Category sidebar ──────────────────────────────────────────────────

    public boolean isCategorySidebarVisible()
    {
        return driver.validateElementIsDisplayed(
                driver.findElement("css", ".left-sidebar"));
    }

    public void clickWomenCategory()    { driver.clickElement(womenCategoryLink); }
    public void clickWomenDressLink()   { driver.clickElement(womenDressLink); }
    public void clickMenCategory()      { driver.clickElement(menCategoryLink); }
    public void clickMenTshirtLink()    { driver.clickElement(menTshirtLink); }

    public String getCategoryPageHeading()
    {
        return driver.getElementText(categoryPageHeading).toUpperCase();
    }

    // ── First product on home page ────────────────────────────────────────

    public void clickFirstViewProduct()
    {
        driver.clickElement(firstViewProductLink);
    }
}