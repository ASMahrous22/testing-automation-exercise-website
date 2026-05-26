package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductsPage;

/**
 * CategoryBrandTests — TC18 and TC19.
 */
@Epic("Categories and Brands")
public class CategoryBrandTests extends BaseTest
{
    // =====================================================================
    // TC18 — View Category Products
    // =====================================================================
    @Test
    @Story("Categories")
    @Description("TC18 — Verify sidebar, click Women > Dress, verify heading, then Men > Tshirts.")
    @Severity(SeverityLevel.NORMAL)
    public void TC18_viewCategoryProducts()
    {
        HomePage home = new HomePage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");
        Assert.assertTrue(home.isCategorySidebarVisible(), "Category sidebar should be visible");

        home.clickWomenCategory();
        home.clickWomenDressLink();

        String womenHeading = home.getCategoryPageHeading();
        Assert.assertTrue(
                womenHeading.contains("DRESS") || womenHeading.contains("WOMEN"),
                "Women/Dress category heading should be visible. Got: " + womenHeading);

        home.clickMenCategory();
        home.clickMenTshirtLink();

        String menHeading = home.getCategoryPageHeading();
        Assert.assertTrue(
                menHeading.contains("TSHIRT") || menHeading.contains("MEN"),
                "Men/Tshirt category heading should be visible. Got: " + menHeading);
    }

    // =====================================================================
    // TC19 — View & Cart Brand Products
    // =====================================================================
    @Test
    @Story("Brands")
    @Description("TC19 — Navigate to Products, verify brands sidebar, click first brand, verify products, click second brand.")
    @Severity(SeverityLevel.NORMAL)
    public void TC19_viewBrandProducts()
    {
        HomePage     home     = new HomePage(getDriver());
        ProductsPage products = new ProductsPage(getDriver());

        home.open();
        Assert.assertTrue(home.isHomePageVisible(), "Home page should be visible");

        home.clickProducts();
        Assert.assertTrue(products.isAllProductsVisible(),   "ALL PRODUCTS heading should be visible");
        Assert.assertTrue(products.isBrandsSidebarVisible(), "Brands sidebar should be visible");

        products.clickFirstBrand();
        Assert.assertFalse(products.getBrandPageTitle().isEmpty(),
                "First brand page title should not be empty");
        Assert.assertTrue(products.isBrandPageProductsVisible(),
                "First brand products should be displayed");

        products.clickSecondBrand();
        Assert.assertFalse(products.getBrandPageTitle().isEmpty(),
                "Second brand page title should not be empty");
        Assert.assertTrue(products.isBrandPageProductsVisible(),
                "Second brand products should be displayed");
    }
}
