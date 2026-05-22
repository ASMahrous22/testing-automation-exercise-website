package pages;

import org.openqa.selenium.By;
import testdata.PaymentData;
import utils.ASM_Framework;

/**
 * PaymentPage — Represents the payment step of the checkout flow.
 *
 * <p>Covers: TC14–TC16, TC24.</p>
 *
 * @author ASMahrous
 */
public class PaymentPage extends BasePage
{
    // ── Payment form ──────────────────────────────────────────────────────
    private final By nameOnCardField   = By.cssSelector("[data-qa='name-on-card']");
    private final By cardNumberField   = By.cssSelector("[data-qa='card-number']");
    private final By cvcField          = By.cssSelector("[data-qa='cvc']");
    private final By expiryMonthField  = By.cssSelector("[data-qa='expiry-month']");
    private final By expiryYearField   = By.cssSelector("[data-qa='expiry-year']");
    private final By payConfirmBtn     = By.cssSelector("[data-qa='pay-button']");

    // ── Order confirmation ────────────────────────────────────────────────
    private final By orderSuccessMsg   = By.cssSelector("[data-qa='order-placed']");

    // ── Invoice download ──────────────────────────────────────────────────
    private final By downloadInvoiceBtn = By.cssSelector(".btn.btn-default.check_out");
    private final By continueBtn        = By.cssSelector("[data-qa='continue-button']");

    // =====================================================================

    public PaymentPage(ASM_Framework driver) { super(driver); }
    public PaymentPage(String browserName)  { super(browserName); }

    public void fillPaymentDetails(PaymentData p)
    {
        driver.writeInElement(nameOnCardField,  p.nameOnCard);
        driver.writeInElement(cardNumberField,  p.cardNumber);
        driver.writeInElement(cvcField,         p.cvc);
        driver.writeInElement(expiryMonthField, p.expiryMonth);
        driver.writeInElement(expiryYearField,  p.expiryYear);
    }

    public void clickPayAndConfirm()
    {
        driver.clickElement(payConfirmBtn);
    }

    public String getOrderSuccessText()
    {
        return driver.getElementText(orderSuccessMsg);
    }

    public boolean isOrderPlacedSuccessfully()
    {
        try {
            return driver.validateElementIsDisplayed(
                    driver.findElement("css", "[data-qa='order-placed']"));
        } catch (Exception e) { return false; }
    }

    public void clickDownloadInvoice()
    {
        driver.clickElement(downloadInvoiceBtn);
    }

    public void clickContinue()
    {
        driver.clickElement(continueBtn);
    }
}