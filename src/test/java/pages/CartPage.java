package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.ASM_Framework;

import java.util.List;

/**
 * CartPage — /view_cart page. Covers TC11–TC13, TC17, TC22.
 */
public class CartPage extends BasePage
{
    private final By cartTable          = By.id("cart_info_table");
    private final By cartRows           = By.cssSelector("#cart_info_table tbody tr");
    private final By proceedToCheckout  = By.cssSelector(".btn.btn-default.check_out");
    private final By registerLoginLink  = By.xpath("//u[text()='Register / Login']");
    private final By subscriptionHeading= By.xpath("//h2[text()='Subscription']");
    private final By subscriptionEmail  = By.id("susbscribe_email");
    private final By subscriptionBtn    = By.id("subscribe");
    private final By subscriptionSuccess= By.id("success-subscribe");

    public CartPage(ASM_Framework driver) { super(driver); }
    public CartPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/view_cart"); }

    public boolean isCartPageVisible()
    {
        return driver.getCurrentPageURL().contains("view_cart");
    }

    public int getCartItemCount()
    {
        return wd().findElements(cartRows).size();
    }

    public boolean hasItems()
    {
        return wd().findElements(cartRows).size() > 0;
    }

    public String getFirstItemQuantity()
    {
        waitFor(By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity button"));
        return wd().findElement(
                        By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity button"))
                .getText().trim();
    }

    public void removeFirstProduct()
    {
        waitFor(By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity_delete"));
        jsClick(By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity_delete"));
    }

    public boolean isCartEmpty()
    {
        try
        {
            killAds();
            driver.setExplicitWait(By.id("empty_cart"), 5);
            return wd().findElement(By.id("empty_cart")).isDisplayed();
        }
        catch (Exception e)
        {
            return wd().findElements(cartRows).size() == 0;
        }
    }

    public void clickProceedToCheckout()
    {
        waitFor(proceedToCheckout);
        safeClick(proceedToCheckout);
    }

    public void clickRegisterLoginLink()
    {
        waitFor(registerLoginLink);
        jsClick(registerLoginLink);
    }

    // ── Subscription ─────────────────────────────────────────────────────

    public void scrollToFooter()    { driver.scrollToElement(subscriptionHeading); }

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
}
