# AutomationExercise — 26 Test Cases Implementation

Automated test suite covering all 26 official test cases from
[automationexercise.com/test_cases](https://automationexercise.com/test_cases),
built on top of the ASM Framework using TestNG, Allure, and the Page Object Model.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Setup & Installation](#setup--installation)
5. [Configuration](#configuration)
6. [Test Data Setup](#test-data-setup)
7. [Page Objects Used](#page-objects-used)
8. [All 26 Test Cases — Full Reference](#all-26-test-cases--full-reference)
9. [Registering Tests in testng.xml](#registering-tests-in-testngxml)
10. [Running the Tests](#running-the-tests)
11. [Viewing the Allure Report](#viewing-the-allure-report)
12. [Framework API Quick Reference](#framework-api-quick-reference)

---

## Project Overview

This repository implements the complete set of 26 automated test cases defined on
AutomationExercise.com. The tests are organized by feature area into separate test
classes, each extending `BaseTest` for automatic browser lifecycle, parallel-safe
thread-local driver management, and on-failure screenshot capture.

**Feature areas covered:**

| Area | Test Cases | Test Class |
|---|---|---|
| User Registration & Login | TC01 – TC05 | `RegisterLoginTests` |
| Contact Us & Navigation | TC06 – TC07 | `ContactAndNavigationTests` |
| Products & Search | TC08 – TC09, TC19 – TC21 | `ProductTests` |
| Subscription | TC10 – TC11 | `SubscriptionTests` |
| Cart Management | TC12 – TC13, TC17, TC22 | `CartTests` |
| Checkout & Orders | TC14 – TC16, TC23 – TC24 | `CheckoutTests` |
| Categories & Brands | TC18 – TC19 | `CategoryBrandTests` |
| UI / Scroll Behaviour | TC25 – TC26 | `ScrollTests` |

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 24 | Language |
| Selenium WebDriver | 4.40.0 | Browser automation |
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
│   │           ├── ASM_Framework.java          ← single driver entry point
│   │           └── framework/                  ← internal managers (do not edit)
│   │
│   └── test/
│       ├── java/
│       │   ├── pages/
│       │   │   ├── BasePage.java
│       │   │   ├── HomePage.java
│       │   │   ├── LoginPage.java              ← TC01–TC05
│       │   │   ├── ContactPage.java            ← TC06
│       │   │   ├── ProductsPage.java           ← TC08–TC09, TC19–TC21
│       │   │   ├── CartPage.java               ← TC11–TC13, TC17, TC22
│       │   │   ├── CheckoutPage.java           ← TC14–TC16, TC23–TC24
│       │   │   └── PaymentPage.java            ← TC14–TC16, TC24
│       │   │
│       │   ├── tests/
│       │   │   ├── BaseTest.java
│       │   │   ├── RegisterLoginTests.java     ← TC01–TC05
│       │   │   ├── ContactAndNavigationTests.java ← TC06–TC07
│       │   │   ├── ProductTests.java           ← TC08–TC09, TC21
│       │   │   ├── SubscriptionTests.java      ← TC10–TC11
│       │   │   ├── CartTests.java              ← TC12–TC13, TC17, TC22
│       │   │   ├── CheckoutTests.java          ← TC14–TC16, TC23–TC24
│       │   │   ├── CategoryBrandTests.java     ← TC18–TC19
│       │   │   └── ScrollTests.java            ← TC25–TC26
│       │   │
│       │   ├── testdata/
│       │   │   ├── UserData.java
│       │   │   ├── PaymentData.java
│       │   │   └── AddressData.java
│       │   │
│       │   └── utils/
│       │       ├── AllureHelper.java
│       │       └── DataReader.java
│       │
│       └── resources/
│           ├── config.properties
│           └── testdata/
│               ├── user.json
│               ├── existingUser.json
│               ├── payment.json
│               └── address.json
│
├── testng-suites/
│   └── testng.xml
│
├── Screenshots/                               ← auto-created at runtime
├── allure-results/                            ← auto-created by Allure
├── pom.xml
└── README.md
```

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

These values are read automatically by `BaseTest`. You can also override them
at the command line:

```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

> **Note:** To use command-line overrides, add `System.getProperty("browser", CONFIG.getProperty("browser"))` in `BaseTest.loadConfig()`.

---

## Test Data Setup

All test data lives in `src/test/resources/testdata/` as JSON files.
Create the following files before running the tests.

### `user.json` — new user registration details

```json
{
  "name": "Test User",
  "email": "testuser_unique123@example.com",
  "password": "Test@1234",
  "firstName": "Test",
  "lastName": "User",
  "company": "Test Corp",
  "address1": "123 Test Street",
  "address2": "Suite 456",
  "country": "United States",
  "state": "California",
  "city": "Los Angeles",
  "zipcode": "90001",
  "mobileNumber": "5551234567",
  "dayOfBirth": "15",
  "monthOfBirth": "June",
  "yearOfBirth": "1990"
}
```

> **Important:** The email in `user.json` must be unique each run, or TC01 will
> fail on the registration step because the email already exists. Either use a
> dynamic email in the test (`UUID` + `@example.com`) or change this file before
> each run.

### `existingUser.json` — a pre-created account for login tests

```json
{
  "email": "existing_account@example.com",
  "password": "ExistingPass@1"
}
```

Create this account manually on the site once before running TC02, TC04, TC16, TC20.

### `payment.json` — card details for checkout tests

```json
{
  "nameOnCard": "Test User",
  "cardNumber": "4111111111111111",
  "cvc": "123",
  "expiryMonth": "12",
  "expiryYear": "2027"
}
```

### `address.json` — address used to verify checkout address matching (TC23)

```json
{
  "firstName": "Test",
  "lastName": "User",
  "company": "Test Corp",
  "address1": "123 Test Street",
  "address2": "Suite 456",
  "country": "United States",
  "state": "California",
  "city": "Los Angeles",
  "zipcode": "90001",
  "mobileNumber": "5551234567"
}
```

### Corresponding POJOs — `src/test/java/testdata/`

**`UserData.java`**
```java
package testdata;
public class UserData {
    public String name, email, password;
    public String firstName, lastName, company;
    public String address1, address2, country, state, city, zipcode, mobileNumber;
    public String dayOfBirth, monthOfBirth, yearOfBirth;
}
```

**`PaymentData.java`**
```java
package testdata;
public class PaymentData {
    public String nameOnCard, cardNumber, cvc, expiryMonth, expiryYear;
}
```

**`AddressData.java`**
```java
package testdata;
public class AddressData {
    public String firstName, lastName, company;
    public String address1, address2, country, state, city, zipcode, mobileNumber;
}
```

---

## Page Objects Used

Each page class extends `BasePage` and exposes methods for exactly one page's
actions. Locators are `private final By` fields — never exposed publicly.

| Page Class | Covers |
|---|---|
| `HomePage` | Home page visibility, scroll, recommended items, subscription footer, scroll-up arrow |
| `LoginPage` | Login form, signup form, error messages, logged-in header assertion |
| `ContactPage` | Contact Us form, file upload, submit, success message |
| `ProductsPage` | Products list, search, product detail, hover add-to-cart, categories, brands, review form |
| `CartPage` | Cart items, quantities, prices, remove product, proceed to checkout |
| `CheckoutPage` | Address details, order comment, place order |
| `PaymentPage` | Payment form, confirm order, download invoice |

---

## All 26 Test Cases — Full Reference

Each entry below lists the test class and method name it maps to, plus the
complete steps from the official spec.

---

### TC01 — Register User
**Class:** `RegisterLoginTests` · **Method:** `registerNewUser`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login'
4. Verify 'New User Signup!' is visible
5. Enter name and email address
6. Click 'Signup'
7. Verify 'ENTER ACCOUNT INFORMATION' is visible
8. Fill Title, Name, Email, Password, Date of birth
9. Check 'Sign up for our newsletter!'
10. Check 'Receive special offers from our partners!'
11. Fill First name, Last name, Company, Address, Address2, Country, State, City, Zipcode, Mobile Number
12. Click 'Create Account'
13. Verify 'ACCOUNT CREATED!' is visible
14. Click 'Continue'
15. Verify 'Logged in as username' is visible
16. Click 'Delete Account'
17. Verify 'ACCOUNT DELETED!' is visible and click 'Continue'

---

### TC02 — Login User with correct email and password
**Class:** `RegisterLoginTests` · **Method:** `loginWithValidCredentials`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login'
4. Verify 'Login to your account' is visible
5. Enter correct email and password
6. Click 'login'
7. Verify 'Logged in as username' is visible
8. Click 'Delete Account'
9. Verify 'ACCOUNT DELETED!' is visible

---

### TC03 — Login User with incorrect email and password
**Class:** `RegisterLoginTests` · **Method:** `loginWithInvalidCredentials`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login'
4. Verify 'Login to your account' is visible
5. Enter incorrect email and password
6. Click 'login'
7. Verify error 'Your email or password is incorrect!' is visible

---

### TC04 — Logout User
**Class:** `RegisterLoginTests` · **Method:** `logoutUser`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login'
4. Verify 'Login to your account' is visible
5. Enter correct email and password
6. Click 'login'
7. Verify 'Logged in as username' is visible
8. Click 'Logout'
9. Verify user is navigated to login page

---

### TC05 — Register User with existing email
**Class:** `RegisterLoginTests` · **Method:** `registerWithExistingEmail`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login'
4. Verify 'New User Signup!' is visible
5. Enter name and an already-registered email address
6. Click 'Signup'
7. Verify error 'Email Address already exist!' is visible

---

### TC06 — Contact Us Form
**Class:** `ContactAndNavigationTests` · **Method:** `submitContactUsForm`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Contact Us'
4. Verify 'GET IN TOUCH' is visible
5. Enter name, email, subject and message
6. Upload a file
7. Click 'Submit'
8. Click OK on the alert dialog
9. Verify success message 'Success! Your details have been submitted successfully.'
10. Click 'Home' and verify landing on home page

---

### TC07 — Verify Test Cases Page
**Class:** `ContactAndNavigationTests` · **Method:** `navigateToTestCasesPage`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Test Cases' in the navigation
4. Verify user is navigated to the test cases page

---

### TC08 — Verify All Products and product detail page
**Class:** `ProductTests` · **Method:** `verifyAllProductsAndDetailPage`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Products'
4. Verify ALL PRODUCTS page is displayed
5. Verify the products list is visible
6. Click 'View Product' on the first product
7. Verify product detail page is opened
8. Verify product name, category, price, availability, condition, brand are visible

---

### TC09 — Search Product
**Class:** `ProductTests` · **Method:** `searchProduct`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Products'
4. Verify ALL PRODUCTS page is displayed
5. Enter a product name in the search input and click Search
6. Verify 'SEARCHED PRODUCTS' heading is visible
7. Verify all products related to the search term are displayed

---

### TC10 — Verify Subscription in home page
**Class:** `SubscriptionTests` · **Method:** `subscriptionOnHomePage`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Scroll down to the footer
4. Verify text 'SUBSCRIPTION' is visible
5. Enter an email address and click the arrow button
6. Verify success message 'You have been successfully subscribed!'

---

### TC11 — Verify Subscription in Cart page
**Class:** `SubscriptionTests` · **Method:** `subscriptionOnCartPage`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Cart'
4. Scroll down to the footer
5. Verify text 'SUBSCRIPTION' is visible
6. Enter an email address and click the arrow button
7. Verify success message 'You have been successfully subscribed!'

---

### TC12 — Add Products in Cart
**Class:** `CartTests` · **Method:** `addTwoProductsToCart`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Products'
4. Hover over the first product and click 'Add to cart'
5. Click 'Continue Shopping'
6. Hover over the second product and click 'Add to cart'
7. Click 'View Cart'
8. Verify both products are in the cart
9. Verify their prices, quantities, and total price

---

### TC13 — Verify Product quantity in Cart
**Class:** `CartTests` · **Method:** `verifyProductQuantityInCart`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'View Product' for any product on the home page
4. Verify the product detail page is open
5. Increase quantity to 4
6. Click 'Add to cart'
7. Click 'View Cart'
8. Verify the product appears in the cart with quantity 4

---

### TC14 — Place Order: Register while Checkout
**Class:** `CheckoutTests` · **Method:** `placeOrderRegisterDuringCheckout`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Add a product to the cart
4. Click 'Cart'
5. Verify cart page is displayed
6. Click 'Proceed To Checkout'
7. Click 'Register / Login'
8. Fill all signup details and create account
9. Verify 'ACCOUNT CREATED!' and click 'Continue'
10. Verify 'Logged in as username' in the header
11. Click 'Cart'
12. Click 'Proceed To Checkout'
13. Verify Address Details and Review Your Order
14. Enter a comment and click 'Place Order'
15. Enter payment details: Name on Card, Card Number, CVC, Expiration date
16. Click 'Pay and Confirm Order'
17. Verify 'Your order has been placed successfully!'
18. Click 'Delete Account'
19. Verify 'ACCOUNT DELETED!' and click 'Continue'

---

### TC15 — Place Order: Register before Checkout
**Class:** `CheckoutTests` · **Method:** `placeOrderRegisterBeforeCheckout`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login' and create a new account
4. Verify 'ACCOUNT CREATED!' and click 'Continue'
5. Verify 'Logged in as username' in the header
6. Add a product to the cart
7. Click 'Cart'
8. Verify cart page is displayed
9. Click 'Proceed To Checkout'
10. Verify Address Details and Review Your Order
11. Enter a comment and click 'Place Order'
12. Enter payment details and click 'Pay and Confirm Order'
13. Verify 'Your order has been placed successfully!'
14. Click 'Delete Account'
15. Verify 'ACCOUNT DELETED!' and click 'Continue'

---

### TC16 — Place Order: Login before Checkout
**Class:** `CheckoutTests` · **Method:** `placeOrderLoginBeforeCheckout`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login', enter email and password, click 'Login'
4. Verify 'Logged in as username' in the header
5. Add a product to the cart
6. Click 'Cart'
7. Verify cart page is displayed
8. Click 'Proceed To Checkout'
9. Verify Address Details and Review Your Order
10. Enter a comment and click 'Place Order'
11. Enter payment details and click 'Pay and Confirm Order'
12. Verify 'Your order has been placed successfully!'
13. Click 'Delete Account'
14. Verify 'ACCOUNT DELETED!' and click 'Continue'

---

### TC17 — Remove Products From Cart
**Class:** `CartTests` · **Method:** `removeProductFromCart`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Add a product to the cart
4. Click 'Cart'
5. Verify cart page is displayed
6. Click the 'X' button for a specific product
7. Verify the product is removed from the cart

---

### TC18 — View Category Products
**Class:** `CategoryBrandTests` · **Method:** `viewCategoryProducts`

1. Navigate to `https://automationexercise.com`
2. Verify categories are visible in the left sidebar
3. Click on 'Women' category
4. Click on a sub-category link under 'Women' (e.g. Dress)
5. Verify the category page is displayed and the page heading contains the category name
6. Click on any sub-category under 'Men' in the left sidebar
7. Verify user is navigated to that Men category page

---

### TC19 — View & Cart Brand Products
**Class:** `CategoryBrandTests` · **Method:** `viewBrandProducts`

1. Navigate to `https://automationexercise.com`
2. Click 'Products'
3. Verify Brands are visible in the left sidebar
4. Click on any brand name
5. Verify user is on the brand page and brand products are displayed
6. Click on a different brand in the left sidebar
7. Verify user is navigated to that brand page and products are visible

---

### TC20 — Search Products and Verify Cart After Login
**Class:** `ProductTests` · **Method:** `searchProductsAndVerifyCartAfterLogin`

1. Navigate to `https://automationexercise.com`
2. Click 'Products'
3. Verify ALL PRODUCTS page is displayed
4. Enter a product name in search and click Search
5. Verify 'SEARCHED PRODUCTS' is visible
6. Verify all matching products are visible
7. Add those products to the cart
8. Click 'Cart' and verify products are visible
9. Click 'Signup / Login' and log in
10. Go to Cart again
11. Verify the same products are still in the cart after login

---

### TC21 — Add review on product
**Class:** `ProductTests` · **Method:** `addReviewOnProduct`

1. Navigate to `https://automationexercise.com`
2. Click 'Products'
3. Verify ALL PRODUCTS page is displayed
4. Click 'View Product' on any product
5. Verify 'Write Your Review' section is visible
6. Enter name, email, and review text
7. Click 'Submit'
8. Verify success message 'Thank you for your review.'

---

### TC22 — Add to cart from Recommended items
**Class:** `CartTests` · **Method:** `addToCartFromRecommendedItems`

1. Navigate to `https://automationexercise.com`
2. Scroll to the bottom of the home page
3. Verify 'RECOMMENDED ITEMS' section is visible
4. Click 'Add To Cart' on a recommended product
5. Click 'View Cart'
6. Verify the product is displayed in the cart

---

### TC23 — Verify address details in checkout page
**Class:** `CheckoutTests` · **Method:** `verifyAddressDetailsAtCheckout`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Click 'Signup / Login' and create a new account (record the address used)
4. Verify 'ACCOUNT CREATED!' and click 'Continue'
5. Verify 'Logged in as username' in the header
6. Add a product to the cart
7. Click 'Cart'
8. Verify cart page is displayed
9. Click 'Proceed To Checkout'
10. Verify the delivery address matches the address entered at registration
11. Verify the billing address matches the address entered at registration
12. Click 'Delete Account'
13. Verify 'ACCOUNT DELETED!' and click 'Continue'

---

### TC24 — Download Invoice after purchase order
**Class:** `CheckoutTests` · **Method:** `downloadInvoiceAfterOrder`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Add a product to the cart
4. Click 'Cart' and verify cart page
5. Click 'Proceed To Checkout'
6. Click 'Register / Login' and create account
7. Verify 'ACCOUNT CREATED!' and click 'Continue'
8. Verify 'Logged in as username'
9. Click 'Cart' → 'Proceed To Checkout'
10. Verify Address Details and Review Your Order
11. Enter a comment and click 'Place Order'
12. Enter payment details and click 'Pay and Confirm Order'
13. Verify 'Your order has been placed successfully!'
14. Click 'Download Invoice' and verify the invoice downloads successfully
15. Click 'Continue'
16. Click 'Delete Account'
17. Verify 'ACCOUNT DELETED!' and click 'Continue'

---

### TC25 — Verify Scroll Up using 'Arrow' button and Scroll Down
**Class:** `ScrollTests` · **Method:** `scrollUpWithArrowButton`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Scroll down to the bottom of the page
4. Verify 'SUBSCRIPTION' text is visible in the footer
5. Click the arrow button at the bottom-right to scroll back to the top
6. Verify the page has scrolled up and 'Full-Fledged practice website for Automation Engineers' text is visible

---

### TC26 — Verify Scroll Up without 'Arrow' button and Scroll Down
**Class:** `ScrollTests` · **Method:** `scrollUpWithoutArrowButton`

1. Navigate to `https://automationexercise.com`
2. Verify home page is visible
3. Scroll down to the bottom of the page
4. Verify 'SUBSCRIPTION' text is visible in the footer
5. Scroll up to the top of the page (without using the arrow button)
6. Verify the page has scrolled up and 'Full-Fledged practice website for Automation Engineers' text is visible

---

## Registering Tests in testng.xml

All test classes must be registered in `testng-suites/testng.xml` before Maven
will execute them. Here is the complete, ready-to-use suite file covering all 26
test cases:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">

<suite name="AutomationExercise — 26 Test Cases" verbose="1">

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

### Running a single test class

Comment out the blocks you do not need:

```xml
<!--
<test name="Cart Tests">
    <classes>
        <class name="tests.CartTests"/>
    </classes>
</test>
-->
```

### Running specific methods only

```xml
<test name="Registration and Login Tests">
    <classes>
        <class name="tests.RegisterLoginTests">
            <methods>
                <include name="registerNewUser"/>
                <include name="loginWithValidCredentials"/>
            </methods>
        </class>
    </classes>
</test>
```

### Running in parallel (methods)

Each `@Test` method gets its own browser via `@BeforeMethod`, so parallel-by-methods
is safe without any code changes:

```xml
<suite name="AutomationExercise — 26 Test Cases" verbose="1"
       parallel="methods" thread-count="4">
    ...
</suite>
```

---

## Running the Tests

```bash
# Run all 26 test cases
mvn test

# Run a single test class
mvn test -Dtest=CartTests

# Run a single test method
mvn test -Dtest=CartTests#addTwoProductsToCart

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
├── TC01_AfterRegistration_2025-07-21_14-35-22-123.png
└── FAILED_loginWithInvalidCredentials_2025-07-21_14-36-01-456.png
```

---

## Framework API Quick Reference

All interactions go through `driver` (inherited from `BasePage` or accessed via
`getDriver()` in tests). Never add `Thread.sleep()` — all waits are handled internally.

```java
// Navigation
driver.goToURL("https://automationexercise.com/login");
driver.manageNavigationButtons("back");        // "back" | "forward" | "refresh"
driver.getCurrentPageTitle();
driver.getCurrentPageURL();
driver.manageScreenSize("maximize");

// Finding Elements
WebElement el = driver.findElement("css", "[data-qa='login-email']");
By         by = driver.getBy("id", "submit-btn");

// Interactions
driver.clickElement(locator);
driver.writeInElement(locator, "text");        // clears first, then types
driver.getElementText(locator);
driver.clearElementText(locator);

// State Checks
driver.validateElementIsDisplayed(element);   // returns boolean
driver.validateElementIsEnabled(element);
driver.validateElementIsSelected(element);

// Dropdowns
driver.selectFromDropDownMenu(locator, "visible", "Egypt");
driver.selectFromDropDownMenu(locator, "value",   "eg");
driver.selectFromDropDownMenu(locator, "index",   "2");

// Advanced Interactions
driver.hoverOverElement(locator);
driver.doubleClick(locator);
driver.rightClick(locator);
driver.scrollToElement(locator);              // centers element in viewport
driver.dragAndDrop(sourceLocator, targetLocator);

// Checkboxes & Radio Buttons
driver.checkCheckbox(locator);
driver.uncheckCheckbox(locator);
driver.selectRadioButton(locator);

// Waits (explicit)
driver.setExplicitWait(locator, 15);
driver.setFluentWait(locator, 10, 500, "Timeout message");

// Alerts
driver.acceptAlert();
driver.dismissAlert();
driver.getAlertText();
driver.typeInAlert("text");

// iFrames
driver.switchToIFrame(locator);
driver.switchToDefaultContent();

// Windows & Tabs
String main = driver.getCurrentWindowHandle();
driver.switchToNewWindow(main);
driver.closeCurrentWindowAndSwitchTo(main);

// Screenshots (saved to disk + attached to Allure)
loginPage.saveScreenshot("TC01_AfterLogin", getDriver());
```

---

## Author

**ASMahrous** — EDGES Software Testing Diploma · Web Automation Final Project

Website under test: [automationexercise.com](https://automationexercise.com) ·
Test cases spec: [automationexercise.com/test_cases](https://automationexercise.com/test_cases)
