package org.example.base;

import org.example.factory.DriverFactory;
import org.example.utils.ConfigReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base test class handling per-test WebDriver initialization and teardown.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browserParam) {
        // Prefer JVM -Dbrowser, then TestNG parameter, then config.properties.
        String browser = System.getProperty("browser");
        if (browser == null || browser.isBlank()) {
            browser = (browserParam != null && !browserParam.isBlank())
                    ? browserParam
                    : ConfigReader.get("browser", "chrome");
        }

        DriverFactory.initDriver(browser);
        DriverFactory.getDriver().get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
