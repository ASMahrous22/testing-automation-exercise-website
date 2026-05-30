package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.ASM_Framework;

/**
 * ProductDetailsPage — /product_details/{id} page.
 *
 * <p>Owns every interaction that happens <em>after</em> the user lands on a
 * single product's detail page, including:</p>
 * <ul>
 *   <li>Asserting detail fields (name, category, price, availability, condition, brand)</li>
 *   <li>Setting the quantity and adding the product to the cart</li>
 *   <li>Submitting a product review and verifying the success message</li>
 * </ul>
 *
 * <p>Navigation <em>to</em> this page (e.g. {@code clickFirstViewProduct()})
 * is handled by the calling page ({@link ProductsPage} or {@link HomePage})
 * because the action originates there.</p>
 *
 * <p>Covers: TC08, TC09 (partial), TC13, TC17, TC21, TC14–TC16, TC23–TC24.</p>
 *
 * @author ASMahrous
 */
public class ProductDetailsPage extends BasePage
{
    // ── URL guard ─────────────────────────────────────────────────────────
    // (checked before any interaction to fail fast if the wrong page loaded)

    // ── Product information section ───────────────────────────────────────
    private final By productName         = By.xpath("//div[@class='product-information']//h2");
    private final By productCategory     = By.xpath("//div[@class='product-information']//p[contains(.,'Category')]");
    private final By productPrice        = By.xpath("//div[@class='product-information']//span");
    private final By productAvailability = By.xpath("//div[@class='product-information']//p[contains(.,'Availability')]");
    private final By productCondition    = By.xpath("//div[@class='product-information']//p[contains(.,'Condition')]");
    private final By productBrand        = By.xpath("//div[@class='product-information']//p[contains(.,'Brand')]");

    // ── Quantity + Add to cart ────────────────────────────────────────────
    private final By quantityField  = By.id("quantity");
    private final By addToCartBtn   = By.cssSelector("button[class='btn btn-default cart']");

    // ── Post-add-to-cart modal ────────────────────────────────────────────
    private final By continueShopping = By.xpath("//button[text()='Continue Shopping']");
    private final By viewCartLink     = By.xpath("//u[text()='View Cart']");

    // ── Review section ────────────────────────────────────────────────────
    private final By writeReviewHeading = By.xpath("//a[text()='Write Your Review']");
    private final By reviewName         = By.id("name");
    private final By reviewEmail        = By.id("email");
    private final By reviewText         = By.id("review");
    private final By reviewSubmitBtn    = By.id("button-review");
    private final By reviewSuccessMsg   = By.xpath("//*[contains(text(),'Thank you for your review')]");

    // =====================================================================

    public ProductDetailsPage(ASM_Framework driver) { super(driver); }
    public ProductDetailsPage(String browserName)   { super(browserName); }

    // ── Page identity ─────────────────────────────────────────────────────

    /**
     * Returns {@code true} when the current URL contains {@code /product_details/},
     * confirming the browser is on a product detail page.
     *
     * @return {@code true} if on a product detail page
     */
    public boolean isProductDetailPageOpen()
    {
        return driver.getCurrentPageURL().contains("/product_details/");
    }

    // ── Detail field visibility ───────────────────────────────────────────

    /**
     * Checks whether a given locator's element is visible on the page,
     * killing ads and waiting before the check.
     */
    private boolean isFieldVisible(By locator)
    {
        try
        {
            waitFor(locator);
            return wd().findElement(locator).isDisplayed();
        }
        catch (Exception e) { return false; }
    }

    /** @return {@code true} if the product name heading is visible */
    public boolean isProductNameVisible()         { return isFieldVisible(productName); }

    /** @return {@code true} if the category line is visible */
    public boolean isProductCategoryVisible()     { return isFieldVisible(productCategory); }

    /** @return {@code true} if the price span is visible */
    public boolean isProductPriceVisible()        { return isFieldVisible(productPrice); }

    /** @return {@code true} if the availability line is visible */
    public boolean isProductAvailabilityVisible() { return isFieldVisible(productAvailability); }

    /** @return {@code true} if the condition line is visible */
    public boolean isProductConditionVisible()    { return isFieldVisible(productCondition); }

    /** @return {@code true} if the brand line is visible */
    public boolean isProductBrandVisible()        { return isFieldVisible(productBrand); }

    // ── Quantity & Add to cart ────────────────────────────────────────────

    /**
     * Clears the quantity field and types the given value.
     *
     * @param qty the desired quantity as a string (e.g., {@code "4"})
     */
    public void setQuantity(String qty)
    {
        killAds();
        WebElement qtyField = wd().findElement(quantityField);
        qtyField.clear();
        qtyField.sendKeys(qty);
    }

    /**
     * Clicks the "Add to cart" button on the product detail page.
     * After calling this a modal will appear — use
     * {@link #clickContinueShopping()} or {@link #clickViewCart()} to dismiss it.
     */
    public void clickAddToCart()
    {
        safeClick(addToCartBtn);
    }

    /**
     * Clicks "Continue Shopping" on the post-add-to-cart modal,
     * keeping the user on the current page.
     */
    public void clickContinueShopping()
    {
        waitFor(continueShopping);
        safeClick(continueShopping);
    }

    /**
     * Clicks "View Cart" on the post-add-to-cart modal,
     * navigating directly to the cart page.
     */
    public void clickViewCart()
    {
        jsClick(viewCartLink);
    }

    // ── Review ────────────────────────────────────────────────────────────

    /**
     * Scrolls to the "Write Your Review" section and confirms it is visible.
     *
     * @return {@code true} if the review heading is displayed
     */
    public boolean isWriteReviewVisible()
    {
        driver.scrollToElement(writeReviewHeading);
        waitFor(writeReviewHeading);
        return wd().findElement(writeReviewHeading).isDisplayed();
    }

    /**
     * Fills in the review form and submits it.
     *
     * @param name   reviewer's display name
     * @param email  reviewer's email address
     * @param review the review body text
     */
    public void submitReview(String name, String email, String review)
    {
        killAds();
        driver.writeInElement(reviewName,  name);
        driver.writeInElement(reviewEmail, email);
        driver.writeInElement(reviewText,  review);
        safeClick(reviewSubmitBtn);
    }

    /**
     * Returns {@code true} when the "Thank you for your review." success
     * message is visible after a review is submitted.
     *
     * @return {@code true} if the review success message is displayed
     */
    public boolean isReviewSuccessVisible()
    {
        try
        {
            waitFor(reviewSuccessMsg);
            return wd().findElement(reviewSuccessMsg).isDisplayed();
        }
        catch (Exception e) { return false; }
    }
}