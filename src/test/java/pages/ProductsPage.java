package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.ASM_Framework;

import java.util.List;

/**
 * ProductsPage — /products page and product detail page.
 * Covers TC08, TC09, TC12, TC19, TC20, TC21.
 */
public class ProductsPage extends BasePage
{
    private final By allProductsHeading = By.xpath("//h2[text()='All Products']");
    private final By productsList       = By.cssSelector(".features_items .col-sm-4");
    private final By searchInput        = By.id("search_product");
    private final By searchButton       = By.id("submit_search");
    private final By searchedHeading    = By.xpath("//h2[text()='Searched Products']");

    // Add-to-cart hover buttons
    private final By firstAddToCart    = By.xpath("(//a[@data-product-id='1'][contains(@class,'add-to-cart')])[1]");
    private final By secondAddToCart   = By.xpath("(//a[@data-product-id='3'][contains(@class,'add-to-cart')])[1]");
    private final By continueShopping  = By.xpath("//button[text()='Continue Shopping']");
    private final By viewCartModal     = By.xpath("//u[text()='View Cart']");

    // Product detail
    private final By firstViewProduct  = By.xpath("(//a[@href='/product_details/1'][1]");
    private final By quantityField     = By.id("quantity");
    private final By addToCartBtn      = By.cssSelector("[data-qa='add-to-cart']");
    private final By viewCartLink      = By.xpath("//u[text()='View Cart']");

    // Product detail fields
    private final By productName         = By.xpath("//div[@class='product-information']//h2");
    private final By productCategory     = By.xpath("//div[@class='product-information']//p[contains(.,'Category')]");
    private final By productPrice        = By.xpath("//div[@class='product-information']//span");
    private final By productAvailability = By.xpath("//div[@class='product-information']//p[contains(.,'Availability')]");
    private final By productCondition    = By.xpath("//div[@class='product-information']//p[contains(.,'Condition')]");
    private final By productBrand        = By.xpath("//div[@class='product-information']//p[contains(.,'Brand')]");

    // Review
    private final By writeReviewHeading = By.xpath("//a[text()='Write Your Review']");
    private final By reviewName         = By.id("name");
    private final By reviewEmail        = By.id("email");
    private final By reviewText         = By.id("review");
    private final By reviewSubmitBtn    = By.id("button-review");
    private final By reviewSuccessMsg   = By.xpath("//*[contains(text(),'Thank you for your review')]");

    // Brands sidebar
    private final By brandsSidebar   = By.cssSelector(".brands_products");
    private final By firstBrandLink  = By.xpath("(//div[@class='brands-name']//a)[1]");
    private final By secondBrandLink = By.xpath("(//div[@class='brands-name']//a)[2]");
    private final By brandPageTitle  = By.cssSelector(".title.text-center");

    // =====================================================================

    public ProductsPage(ASM_Framework driver) { super(driver); }
    public ProductsPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/products"); }

    // ── All Products page ─────────────────────────────────────────────────

    public boolean isAllProductsVisible()
    {
        waitFor(allProductsHeading);
        return wd().findElement(allProductsHeading).isDisplayed();
    }

    public boolean isProductsListVisible()
    {
        return wd().findElements(productsList).size() > 0;
    }

    // ── Product detail ────────────────────────────────────────────────────

    public void clickFirstViewProduct()      { safeClick(firstViewProduct); }

    public boolean isProductDetailPageOpen()
    {
        return driver.getCurrentPageURL().contains("/product_details/");
    }

    private boolean isFieldVisible(By locator)
    {
        try { waitFor(locator); return wd().findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean isProductNameVisible()         { return isFieldVisible(productName); }
    public boolean isProductCategoryVisible()     { return isFieldVisible(productCategory); }
    public boolean isProductPriceVisible()        { return isFieldVisible(productPrice); }
    public boolean isProductAvailabilityVisible() { return isFieldVisible(productAvailability); }
    public boolean isProductConditionVisible()    { return isFieldVisible(productCondition); }
    public boolean isProductBrandVisible()        { return isFieldVisible(productBrand); }

    // ── Search ────────────────────────────────────────────────────────────

    public void searchProduct(String name)
    {
        killAds();
        driver.writeInElement(searchInput, name);
        safeClick(searchButton);
    }

    public boolean isSearchedProductsHeadingVisible()
    {
        waitFor(searchedHeading);
        return wd().findElement(searchedHeading).isDisplayed();
    }

    public int getSearchedProductCount()
    {
        return wd().findElements(productsList).size();
    }

    // ── TC12: hover add to cart ───────────────────────────────────────────

    public void hoverAndAddFirstProductToCart()
    {
        driver.hoverOverElement(firstAddToCart);
        safeClick(firstAddToCart);
    }

    public void hoverAndAddSecondProductToCart()
    {
        driver.hoverOverElement(secondAddToCart);
        safeClick(secondAddToCart);
    }

    public void clickContinueShopping()
    {
        waitFor(continueShopping);
        safeClick(continueShopping);
    }

    public void clickViewCartFromModal()
    {
        waitFor(viewCartModal);
        jsClick(viewCartModal);
    }

    // ── TC13: quantity ────────────────────────────────────────────────────

    public void setQuantity(String qty)
    {
        killAds();
        WebElement qtyField = wd().findElement(quantityField);
        qtyField.clear();
        qtyField.sendKeys(qty);
    }

    public void clickAddToCart()   { safeClick(addToCartBtn); }
    public void clickViewCart()    { jsClick(viewCartLink); }

    // ── TC20: add all searched products ──────────────────────────────────

    public void hoverAndAddFirstProductToCartNoModal()
    {
        driver.hoverOverElement(firstAddToCart);
        safeClick(firstAddToCart);
        try
        {
            waitFor(continueShopping);
            safeClick(continueShopping);
        }
        catch (Exception ignored) {}
    }

    // ── TC21: review ──────────────────────────────────────────────────────

    public boolean isWriteReviewVisible()
    {
        driver.scrollToElement(writeReviewHeading);
        waitFor(writeReviewHeading);
        return wd().findElement(writeReviewHeading).isDisplayed();
    }

    public void submitReview(String name, String email, String review)
    {
        killAds();
        driver.writeInElement(reviewName,  name);
        driver.writeInElement(reviewEmail, email);
        driver.writeInElement(reviewText,  review);
        safeClick(reviewSubmitBtn);
    }

    public boolean isReviewSuccessVisible()
    {
        try { waitFor(reviewSuccessMsg); return wd().findElement(reviewSuccessMsg).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // ── TC19: brands ─────────────────────────────────────────────────────

    public boolean isBrandsSidebarVisible()
    {
        return wd().findElements(brandsSidebar).size() > 0;
    }

    public void clickFirstBrand()  { safeClick(firstBrandLink); }
    public void clickSecondBrand() { safeClick(secondBrandLink); }

    public String getBrandPageTitle()
    {
        waitFor(brandPageTitle);
        return wd().findElement(brandPageTitle).getText();
    }

    public boolean isBrandPageProductsVisible()
    {
        return wd().findElements(productsList).size() > 0;
    }
}
