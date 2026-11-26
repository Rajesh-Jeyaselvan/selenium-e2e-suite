# Selenium E2E Suite

A modular, thread-safe UI test automation framework built with **Java 17**, **Selenium 4**, **TestNG**, and **Maven** following the **Page Object Model (POM)** design pattern. Automated end-to-end against **[SauceDemo](https://www.saucedemo.com)**.

---

## Tech Stack
- **Language:** Java 17
- **Browser Automation:** Selenium Java `4.22.0` (with built-in Selenium Manager)
- **Test Runner:** TestNG `7.10.2`
- **Reporting:** Allure Reports (`allure-testng` + `allure-maven`)
- **Build Tool:** Maven (Surefire `3.2.5`)
- **Logging:** SLF4J Simple

---

## Key Features
- **Thread-Safe Parallel Execution:** Uses `ThreadLocal<WebDriver>` in `DriverFactory` with TestNG `parallel="methods"` (`thread-count="2"`).
- **Cross-Browser & Headless Support:** Supports `chrome`, `edge`, and `firefox`, configurable via `config.properties`, `testng.xml` parameters, or Maven CLI flags (`-Dbrowser=... -Dheadless=true`).
- **Centralized Explicit Waits:** `BasePage` encapsulates `WebDriverWait` (visibility, clickability, dropdown selection, URL checks) with `implicitlyWait` set to `0` to keep wait behavior deterministic.
- **Fluent Page Objects:** Page actions return the next Page Object (e.g., `LoginPage.loginAs(...)` → `InventoryPage` → `CartPage` → `CheckoutPage`) for readable E2E flows.
- **Data-Driven Testing:** Uses TestNG `@DataProvider` (with `parallel = true`) for locked-out, invalid, and missing-credential login scenarios.
- **Automatic Failure Screenshots & Allure Reporting:** `TestListener` (`ITestListener`, `ISuiteListener`) clears previous `target/allure-results/` at the start of every suite run, logs lifecycle events, saves PNG screenshots to `target/screenshots/`, and attaches failure screenshots directly to the Allure report.

---

## Automated Scenarios
- **Login (`LoginTest`):** Valid login, locked-out/invalid/empty-username login (`@DataProvider`), and sidebar logout.
- **Inventory (`InventoryTest`):** Product sorting by `Price (low to high)` with ascending price verification.
- **Cart (`CartTest`):** Adding multiple products, verifying cart badge count, and removing an item from the cart.
- **Checkout (`CheckoutTest`):** Full E2E order placement (`Thank you for your order!`) and missing customer field validation.

---

## Project Structure
```text
├── pom.xml
├── testng.xml
└── src
    ├── main/java/org/example
    │   ├── factory/
    │   │   └── DriverFactory.java      # ThreadLocal WebDriver & browser/headless setup
    │   ├── pages/
    │   │   ├── BasePage.java           # Explicit waits & reusable UI actions
    │   │   ├── LoginPage.java          # Login page locators & fluent actions
    │   │   ├── InventoryPage.java      # Product catalog, sorting, cart badge & logout
    │   │   ├── CartPage.java           # Cart verification, removal & checkout navigation
    │   │   └── CheckoutPage.java       # Customer info, order finish & confirmation
    │   └── utils/
    │       ├── ConfigReader.java       # Properties loader with -D system overrides
    │       └── ScreenshotUtils.java    # Failure screenshot capture to target/screenshots/
    └── test
        ├── java/org/example
        │   ├── base/
        │   │   └── BaseTest.java       # @BeforeMethod / @AfterMethod lifecycle
        │   ├── listeners/
        │   │   └── TestListener.java   # TestNG listener for logging & screenshots
        │   └── tests/
        │       ├── LoginTest.java      # Auth & logout scenarios
        │       ├── InventoryTest.java  # Product sorting scenario
        │       ├── CartTest.java       # Cart add/remove scenario
        │       └── CheckoutTest.java   # E2E checkout & validation scenarios
        └── resources/
            └── config.properties       # Default baseUrl, browser, headless, timeout
```

---

## How to Run

### Run with Default Config (`config.properties`)
```bash
mvn test
```

### Run Headless or Override Browser via CLI
```bash
# Chrome headless
mvn test -Dbrowser=chrome -Dheadless=true

# Edge headless
mvn test -Dbrowser=edge -Dheadless=true

# Firefox
mvn test -Dbrowser=firefox
```

### Run from IntelliJ IDEA
Right-click `testng.xml` or any class under `src/test/java/org/example/tests/` and select **Run**.

### Generate & View Allure Report
Previous results in `target/allure-results/` are automatically cleared at the start of each run:
```bash
# Generate static HTML report in target/allure-report/
mvn allure:report

# Generate and open interactive report in your browser
mvn allure:serve
```

---

## Architecture & Interview Highlights
- **Why `ThreadLocal<WebDriver>`?** Isolates each test thread's `WebDriver` instance so parallel TestNG methods never collide on the same browser session.
- **Why `implicitlyWait(Duration.ZERO)` with `WebDriverWait`?** Mixing implicit and explicit waits causes unpredictable wait times; keeping implicit wait at `0` makes explicit wait timeouts deterministic.
- **Why `ITestListener` for screenshots?** Decouples failure handling from test assertions—any test failure automatically captures a timestamped screenshot without duplicating `try/catch` blocks in tests.
- **Why system-property overrides in `ConfigReader`?** Allows CI/CD pipelines (GitHub Actions, Jenkins) to switch browsers or enable headless mode via `-D` flags without modifying tracked config files.
