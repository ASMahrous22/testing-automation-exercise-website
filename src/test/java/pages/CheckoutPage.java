package pages;

import org.openqa.selenium.By;
import utils.ASM_Framework;

/**
 * CheckoutPage — /checkout page. Covers TC14–TC16, TC23–TC24.
 */
public class CheckoutPage extends BasePage
{
    private final By addressDelivery = By.id("address_delivery");
    private final By addressInvoice  = By.id("address_invoice");
    private final By commentField    = By.cssSelector("textarea[name='message']");
    private final By placeOrderBtn   = By.cssSelector(".btn.btn-default.check_out");
    private final By cartInfoTable   = By.id("cart_info");

    public CheckoutPage(ASM_Framework driver) { super(driver); }
    public CheckoutPage(String browserName)  { super(browserName); }

    public boolean isCheckoutPageVisible()
    {
        return driver.getCurrentPageURL().contains("checkout");
    }

    public boolean isAddressDetailsVisible()
    {
        try { waitFor(addressDelivery); return wd().findElement(addressDelivery).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean isReviewOrderVisible()
    {
        try { waitFor(cartInfoTable); return wd().findElement(cartInfoTable).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public String getDeliveryAddressText()
    {
        waitFor(addressDelivery);
        return wd().findElement(addressDelivery).getText();
    }

    public String getBillingAddressText()
    {
        waitFor(addressInvoice);
        return wd().findElement(addressInvoice).getText();
    }

    public void enterOrderComment(String comment)
    {
        killAds();
        driver.writeInElement(commentField, comment);
    }

    public void clickPlaceOrder()
    {
        waitFor(placeOrderBtn);
        safeClick(placeOrderBtn);
    }
}
