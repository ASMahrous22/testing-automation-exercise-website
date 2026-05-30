# AutomationExercise — 26 Test Cases Implementation

Automated test suite covering all 26 official test cases from
[automationexercise.com/test_cases](https://automationexercise.com/test_cases),
built on top of the **ASM Framework** using TestNG, Allure, and the Page Object Model.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Setup & Installation](#setup--installation)
5. [Configuration](#configuration)
6. [Test Data Setup](#test-data-setup)
7. [Page Objects](#page-objects)
8. [All 26 Test Cases — Full Reference](#all-26-test-cases--full-reference)
9. [Registering Tests in testng.xml](#registering-tests-in-testngxml)
10. [Running the Tests](#running-the-tests)
11. [Viewing the Allure Report](#viewing-the-allure-report)
12. [Framework API Quick Reference](#framework-api-quick-reference)

---

## Project Overview

This repository implements the complete set of 26 automated test cases defined on
AutomationExercise.com, plus two additional negative-path cases (N01, N02). The
tests are organized by feature area into separate test classes, each extending
`BaseTest` for automatic browser lifecycle management, thread-safe parallel
execution via `ThreadLocal`, and on-failure screenshot capture.

**Feature areas covered:**

| Area | Test Cases | Test Class |
|---|---|---|
| User Registration & Login | TC01 – TC05, NEGATIVE N01–N02 | `RegisterLoginTests` |
| Contact Us & Navigation | TC06 – TC07 | `ContactAndNavigationTests` |
| Products & Search | TC08, TC09, TC20, TC21 | `ProductTests` |
| Subscription | TC10 – TC11 | `SubscriptionTests` |
| Cart Management | TC12, TC13, TC17, TC22 | `CartTests` |
| Checkout & Orders | TC14 – TC16, TC23 – TC24 | `CheckoutTests` |
| Categories & Brands | TC18, TC19 | `CategoryBrandTests` |
| UI / Scroll Behaviour | TC25 – TC26 | `ScrollTests` |

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 24 | Language |
| Selenium WebDriver | 4.41.0 | Browser automation |
| TestNG | 7.12.0 | Test runner, lifecycle, suite XML |
| Allure TestNG | 2.34.0 | HTML reports + screenshot attachment |
| Gson | 2.14.0 | JSON test data deserialization |
| Maven | 3.x | Build and dependency management |

---

## Project Structure

```
automationexercise-tests/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── utils/
│   │           ├── ASM_Framework.java          ← single driver entry point (use this in tests)
│   │           └── framework/                  ← internal managers (do not use directly)
│   │               ├── BrowserManager.java
│   │               ├── WaitManager.java
│   │               ├── ElementFinder.java
│   │               ├── ElementInteractions.java
│   │               ├── ActionsManager.java
│   │               ├── DropdownManager.java
│   │               ├── WindowManager.java
│   │               ├── AlertManager.java
│   │               ├── FrameManager.java
│   │               └── ScreenshotManager.java
│   │
│   └── test/
│       ├── java/
│       │   ├── pages/
│       │   │   ├── BasePage.java               ← parent for all page objects
│       │   │   ├── HomePage.java
│       │   │   ├── LoginPage.java              ← TC01–TC05
│       │   │   ├── ContactPage.java            ← TC06
│       │   │   ├── ProductsPage.java           ← TC08–TC09, TC19–TC20
│       │   │   ├── ProductDetailsPage.java     ← TC08, TC13, TC17, TC21
│       │   │   ├── CartPage.java               ← TC11–TC13, TC17, TC22
│       │   │   ├── CheckoutPage.java           ← TC14–TC16, TC23–TC24
│       │   │   └── PaymentPage.java            ← TC14–TC16, TC24
│       │   │
│       │   ├── tests/
│       │   │   ├── BaseTest.java               ← lifecycle, ThreadLocal driver, config
│       │   │   ├── RegisterLoginTests.java     ← TC01–TC05, N01, N02
│       │   │   ├── ContactAndNavigationTests.java ← TC06–TC07
│       │   │   ├── ProductTests.java           ← TC08, TC09, TC20, TC21
│       │   │   ├── SubscriptionTests.java      ← TC10–TC11
│       │   │   ├── CartTests.java              ← TC12, TC13, TC17, TC22
│       │   │   ├── CheckoutTests.java          ← TC14–TC16, TC23–TC24
│       │   │   ├── CategoryBrandTests.java     ← TC18–TC19
│       │   │   └── ScrollTests.java            ← TC25–TC26
│       │   │
│       │   ├── testdata/
│       │   │   ├── UserData.java
│       │   │   ├── PaymentData.java
│       │   │   └── ContactData.java
│       │   │
│       │   └── utils/
│       │       ├── AdsHelper.java              ← ad-killing, safe-click, wait helpers
│       │       ├── AllureHelper.java           ← screenshot → Allure attachment
│       │       └── DataReader.java             ← JSON test data reader (Gson)
│       │
│       └── resources/
│           ├── config.properties
│           └── testdata/
│               ├── user.json
│               ├── existingUser.json
│               ├── payment.json
│               ├── contact.json
│               └── upload_sample.txt
│
├── testng-suites/
│   └── Testng.xml
│
├── Screenshots/                               ← auto-created at runtime
├── allure-results/                            ← auto-created by Allure
├── pom.xml
└── README.md
```

> **Important:** All framework interactions in page objects and tests go through
> `ASM_Framework` (accessed via `driver` in page objects or `getDriver()` in tests).
> The classes under `utils/framework/` are internal implementation details — never
> import or instantiate them directly.

---

## Setup & Installation

### Prerequisites

- Java 24+
- Maven 3.x
- IntelliJ IDEA (or any IDE)
- Google Chrome, Firefox, or Edge installed

### Steps

```bash
# 1. Clone
git clone https://github.com/YOUR_USERNAME/automationexercise-tests.git
cd automationexercise-tests

# 2. Open in IntelliJ
#    File → Open → select folder → Open as Project

# 3. Mark source roots (if not auto-detected)
#    Right-click src/test/java      → Mark Directory as → Test Sources Root
#    Right-click src/test/resources → Mark Directory as → Test Resources Root

# 4. Load dependencies
mvn dependency:resolve

# 5. Verify compilation
mvn test-compile
```

---

## Configuration

Edit `src/test/resources/config.properties` before your first run:

```properties
# Browser to run tests on: chrome | firefox | edge | safari
browser=chrome

# Base URL — do not change unless the site moves
base.url=https://automationexercise.com

# true → no visible window; ideal for CI pipelines
headless=false
```

`BaseTest` reads these values automatically at suite startup. You can also override
them on the command line:

```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

---

## Test Data Setup

All test data lives in `src/test/resources/testdata/` as JSON files. They are
deserialized at runtime by `DataReader` using Gson.

### `user.json` — new user registration details

```json
{
  "title":         "Mr",
  "name":          "abdallah",
  "firstName":     "Abdallah",
  "lastName":      "Mahrous",
  "email":         "asm7391@test.com",
  "password":      "Test@1234",
  "dayOfBirth":    "27",
  "monthOfBirth":  "February",
  "yearOfBirth":   "2001",
  "company":       "EDGES",
  "address1":      "123 Test Street",
  "address2":      "Suite 456",
  "country":       "United States",
  "state":         "California",
  "city":          "Los Angeles",
  "zipcode":       "90001",
  "mobileNumber":  "5551234567"
}
```

> **Important:** Registration tests (TC01, TC02, TC04, TC14–TC16, TC23–TC24)
> override `email` at runtime using `System.currentTimeMillis()` to guarantee
> uniqueness, so the email in this file is only a fallback.

### `existingUser.json` — a pre-created account for TC05 and TC20

```json
{
  "name":     "abdallah",
  "email":    "asm4821@test.com",
  "password": "Test@1234"
}
```

Create this account manually on the site once before running TC05 and TC20.

### `payment.json` — card details for checkout tests

```json
{
  "nameOnCard":   "Abdallah Mahrous",
  "cardNumber":   "4111111111111111",
  "cvc":          "123",
  "expiryMonth":  "12",
  "expiryYear":   "2027"
}
```

### `contact.json` — Contact Us form data for TC06

```json
{
  "name":     "abdallah",
  "email":    "asm6204@test.com",
  "subject":  "Test Automation Inquiry",
  "message":  "This is an automated test message sent by the Selenium test suite.",
  "filePath": "src/test/resources/testdata/upload_sample.txt"
}
```

### POJO classes — `src/test/java/testdata/`

| Class | JSON file | Fields |
|---|---|---|
| `UserData` | `user.json`, `existingUser.json` | title, name, firstName, lastName, email, password, dayOfBirth, monthOfBirth, yearOfBirth, company, address1, address2, country, state, city, zipcode, mobileNumber |
| `PaymentData` | `payment.json` | nameOnCard, cardNumber, cvc, expiryMonth, expiryYear |
| `ContactData` | `contact.json` | name, email, subject, message, filePath |

---

## Page Objects

Each page class extends `BasePage` and exposes public methods for exactly one
page's actions. Locators are `private final By` fields — never exposed publicly.
All clicks and waits go through `AdsHelper` primitives inherited from `BasePage`
to suppress ad overlays that would otherwise block interactions.

| Page Class | URL / area | Covers |
|---|---|---|
| `HomePage` | `/` | Visibility, navbar clicks, logged-in state, subscription footer, categories sidebar, recommended items, scroll-up |
| `LoginPage` | `/login` | Login form, signup step 1, account info form, ACCOUNT CREATED, Continue button |
| `ContactPage` | `/contact_us` | Contact Us form, file upload, alert, success message, Home button |
| `ProductsPage` | `/products` | Product list, search, hover add-to-cart, View Product link, brands sidebar |
| `ProductDetailsPage` | `/product_details/{id}` | Detail fields, quantity, add-to-cart, Continue/View Cart modal, review form |
| `CartPage` | `/view_cart` | Item count, quantity, remove, proceed to checkout, Register/Login link, subscription |
| `CheckoutPage` | `/checkout` | Address blocks, order comment, Place Order button |
| `PaymentPage` | `/payment` | Payment form, Pay and Confirm, order success, Download Invoice, Continue |

---

## All 26 Test Cases — Full Reference

### TC01 — Register User
**Class:** `RegisterLoginTests` · **Method:** `TC01_registerNewUser`

Navigates to the home page, clicks Signup/Login, fills the signup form with a
unique email, completes account information, verifies ACCOUNT CREATED, continues,
verifies "Logged in as abdallah" in the navbar, then deletes the account.

---

### TC02 — Login User with correct email and password
**Class:** `RegisterLoginTests` · **Method:** `TC02_loginWithValidCredentials`

Registers a fresh account (auto-logs in), logs out, logs back in with the same
credentials, verifies the logged-in navbar label, then deletes the account.

---

### TC03 — Login User with incorrect email and password
**Class:** `RegisterLoginTests` · **Method:** `TC03_loginWithInvalidCredentials`

Attempts login with a non-existent email and wrong password, then verifies the
"Your email or password is incorrect!" error message is visible.

---

### TC04 — Logout User
**Class:** `RegisterLoginTests` · **Method:** `TC04_logoutUser`

Registers a fresh account (auto-logs in), clicks Logout, and verifies the browser
URL contains `/login`.

---

### TC05 — Register User with existing email
**Class:** `RegisterLoginTests` · **Method:** `TC05_registerWithExistingEmail`

Attempts signup with the email from `existingUser.json` and verifies the
"Email Address already exist!" error is shown.

---

### NEGATIVE TC-N01 — Login with empty credentials
**Class:** `RegisterLoginTests` · **Method:** `NEGATIVE_TC_N01_loginWithEmptyCredentials`

Submits the login form with both fields blank and asserts the user is NOT logged in.

---

### NEGATIVE TC-N02 — Register with invalid email format
**Class:** `RegisterLoginTests` · **Method:** `NEGATIVE_TC_N02_registerWithInvalidEmailFormat`

Attempts signup with a malformed email ("notAnEmail") and verifies the page stays on `/login`
due to HTML5 field validation blocking the submission.

---

### TC06 — Contact Us Form
**Class:** `ContactAndNavigationTests` · **Method:** `TC06_submitContactUsForm`

Opens the Contact Us page, fills all fields, uploads `upload_sample.txt`, submits,
accepts the browser alert, verifies the success message contains "Success", then
clicks Home and verifies the home page is visible.

---

### TC07 — Verify Test Cases Page
**Class:** `ContactAndNavigationTests` · **Method:** `TC07_navigateToTestCasesPage`

Clicks "Test Cases" in the navbar and asserts the URL contains `test_cases`.

---

### TC08 — Verify All Products and product detail page
**Class:** `ProductTests` · **Method:** `TC08_verifyAllProductsAndDetailPage`

Navigates to Products, asserts the heading and product list are visible, clicks
"View Product" on the first item, then asserts all six detail fields (name,
category, price, availability, condition, brand) are visible.

---

### TC09 — Search Product
**Class:** `ProductTests` · **Method:** `TC09_searchProduct`

On the Products page, searches for "Blue Top", asserts the "Searched Products"
heading appears, and verifies at least one result is displayed.

---

### TC10 — Verify Subscription in home page
**Class:** `SubscriptionTests` · **Method:** `TC10_subscriptionOnHomePage`

Scrolls to the home page footer, asserts "SUBSCRIPTION" is visible, submits a
unique email, and verifies the "You have been successfully subscribed!" message.

---

### TC11 — Verify Subscription in Cart page
**Class:** `SubscriptionTests` · **Method:** `TC11_subscriptionOnCartPage`

Navigates to the Cart page, scrolls to footer, verifies "SUBSCRIPTION", subscribes
with a unique email, and asserts the success message.

---

### TC12 — Add Products in Cart
**Class:** `CartTests` · **Method:** `TC12_addTwoProductsToCart`

On the Products page, hovers and adds the first product (Continue Shopping), then
hovers and adds the second product (View Cart). Asserts the cart contains at least
two items.

---

### TC13 — Verify Product quantity in Cart
**Class:** `CartTests` · **Method:** `TC13_verifyProductQuantityInCart`

Clicks "View Product" from the home page, sets quantity to 4, adds to cart, opens
the cart, and asserts the quantity shown is "4".

---

### TC14 — Place Order: Register while Checkout
**Class:** `CheckoutTests` · **Method:** `TC14_placeOrderRegisterDuringCheckout`

Adds a product, goes to cart, clicks Proceed to Checkout as a guest, clicks
Register/Login, registers a new account, returns to the cart, proceeds to checkout,
verifies address and order review, enters a comment, places the order, fills payment
details, verifies "Your order has been placed successfully!", then deletes the account.

---

### TC15 — Place Order: Register before Checkout
**Class:** `CheckoutTests` · **Method:** `TC15_placeOrderRegisterBeforeCheckout`

Registers first, adds a product, proceeds to checkout while already logged in,
places the order with payment, verifies success, then deletes the account.

---

### TC16 — Place Order: Login before Checkout
**Class:** `CheckoutTests` · **Method:** `TC16_placeOrderLoginBeforeCheckout`

Registers a fresh account (which auto-logs in), adds a product, proceeds to
checkout, places the order, verifies success, then deletes the account.

---

### TC17 — Remove Products From Cart
**Class:** `CartTests` · **Method:** `TC17_removeProductFromCart`

Adds a product via the detail page, opens the cart, clicks the delete (×) button
on the first row, and asserts the cart is empty.

---

### TC18 — View Category Products
**Class:** `CategoryBrandTests` · **Method:** `TC18_viewCategoryProducts`

Verifies the category sidebar is visible, expands Women → Dress, asserts the
heading contains "DRESS" or "WOMEN", then expands Men → Tshirts and asserts the
heading contains "TSHIRT" or "MEN".

---

### TC19 — View & Cart Brand Products
**Class:** `CategoryBrandTests` · **Method:** `TC19_viewBrandProducts`

On the Products page, asserts the brands sidebar is visible, clicks the first brand,
verifies a non-empty title and at least one product, then clicks the second brand
and repeats the assertion.

---

### TC20 — Search Products and Verify Cart After Login
**Class:** `ProductTests` · **Method:** `TC20_searchProductsAndVerifyCartAfterLogin`

Searches for "Blue Top", adds the first result to the cart, verifies the cart has
items, logs in with `existingUser.json` credentials, re-opens the cart, and asserts
the items are still present after login.

---

### TC21 — Add review on product
**Class:** `ProductTests` · **Method:** `TC21_addReviewOnProduct`

Opens the first product's detail page, scrolls to "Write Your Review", submits a
review (name, email, text), and verifies the "Thank you for your review." success
message.

---

### TC22 — Add to cart from Recommended items
**Class:** `CartTests` · **Method:** `TC22_addToCartFromRecommendedItems`

Scrolls to the "RECOMMENDED ITEMS" section on the home page, asserts it is visible,
clicks "Add To Cart" on the first recommended item, navigates to the cart (via modal
or navbar), and asserts the cart has items.

---

### TC23 — Verify address details in checkout page
**Class:** `CheckoutTests` · **Method:** `TC23_verifyAddressDetailsAtCheckout`

Registers a new account, adds a product, proceeds to checkout, and asserts that the
delivery address block contains the first name and city used during registration.
Then deletes the account.

---

### TC24 — Download Invoice after purchase order
**Class:** `CheckoutTests` · **Method:** `TC24_downloadInvoiceAfterOrder`

Completes a full order flow (add product → checkout as guest → register → return to
cart → place order → pay), verifies "Your order has been placed successfully!",
clicks "Download Invoice", clicks Continue, then deletes the account.

---

### TC25 — Verify Scroll Up using 'Arrow' button and Scroll Down
**Class:** `ScrollTests` · **Method:** `TC25_scrollUpWithArrowButton`

Scrolls to the footer, verifies "SUBSCRIPTION" is visible, clicks the scroll-up
arrow (bottom-right), and asserts the hero text "Full-Fledged practice website for
Automation Engineers" is visible.

---

### TC26 — Verify Scroll Up without 'Arrow' button and Scroll Down
**Class:** `ScrollTests` · **Method:** `TC26_scrollUpWithoutArrowButton`

Same as TC25 but scrolls back to the top via `window.scrollTo({top:0})` JavaScript
instead of the arrow button.

---

## Registering Tests in testng.xml

All test classes are registered in `testng-suites/Testng.xml`. The full suite:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">

<suite name="AutomationExercise Test Suite" verbose="1">

    <test name="Registration and Login Tests">
        <classes>
            <class name="tests.RegisterLoginTests"/>
        </classes>
    </test>

    <test name="Contact Us and Navigation Tests">
        <classes>
            <class name="tests.ContactAndNavigationTests"/>
        </classes>
    </test>

    <test name="Product Tests">
        <classes>
            <class name="tests.ProductTests"/>
        </classes>
    </test>

    <test name="Subscription Tests">
        <classes>
            <class name="tests.SubscriptionTests"/>
        </classes>
    </test>

    <test name="Cart Tests">
        <classes>
            <class name="tests.CartTests"/>
        </classes>
    </test>

    <test name="Checkout and Orders Tests">
        <classes>
            <class name="tests.CheckoutTests"/>
        </classes>
    </test>

    <test name="Category and Brand Tests">
        <classes>
            <class name="tests.CategoryBrandTests"/>
        </classes>
    </test>

    <test name="Scroll Behaviour Tests">
        <classes>
            <class name="tests.ScrollTests"/>
        </classes>
    </test>

</suite>
```

### Running specific methods only

```xml
<test name="Registration and Login Tests">
    <classes>
        <class name="tests.RegisterLoginTests">
            <methods>
                <include name="TC01_registerNewUser"/>
                <include name="TC02_loginWithValidCredentials"/>
            </methods>
        </class>
    </classes>
</test>
```

### Running in parallel (methods)

Each `@Test` gets its own browser via `@BeforeMethod` (ThreadLocal driver), so
parallel-by-methods is safe without code changes:

```xml
<suite name="AutomationExercise Test Suite" verbose="1"
       parallel="methods" thread-count="4">
    ...
</suite>
```

---

## Running the Tests

```bash
# Run all tests via testng.xml
mvn test

# Run a single test class
mvn test -Dtest=CartTests

# Run a single test method
mvn test -Dtest=CartTests#TC12_addTwoProductsToCart

# Run headless on CI
mvn test -Dheadless=true

# Run on a specific browser
mvn test -Dbrowser=firefox
```

---

## Viewing the Allure Report

```bash
# Generate and open the report in your browser (recommended)
mvn allure:serve

# Generate static HTML only
mvn allure:report
# Output: target/site/allure-maven-plugin/index.html
```

The report shows a pass/fail breakdown per test class, full stack traces for
failures, and screenshots attached directly to failing tests. Any manual
`saveScreenshot()` calls inside tests also appear at the exact step where they
were taken.

On-disk screenshots are saved under `Screenshots/` with this naming pattern:

```
Screenshots/
├── TC01_LoggedIn_2025-07-21_14-35-22-123.png
├── TC02_LoggedIn_2025-07-21_14-36-01-456.png
└── FAILED_TC03_loginWithInvalidCredentials_2025-07-21_14-37-00-789.png
```

---

## Framework API Quick Reference

All interactions go through the `ASM_Framework` instance — accessed via `driver`
in page objects (inherited from `BasePage`) or via `getDriver()` in test classes
(inherited from `BaseTest`). The classes inside `utils/framework/` are internal
implementation; never import them directly.

```java
// ── Navigation ────────────────────────────────────────────────────────────
driver.goToURL("https://automationexercise.com/login");
driver.manageNavigationButtons("back");   // "back" | "forward" | "refresh"
driver.getCurrentPageTitle();
driver.getCurrentPageURL();
driver.manageScreenSize("maximize");      // "maximize" | "minimize" | fullscreen

// ── Finding Elements ──────────────────────────────────────────────────────
WebElement el = driver.findElement("css", "[data-qa='login-email']");
By         by = driver.getBy("id", "submit-btn");

// Supported locator types: "id", "name", "class", "xpath", "css"

// ── Interactions ──────────────────────────────────────────────────────────
driver.clickElement(locator);
driver.clickElement(element);
driver.writeInElement(locator, "text");   // clears first, then types
driver.writeInElement(element, "text");
driver.getElementText(locator);
driver.clearElementText(locator);

// ── Click + Navigation Wait ───────────────────────────────────────────────
// Clicks the element then waits until the URL contains the expected substring.
// Use for navigation-bound clicks; not for modals or JS-only actions.
driver.clickAndWaitForUrl(locator, "/product_details/", 10);

// ── Element State ─────────────────────────────────────────────────────────
driver.validateElementIsDisplayed(element);   // returns boolean
driver.validateElementIsEnabled(element);
driver.validateElementIsSelected(element);

// ── Dropdowns (<select>) ──────────────────────────────────────────────────
driver.selectFromDropDownMenu(locator, "visible", "Egypt");
driver.selectFromDropDownMenu(locator, "value",   "eg");
driver.selectFromDropDownMenu(locator, "index",   "2");
driver.selectFromDropDownMenu(locator, "contains","Egy");

driver.deselectFromDropDownMenu(locator, "index", "0");
driver.deselectFromDropDownMenu(locator, "all",   "");

// ── Advanced Interactions ─────────────────────────────────────────────────
driver.hoverOverElement(locator);
driver.hoverOverElement(element);
driver.doubleClick(locator);
driver.rightClick(locator);
driver.scrollToElement(locator);          // centers element in viewport (JS)
driver.scrollToElement(element);
driver.dragAndDrop(sourceLocator, targetLocator);

// ── Checkboxes & Radio Buttons ────────────────────────────────────────────
driver.checkCheckbox(locator);            // clicks only if not already checked
driver.uncheckCheckbox(locator);          // clicks only if currently checked
driver.selectRadioButton(locator);        // clicks only if not already selected

// ── Waits ─────────────────────────────────────────────────────────────────
// Prefer AdsHelper.waitForElement() in page objects (kills ads before waiting).
// Use framework waits for non-ad-affected elements.
driver.setExplicitWait(locator, 15);
driver.setFluentWait(locator, 10, 500, "Timeout waiting for element");
driver.setImplicitWait(5);                // avoid mixing with explicit waits

// ── Alerts ────────────────────────────────────────────────────────────────
driver.acceptAlert();
driver.dismissAlert();
driver.getAlertText();
driver.typeInAlert("text");

// ── iFrames ───────────────────────────────────────────────────────────────
driver.switchToIFrame(locator);
driver.switchToIFrame(element);
driver.switchToIFrameByIndex(0);
driver.switchToIFrameByNameOrId("myFrame");
driver.switchToDefaultContent();
driver.switchToParentFrame();

// ── Windows & Tabs ────────────────────────────────────────────────────────
String main = driver.getCurrentWindowHandle();
driver.switchToNewWindow(main);
driver.switchToWindowByIndex(1);
driver.closeCurrentWindowAndSwitchTo(main);
driver.getWindowCount();

// ── Screenshots (saved to disk + attached to Allure) ─────────────────────
// From a test class:
AllureHelper.saveScreenshot("TC01_AfterLogin", getDriver());

// From a page object (BasePage helper):
saveScreenshot("TC01_AfterLogin", driver);

// Raw capture (returns Path for manual use):
Path shot = getDriver().takeScreenshot("LoginPage");
```

### AdsHelper — ad-safe primitives (used inside page objects)

`BasePage` wraps these and exposes them to all page classes. Use them instead of
raw `driver.clickElement()` on automationexercise.com, where ad overlays
frequently intercept clicks.

```java
// From any page class (inherited from BasePage):
jsClick(locator);        // kill ads + JS click — use for ALL navbar/link clicks
jsClick(element);
safeClick(locator);      // kill ads + regular click with retry, JS as last resort
waitFor(locator);        // kill ads + wait for visibility before asserting
waitAndGet(locator);     // same, returns the element
killAds();               // strip ads only — call before writing into a field
```

---

## Lifecycle Overview

`BaseTest` manages the full driver lifecycle so tests stay independent:

```
@BeforeSuite   → prints environment banner once
  @BeforeClass → logs class name to console + Allure description
    @BeforeMethod → creates fresh ASM_Framework, maximizes, navigates to base URL
      @Test
    @AfterMethod  → on FAILURE: captures screenshot + attaches to Allure
                    always: navigates to about:blank, quits browser, clears ThreadLocal
  @AfterClass  → logs completion banner
@AfterSuite    → prints total elapsed time
```

Each `@Test` runs with its own isolated browser instance. This makes every test
independently executable and safe for parallel execution at the method level.

---

## Author

**ASMahrous** — EDGES Software Testing Diploma · Web Automation Final Project

Website under test: [automationexercise.com](https://automationexercise.com) ·
Test cases spec: [automationexercise.com/test_cases](https://automationexercise.com/test_cases)
