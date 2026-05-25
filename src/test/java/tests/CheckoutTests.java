package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import testdata.PaymentData;
import testdata.UserData;
import utils.DataReader;

import java.io.IOException;

/**
 * CheckoutTests — TC14, TC15, TC16, TC23, TC24.
 */
@Epic("Checkout and Orders")
public class CheckoutTests extends BaseTest
{
    private String uniqueEmail()
    {
        return "asm" + System.currentTimeMillis() + "@test.com";
    }

    /**
     * Navigates to the first product's detail page and adds it to cart,
     * then dismisses the Continue Shopping modal so the caller can navigate freely.
     */
    private void addFirstProductToCart(HomePage home, ProductsPage products)
    {
        home.clickFirstViewProduct();
        products.clickAddToCart();
        try { products.clickContinueShopping(); } catch (Exception ignored) {}
    }

    // =====================================================================
    // TC14 — Place Order: Register while Checkout
    // =====================================================================
    @Test
    @Story("Place Order")
    @Description("TC14 — Add product, proceed to checkout as guest, register during checkout, complete order.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC14_placeOrderRegisterDuringCheckout() throws IOException
    {
        UserData    u = DataReader.read("user.json", UserData.class);
        u.email       = uniqueEmail();
        u.name        = "abdallah";
        PaymentData p = DataReader.read("payment.json", PaymentData.class);

        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());
        CheckoutPage checkout = new CheckoutPage(getDriver());
        PaymentPage  payment  = new PaymentPage(getDriver());
        LoginPage    login    = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        addFirstProductToCart(home, products);
        home.clickCart();
        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");

        cart.clickProceedToCheckout();
        cart.clickRegisterLoginLink();

        RegisterLoginTests.registerAndContinue(home, login, u);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear");

        home.clickCart();
        cart.clickProceedToCheckout();

        Assert.assertTrue(checkout.isAddressDetailsVisible(), "Address details should be visible");
        Assert.assertTrue(checkout.isReviewOrderVisible(),    "Order review should be visible");

        checkout.enterOrderComment("Automated test order TC14");
        checkout.clickPlaceOrder();

        payment.fillPaymentDetails(p);
        payment.clickPayAndConfirm();

        Assert.assertTrue(payment.isOrderPlacedSuccessfully(),
                "'Your order has been placed successfully!' should appear");

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC15 — Place Order: Register before Checkout
    // =====================================================================
    @Test
    @Story("Place Order")
    @Description("TC15 — Register first, add product, proceed to checkout, complete order.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC15_placeOrderRegisterBeforeCheckout() throws IOException
    {
        UserData    u = DataReader.read("user.json", UserData.class);
        u.email       = uniqueEmail();
        u.name        = "abdallah";
        PaymentData p = DataReader.read("payment.json", PaymentData.class);

        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());
        CheckoutPage checkout = new CheckoutPage(getDriver());
        PaymentPage  payment  = new PaymentPage(getDriver());
        LoginPage    login    = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        RegisterLoginTests.registerAndContinue(home, login, u);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear");

        addFirstProductToCart(home, products);
        home.clickCart();
        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");

        cart.clickProceedToCheckout();
        Assert.assertTrue(checkout.isAddressDetailsVisible(), "Address details should be visible");
        Assert.assertTrue(checkout.isReviewOrderVisible(),    "Order review should be visible");

        checkout.enterOrderComment("Automated test order TC15");
        checkout.clickPlaceOrder();

        payment.fillPaymentDetails(p);
        payment.clickPayAndConfirm();

        Assert.assertTrue(payment.isOrderPlacedSuccessfully(),
                "'Your order has been placed successfully!' should appear");

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC16 — Place Order: Login before Checkout
    // =====================================================================
    @Test
    @Story("Place Order")
    @Description("TC16 — Login with a fresh account, add product, checkout, complete order.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC16_placeOrderLoginBeforeCheckout() throws IOException
    {
        // Register a fresh user so the test is fully self-contained
        UserData    u = DataReader.read("user.json", UserData.class);
        u.email       = uniqueEmail();
        u.name        = "abdallah";
        PaymentData p = DataReader.read("payment.json", PaymentData.class);

        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());
        CheckoutPage checkout = new CheckoutPage(getDriver());
        PaymentPage  payment  = new PaymentPage(getDriver());
        LoginPage    login    = new LoginPage(getDriver());

        home.open();
        RegisterLoginTests.registerAndContinue(home, login, u);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear");

        addFirstProductToCart(home, products);
        home.clickCart();
        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");

        cart.clickProceedToCheckout();
        Assert.assertTrue(checkout.isAddressDetailsVisible(), "Address details should be visible");
        Assert.assertTrue(checkout.isReviewOrderVisible(),    "Order review should be visible");

        checkout.enterOrderComment("Automated test order TC16");
        checkout.clickPlaceOrder();

        payment.fillPaymentDetails(p);
        payment.clickPayAndConfirm();

        Assert.assertTrue(payment.isOrderPlacedSuccessfully(),
                "'Your order has been placed successfully!' should appear");

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC23 — Verify address details in checkout page
    // =====================================================================
    @Test
    @Story("Address Verification")
    @Description("TC23 — Register, add product, checkout, verify delivery address matches registration data.")
    @Severity(SeverityLevel.NORMAL)
    public void TC23_verifyAddressDetailsAtCheckout() throws IOException
    {
        UserData u = DataReader.read("user.json", UserData.class);
        u.email    = uniqueEmail();
        u.name     = "abdallah";

        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());
        CheckoutPage checkout = new CheckoutPage(getDriver());
        LoginPage    login    = new LoginPage(getDriver());

        home.open();
        RegisterLoginTests.registerAndContinue(home, login, u);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear");

        addFirstProductToCart(home, products);
        home.clickCart();
        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");

        cart.clickProceedToCheckout();
        Assert.assertTrue(checkout.isAddressDetailsVisible(), "Address details should be visible");

        String deliveryAddr = checkout.getDeliveryAddressText();
        Assert.assertTrue(deliveryAddr.contains(u.firstName),
                "Delivery address should contain first name. Got: " + deliveryAddr);
        Assert.assertTrue(deliveryAddr.contains(u.city),
                "Delivery address should contain city. Got: " + deliveryAddr);

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }

    // =====================================================================
    // TC24 — Download Invoice after purchase order
    // =====================================================================
    @Test
    @Story("Invoice Download")
    @Description("TC24 — Complete full order flow, click Download Invoice, continue, delete account.")
    @Severity(SeverityLevel.NORMAL)
    public void TC24_downloadInvoiceAfterOrder() throws IOException
    {
        UserData    u = DataReader.read("user.json", UserData.class);
        u.email       = uniqueEmail();
        u.name        = "abdallah";
        PaymentData p = DataReader.read("payment.json", PaymentData.class);

        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());
        CheckoutPage checkout = new CheckoutPage(getDriver());
        PaymentPage  payment  = new PaymentPage(getDriver());
        LoginPage    login    = new LoginPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        addFirstProductToCart(home, products);
        home.clickCart();
        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");

        cart.clickProceedToCheckout();
        cart.clickRegisterLoginLink();

        RegisterLoginTests.registerAndContinue(home, login, u);
        Assert.assertTrue(home.getLoggedInUsername().toLowerCase().contains("abdallah"),
                "'Logged in as abdallah' should appear");

        home.clickCart();
        cart.clickProceedToCheckout();

        checkout.enterOrderComment("TC24 invoice test");
        checkout.clickPlaceOrder();

        payment.fillPaymentDetails(p);
        payment.clickPayAndConfirm();

        Assert.assertTrue(payment.isOrderPlacedSuccessfully(),
                "'Your order has been placed successfully!' should appear");

        // Download invoice button click — wait for it to be ready first
        payment.clickDownloadInvoice();

        // Wait for the page to settle after download triggers, then continue
        payment.clickContinue();

        home.clickDeleteAccount();
        Assert.assertTrue(home.isAccountDeletedVisible(), "'ACCOUNT DELETED!' should be visible");
        home.clickContinueAfterDeletion();
    }
}
