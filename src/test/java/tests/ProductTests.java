package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import testdata.UserData;
import utils.DataReader;

import java.io.IOException;

/**
 * ProductTests — TC08, TC09, TC20, TC21.
 */
@Epic("Products")
public class ProductTests extends BaseTest
{
    // =====================================================================
    // TC08 — Verify All Products and product detail page
    // =====================================================================
    @Test
    @Story("All Products")
    @Description("TC08 — Navigate to Products, verify list, open first product, verify all detail fields.")
    @Severity(SeverityLevel.NORMAL)
    public void TC08_verifyAllProductsAndDetailPage()
    {
        HomePage           home    = new HomePage(getDriver());
        ProductsPage       products = new ProductsPage(getDriver());
        ProductDetailsPage detail   = new ProductDetailsPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickProducts();
        Assert.assertTrue(products.isAllProductsVisible(),  "ALL PRODUCTS heading should be visible");
        Assert.assertTrue(products.isProductsListVisible(), "Products list should be visible");

        products.clickFirstViewProduct();
        Assert.assertTrue(detail.isProductDetailPageOpen(), "Product detail page should be open");

        Assert.assertTrue(detail.isProductNameVisible(),         "Product name should be visible");
        Assert.assertTrue(detail.isProductCategoryVisible(),     "Product category should be visible");
        Assert.assertTrue(detail.isProductPriceVisible(),        "Product price should be visible");
        Assert.assertTrue(detail.isProductAvailabilityVisible(), "Product availability should be visible");
        Assert.assertTrue(detail.isProductConditionVisible(),    "Product condition should be visible");
        Assert.assertTrue(detail.isProductBrandVisible(),        "Product brand should be visible");
    }

    // =====================================================================
    // TC09 — Search Product
    // =====================================================================
    @Test
    @Story("Search Product")
    @Description("TC09 — Search for 'Blue Top', verify SEARCHED PRODUCTS heading and at least one result.")
    @Severity(SeverityLevel.NORMAL)
    public void TC09_searchProduct()
    {
        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickProducts();
        Assert.assertTrue(products.isAllProductsVisible(), "ALL PRODUCTS heading should be visible");

        products.searchProduct("Blue Top");
        Assert.assertTrue(products.isSearchedProductsHeadingVisible(),
                "'SEARCHED PRODUCTS' heading should be visible");
        Assert.assertTrue(products.getSearchedProductCount() > 0,
                "At least one product should appear in search results");
    }

    // =====================================================================
    // TC20 — Search Products and Verify Cart After Login
    // =====================================================================
    @Test
    @Story("Search and Cart After Login")
    @Description("TC20 — Search, add to cart, login with existing user, verify cart still has products.")
    @Severity(SeverityLevel.NORMAL)
    public void TC20_searchProductsAndVerifyCartAfterLogin() throws IOException
    {
        UserData u = DataReader.read("existingUser.json", UserData.class);

        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());
        CartPage     cart     = new CartPage(getDriver());
        LoginPage    login    = new LoginPage(getDriver());

        home.open();
        home.clickProducts();
        Assert.assertTrue(products.isAllProductsVisible(), "ALL PRODUCTS heading should be visible");

        products.searchProduct("Blue Top");
        Assert.assertTrue(products.isSearchedProductsHeadingVisible(),
                "'SEARCHED PRODUCTS' heading should be visible");
        Assert.assertTrue(products.getSearchedProductCount() > 0,
                "Search results should be visible");

        // Add first found product then Continue Shopping
        products.hoverAndAddFirstProductToCartNoModal();

        home.clickCart();
        Assert.assertTrue(cart.hasItems(), "Cart should have items before login");

        // Login with existing user
        home.clickSignupLogin();
        Assert.assertTrue(login.isLoginHeadingVisible(), "'Login to your account' should be visible");
        login.loginWith(u.email, u.password);
        Assert.assertTrue(home.isLoggedIn(), "User should be logged in");

        // Re-check cart
        home.clickCart();
        Assert.assertTrue(cart.hasItems(), "Cart should still have items after login");
    }

    // =====================================================================
    // TC21 — Add review on product
    // =====================================================================
    @Test
    @Story("Product Review")
    @Description("TC21 — Open any product, submit a review, verify 'Thank you for your review.' success.")
    @Severity(SeverityLevel.MINOR)
    public void TC21_addReviewOnProduct()
    {
        HomePage           home     = new HomePage(getDriver());
        ProductsPage       products = new ProductsPage(getDriver());
        ProductDetailsPage detail   = new ProductDetailsPage(getDriver());

        home.open();
        home.clickProducts();
        Assert.assertTrue(products.isAllProductsVisible(), "ALL PRODUCTS heading should be visible");

        products.clickFirstViewProduct();
        Assert.assertTrue(detail.isProductDetailPageOpen(), "Product detail page should be open");

        Assert.assertTrue(detail.isWriteReviewVisible(),
                "'Write Your Review' section should be visible");

        detail.submitReview("abdallah", "review@test.com", "Great product! Highly recommended.");
        Assert.assertTrue(detail.isReviewSuccessVisible(),
                "Review success message should appear");
    }
}