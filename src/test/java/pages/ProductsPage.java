package pages;

import org.openqa.selenium.By;
import utils.ASM_Framework;
import utils.AdsHelper;

/**
 * ProductsPage — /products page (product list and search).
 *
 * <p>Responsible for everything on the All Products listing page:
 * navigating to a product's detail page, searching, hover-add-to-cart
 * from the product cards, and browsing the brands sidebar.</p>
 *
 * <p>Everything that happens <em>inside</em> a product's detail page
 * (field visibility, quantity, add-to-cart button, reviews) has been
 * moved to {@link ProductDetailsPage}.</p>
 *
 * <p>Covers: TC08, TC09, TC12, TC19, TC20.</p>
 *
 * @author ASMahrous
 */
public class ProductsPage extends BasePage
{
    // ── All Products heading & list ───────────────────────────────────────
    private final By allProductsHeading = By.xpath("//h2[text()='All Products']");
    private final By productsList       = By.cssSelector(".features_items .col-sm-4");

    // ── Search ────────────────────────────────────────────────────────────
    private final By searchInput     = By.id("search_product");
    private final By searchButton    = By.id("submit_search");
    private final By searchedHeading = By.xpath("//h2[text()='Searched Products']");

    // ── Hover add-to-cart (product cards on the list page) ───────────────
    private final By firstAddToCart   = By.xpath("(//a[@data-product-id='1'][contains(@class,'add-to-cart')])[1]");
    private final By secondAddToCart  = By.xpath("(//a[@data-product-id='3'][contains(@class,'add-to-cart')])[1]");
    private final By continueShopping = By.xpath("//button[text()='Continue Shopping']");
    private final By viewCartModal    = By.xpath("//u[text()='View Cart']");

    // ── Navigate to product detail ────────────────────────────────────────
    private final By firstViewProduct = By.xpath("(//a[@href='/product_details/1'])[1]");

    // ── Brands sidebar ────────────────────────────────────────────────────
    private final By brandsSidebar   = By.cssSelector(".brands_products");
    private final By firstBrandLink  = By.xpath("(//div[@class='brands-name']//a)[1]");
    private final By secondBrandLink = By.xpath("(//div[@class='brands-name']//a)[2]");
    private final By brandPageTitle  = By.cssSelector(".title.text-center");

    // =====================================================================

    public ProductsPage(ASM_Framework driver) { super(driver); }
    public ProductsPage(String browserName)  { super(browserName); }

    public void open() { driver.goToURL("https://automationexercise.com/products"); }

    // ── All Products page ─────────────────────────────────────────────────

    /**
     * Waits for and checks the "All Products" heading visibility.
     *
     * @return {@code true} if the heading is displayed
     */
    public boolean isAllProductsVisible()
    {
        waitFor(allProductsHeading);
        return wd().findElement(allProductsHeading).isDisplayed();
    }

    /**
     * @return {@code true} if at least one product card is rendered
     */
    public boolean isProductsListVisible()
    {
        return wd().findElements(productsList).size() > 0;
    }

    // ── Navigation to detail page ─────────────────────────────────────────

    /**
     * Clicks the "View Product" link for the first product (id=1),
     * navigating to its detail page.
     *
     * <p>After calling this, switch to {@link ProductDetailsPage} to interact
     * with the detail content.</p>
     */
//    public void clickFirstViewProduct()
//    {
//        safeClick(firstViewProduct);
//    }
    public void clickFirstViewProduct()
    {
        String urlToBe = "/product_details/";
        AdsHelper.killAdsAndClick(wd(),firstViewProduct);
        driver.clickAndWaitForUrl(firstViewProduct, urlToBe, 10);
    }
    // ── Search ────────────────────────────────────────────────────────────

    /**
     * Types {@code name} into the search box and submits the search.
     *
     * @param name the product name to search for
     */
    public void searchProduct(String name)
    {
        killAds();
        driver.writeInElement(searchInput, name);
        safeClick(searchButton);
    }

    /**
     * @return {@code true} if the "Searched Products" heading is visible
     */
    public boolean isSearchedProductsHeadingVisible()
    {
        waitFor(searchedHeading);
        return wd().findElement(searchedHeading).isDisplayed();
    }

    /**
     * @return the number of product cards currently shown (used for search assertions)
     */
    public int getSearchedProductCount()
    {
        return wd().findElements(productsList).size();
    }

    // ── TC12: hover add-to-cart from product cards ────────────────────────

    /**
     * Hovers over the first product card and clicks its "Add to cart" overlay button.
     * A modal will appear — use {@link #clickContinueShopping()} to dismiss it.
     */
    public void hoverAndAddFirstProductToCart()
    {
        driver.hoverOverElement(firstAddToCart);
        safeClick(firstAddToCart);
    }

    /**
     * Hovers over the second product card and clicks its "Add to cart" overlay button.
     * A modal will appear — use {@link #clickViewCartFromModal()} to go to the cart.
     */
    public void hoverAndAddSecondProductToCart()
    {
        driver.hoverOverElement(secondAddToCart);
        safeClick(secondAddToCart);
    }

    /**
     * Dismisses the post-add-to-cart modal by clicking "Continue Shopping".
     */
    public void clickContinueShopping()
    {
        waitFor(continueShopping);
        safeClick(continueShopping);
    }

    /**
     * Dismisses the post-add-to-cart modal by clicking "View Cart",
     * navigating directly to the cart page.
     */
    public void clickViewCartFromModal()
    {
        waitFor(viewCartModal);
        jsClick(viewCartModal);
    }

    // ── TC20: add first searched product without blocking on modal ────────

    /**
     * Hovers and adds the first product to the cart, then silently dismisses
     * the "Continue Shopping" modal if it appears.
     * Use this in flows where you need to stay on the products list page.
     */
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

    // ── TC19: brands sidebar ──────────────────────────────────────────────

    /**
     * @return {@code true} if the Brands sidebar panel is rendered
     */
    public boolean isBrandsSidebarVisible()
    {
        return wd().findElements(brandsSidebar).size() > 0;
    }

    /** Clicks the first brand link in the sidebar. */
    public void clickFirstBrand()  { safeClick(firstBrandLink); }

    /** Clicks the second brand link in the sidebar. */
    public void clickSecondBrand() { safeClick(secondBrandLink); }

    /**
     * Returns the page title shown on a brand products page.
     *
     * @return the title text (e.g., "Brand - Polo Products")
     */
    public String getBrandPageTitle()
    {
        waitFor(brandPageTitle);
        return wd().findElement(brandPageTitle).getText();
    }

    /**
     * @return {@code true} if at least one product is shown on the brand page
     */
    public boolean isBrandPageProductsVisible()
    {
        return !wd().findElements(productsList).isEmpty();
    }
}