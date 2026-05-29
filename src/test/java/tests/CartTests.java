package tests;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import utils.AdsHelper;

/**
 * CartTests — TC12, TC13, TC17, TC22.
 */
@Epic("Cart Management")
public class CartTests extends BaseTest
{
    // =====================================================================
    // TC12 — Add Products in Cart
    // =====================================================================
    @Test
    @Story("Add to Cart")
    @Description("TC12 — Hover and add first product, Continue Shopping, hover and add second, View Cart, verify both.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC12_addTwoProductsToCart()
    {
        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickProducts();
        Assert.assertTrue(products.isAllProductsVisible(), "ALL PRODUCTS heading should be visible");

        products.hoverAndAddFirstProductToCart();
        products.clickContinueShopping();

        products.hoverAndAddSecondProductToCart();
        products.clickViewCartFromModal();

        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");
        Assert.assertTrue(cart.getCartItemCount() >= 2,
                "Cart should contain at least 2 products. Count: " + cart.getCartItemCount());
    }

    // =====================================================================
    // TC13 — Verify Product quantity in Cart
    // =====================================================================
    @Test
    @Story("Cart Quantity")
    @Description("TC13 — Open product detail, set quantity to 4, add to cart, verify quantity is 4 in cart.")
    @Severity(SeverityLevel.NORMAL)
    public void TC13_verifyProductQuantityInCart()
    {
        HomePage           home     = new HomePage(getDriver());
        ProductDetailsPage detail   = new ProductDetailsPage(getDriver());
        CartPage           cart     = new CartPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickFirstViewProduct();
        Assert.assertTrue(detail.isProductDetailPageOpen(), "Product detail page should be open");

        detail.setQuantity("4");
        detail.clickAddToCart();
        detail.clickViewCart();

        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");
        String qty = cart.getFirstItemQuantity();
        Assert.assertEquals(qty, "4", "Product quantity in cart should be 4. Got: " + qty);
    }

    // =====================================================================
    // TC17 — Remove Products From Cart
    // =====================================================================
    @Test
    @Story("Remove from Cart")
    @Description("TC17 — Add a product to cart, open cart, remove it, verify cart is empty.")
    @Severity(SeverityLevel.NORMAL)
    public void TC17_removeProductFromCart()
    {
        HomePage           home   = new HomePage(getDriver());
        ProductDetailsPage detail = new ProductDetailsPage(getDriver());
        CartPage           cart   = new CartPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickFirstViewProduct();
        Assert.assertTrue(detail.isProductDetailPageOpen(), "Product detail page should be open");

        detail.clickAddToCart();
        detail.clickViewCart();

        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");

        cart.removeFirstProduct();

        // Wait for cart to update — use framework explicit wait on empty-cart indicator
        AdsHelper.waitForElement(getDriver().getDriver(),
                By.xpath("//b[text()='Cart is empty!'] | //*[@id='empty_cart']"));

        Assert.assertTrue(cart.isCartEmpty(), "Cart should be empty after removing the product");
    }

    // =====================================================================
    // TC22 — Add to cart from Recommended items
    // =====================================================================
    @Test
    @Story("Recommended Items")
    @Description("TC22 — Scroll to RECOMMENDED ITEMS, add first item to cart, verify in cart.")
    @Severity(SeverityLevel.NORMAL)
    public void TC22_addToCartFromRecommendedItems()
    {
        HomePage home = new HomePage(getDriver());
        CartPage cart = new CartPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.scrollToRecommendedItems();
        Assert.assertTrue(home.isRecommendedSectionVisible(),
                "'RECOMMENDED ITEMS' should be visible");

        home.addFirstRecommendedItemToCart();

        // Try to click "View Cart" from the modal; fall back to navbar cart link
        try
        {
            AdsHelper.waitForElement(getDriver().getDriver(), By.xpath("//u[text()='View Cart']"));
            AdsHelper.jsClick(getDriver().getDriver(), By.xpath("//u[text()='View Cart']"));
        }
        catch (Exception e)
        {
            home.clickCart();
        }

        Assert.assertTrue(cart.isCartPageVisible(), "Cart page should be visible");
        Assert.assertTrue(cart.hasItems(), "Cart should contain the recommended item");
    }
}