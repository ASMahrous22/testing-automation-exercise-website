package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import utils.ASM_Framework;

/**
 * HomePage — automationexercise.com home page.
 * All clicks go through jsClick() / safeClick() so ads never block them.
 */
public class HomePage extends BasePage
{
    // ── Navbar ────────────────────────────────────────────────────────────
    private final By signupLoginLink   = By.linkText("Signup / Login");
    private final By logoutLink        = By.linkText("Logout");
    private final By deleteAccountLink = By.linkText("Delete Account");
    private final By productsLink      = By.cssSelector("a[href='/products']");
    private final By cartLink          = By.cssSelector("a[href='/view_cart']");
    private final By testCasesLink     = By.cssSelector("a[href='/test_cases']");
    private final By contactUsLink     = By.cssSelector("a[href='/contact_us']");

    // ── Home-page visibility ──────────────────────────────────────────────
    private final By homeLogo          = By.cssSelector("img[alt='Website for automation practice']");

    // ── Logged-in label ───────────────────────────────────────────────────
    private final By loggedInLabel     = By.xpath("//a[contains(.,'Logged in as')]");

    // ── Account Deleted ───────────────────────────────────────────────────
    private final By accountDeletedHeading = By.xpath("//b[contains(text(),'Account Deleted!')]");
    private final By continueBtn           = By.cssSelector("a[data-qa='continue-button']");

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

    // ── Hero text ─────────────────────────────────────────────────────────
    private final By heroText = By.cssSelector(".active .item h2");

    // ── Category sidebar ──────────────────────────────────────────────────
    private final By categorySidebar   = By.cssSelector(".left-sidebar");
    private final By womenCategoryLink = By.xpath("//a[@href='#Women']");
    private final By womenDressLink    = By.xpath("//div[@id='Women']//a[contains(@href,'dress')]");
    private final By menCategoryLink   = By.xpath("//a[@href='#Men']");
    private final By menTshirtLink     = By.xpath("//div[@id='Men']//a[contains(@href,'tshirts')]");
    private final By categoryPageHeading = By.cssSelector(".title.text-center");

    // ── First product on home page ────────────────────────────────────────
    private final By firstViewProductLink = By.xpath(
            "(//div[@class='productinfo text-center']//a[@href])[1]");

    // =====================================================================

    public HomePage(ASM_Framework driver) { super(driver); }
    public HomePage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com"); }

    // ── Visibility ────────────────────────────────────────────────────────

    public boolean isHomePageVisible()
    {
        waitFor(homeLogo);
        return wd().findElement(homeLogo).isDisplayed();
    }

    // ── Navbar actions — always jsClick ───────────────────────────────────

    public void clickSignupLogin()   { jsClick(signupLoginLink); }
    public void clickLogout()        { jsClick(logoutLink); }
    public void clickDeleteAccount() { jsClick(deleteAccountLink); }
    public void clickProducts()      { jsClick(productsLink); }
    public void clickCart()          { jsClick(cartLink); }
    public void clickTestCases()     { jsClick(testCasesLink); }
    public void clickContactUs()     { jsClick(contactUsLink); }

    // ── Logged-in state ───────────────────────────────────────────────────

    public String getLoggedInUsername()
    {
        waitFor(loggedInLabel);
        return wd().findElement(By.xpath("//a[contains(.,'Logged in as')]/b"))
                .getText().trim();
    }

    public boolean isLoggedIn()
    {
        try
        {
            killAds();
            driver.setExplicitWait(loggedInLabel, 5);
            return wd().findElement(loggedInLabel).isDisplayed();
        }
        catch (Exception e) { return false; }
    }

    // ── Account Deleted ───────────────────────────────────────────────────

    public boolean isAccountDeletedVisible()
    {
        waitFor(accountDeletedHeading);
        return wd().findElement(accountDeletedHeading).isDisplayed();
    }

    public void clickContinueAfterDeletion()
    {
        jsClick(continueBtn);
    }

    // ── Footer subscription ───────────────────────────────────────────────

    public void scrollToFooter()
    {
        driver.scrollToElement(subscriptionHeading);
    }

    public boolean isSubscriptionHeadingVisible()
    {
        waitFor(subscriptionHeading);
        return wd().findElement(subscriptionHeading).getText().toUpperCase().contains("SUBSCRIPTION");
    }

    public void subscribeWithEmail(String email)
    {
        killAds();
        driver.writeInElement(subscriptionEmail, email);
        safeClick(subscriptionBtn);
    }

    public String getSubscriptionSuccessText()
    {
        waitFor(subscriptionSuccess);
        return wd().findElement(subscriptionSuccess).getText();
    }

    // ── Recommended items ─────────────────────────────────────────────────

    public void scrollToRecommendedItems() { driver.scrollToElement(recommendedSection); }

    public boolean isRecommendedSectionVisible()
    {
        waitFor(recommendedSection);
        return wd().findElement(recommendedSection).isDisplayed();
    }

    public void addFirstRecommendedItemToCart() { safeClick(recommendedAddToCart); }

    // ── Scroll-up arrow ───────────────────────────────────────────────────

    public void scrollToBottom()    { driver.scrollToElement(subscriptionHeading); }
    public void clickScrollUpArrow(){ jsClick(scrollUpArrow); }

    public void scrollUpWithJS()
    {
        ((JavascriptExecutor) wd()).executeScript("window.scrollTo({top: 0, behavior: 'smooth'});");
    }

    public boolean isHeroTextVisible()
    {
        waitFor(heroText);
        return wd().findElement(heroText).getText()
                .contains("Full-Fledged practice website for Automation Engineers");
    }

    // ── Category sidebar ──────────────────────────────────────────────────

    public boolean isCategorySidebarVisible()
    {
        waitFor(categorySidebar);
        return wd().findElement(categorySidebar).isDisplayed();
    }

    public void clickWomenCategory() { safeClick(womenCategoryLink); }
    public void clickWomenDressLink(){ safeClick(womenDressLink); }
    public void clickMenCategory()   { safeClick(menCategoryLink); }
    public void clickMenTshirtLink() { safeClick(menTshirtLink); }

    public String getCategoryPageHeading()
    {
        waitFor(categoryPageHeading);
        return wd().findElement(categoryPageHeading).getText().toUpperCase();
    }

    // ── First product on home page ────────────────────────────────────────

    public void clickFirstViewProduct() { safeClick(firstViewProductLink); }
}
