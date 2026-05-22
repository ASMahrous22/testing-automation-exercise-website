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
    private final By signupLoginLink   = By.cssSelector("a[href='/login']");
    private final By logoutLink        = By.cssSelector("a[href='/logout']");
    private final By deleteAccountLink = By.cssSelector("a[href='/delete_account']");
    private final By productsLink      = By.cssSelector("a[href='/products']");
    private final By cartLink          = By.cssSelector("a[href='/view_cart']");
    private final By testCasesLink     = By.cssSelector("a[href='/test_cases']");
    private final By contactUsLink     = By.cssSelector("a[href='/contact_us']");

    // ── Logged-in navbar label ─────────────────────────────────────────────
    // After login the site renders:
    //   <li><a href="/account_info"><b> Logged in as abdallah</b></a></li>
    // The text lives inside the <b> child — we target the <b> directly.
    // Two locators kept for resilience: CSS is the primary, XPath is the fallback.
    private final By loggedInLabel     = By.cssSelector("a[href='/account_info'] b");
    private final By loggedInLabelXP   = By.xpath("//a[@href='/account_info']/b");

    // ── Home page hero ────────────────────────────────────────────────────
    private final By heroText          = By.cssSelector(".active .item h2");

    // ── Footer subscription ───────────────────────────────────────────────
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
    private final By categorySidebar   = By.cssSelector(".left-sidebar");
    private final By womenCategoryLink = By.xpath("//a[@href='#Women']");
    private final By womenDressLink    = By.xpath("//div[@id='Women']//a[contains(@href,'dress')]");
    private final By menCategoryLink   = By.xpath("//a[@href='#Men']");
    private final By menTshirtLink     = By.xpath("//div[@id='Men']//a[contains(@href,'tshirts')]");
    private final By categoryPageHeading = By.cssSelector(".title.text-center");

    // ── Product quick-view on homepage ────────────────────────────────────
    private final By firstViewProductLink = By.xpath(
            "(//div[@class='productinfo text-center']//a[@href])[1]");

    // ── Account deleted ───────────────────────────────────────────────────
    private final By accountDeletedHeading = By.xpath("//h2[@data-qa='account-deleted']");
    private final By continueBtn           = By.xpath("//a[@data-qa='continue-button']");

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

    public void clickSignupLogin()   { driver.clickElement(signupLoginLink); }
    public void clickLogout()        { driver.clickElement(logoutLink); }
    public void clickDeleteAccount() { driver.clickElement(deleteAccountLink); }
    public void clickProducts()      { driver.clickElement(productsLink); }
    public void clickCart()          { driver.clickElement(cartLink); }
    public void clickTestCases()     { driver.clickElement(testCasesLink); }
    public void clickContactUs()     { driver.clickElement(contactUsLink); }

    // ── Logged-in state ───────────────────────────────────────────────────

    /**
     * Returns the username text from the logged-in navbar label.
     *
     * <p>The site renders {@code <a href="/account_info"><b> Logged in as NAME</b></a>}.
     * We target the {@code <b>} element directly and strip leading/trailing
     * whitespace. The returned string contains the full text including
     * "Logged in as ", so callers should use {@code contains()} or
     * {@code endsWith()} to check just the name.</p>
     *
     * <p>Uses a 20-second timeout to accommodate post-registration redirect
     * delays and any lingering ad overlays. Tries CSS first; falls back to
     * XPath if the CSS wait times out.</p>
     */
    public String getLoggedInUsername()
    {
        // 20s timeout — the page may still be navigating after ad dismiss + Continue
        try
        {
            return driver.findElement("xpath","//a[contains(.,'Logged in as')]/b", 10)
                    .getText()
                    .trim();
        }
        catch (Exception e)
        {
            // Fallback — identical element, different locator strategy
            return driver.findElement("xpath", "//a[contains(.,'Logged in as')]/b", 20)
                    .getText()
                    .trim();
        }
    }

    /**
     * Returns {@code true} if the logged-in navbar label is present in the DOM.
     * Uses a short 5-second timeout so the check is fast when the user is
     * NOT logged in.
     */
    public boolean isLoggedIn()
    {
        try
        {
            driver.findElement("xpath", "//a[contains(.,'Logged in as')]", 5);
            return true;
        }
        catch (Exception e) { return false; }
    }

    // ── Account Deleted confirmation ──────────────────────────────────────

    public boolean isAccountDeletedVisible()
    {
        return driver.getElementText(accountDeletedHeading)
                .equalsIgnoreCase("Account Deleted!");
    }

    public void clickContinueAfterDeletion()
    {
        // Ads often appear after clicking 'Delete Account'
        dismissAdIfPresent();
        driver.clickElement(continueBtn);
    }

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

    public void clickWomenCategory() { driver.clickElement(womenCategoryLink); }
    public void clickWomenDressLink(){ driver.clickElement(womenDressLink); }
    public void clickMenCategory()   { driver.clickElement(menCategoryLink); }
    public void clickMenTshirtLink() { driver.clickElement(menTshirtLink); }

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