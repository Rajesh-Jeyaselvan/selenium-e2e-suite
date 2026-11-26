package org.example.factory;

import org.example.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Thread-safe WebDriver factory using {@link ThreadLocal} to support parallel TestNG execution.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    /**
     * Initializes a WebDriver instance for the current thread.
     * Supports {@code chrome}, {@code edge}, and {@code firefox}, plus headless mode via config or {@code -Dheadless=true}.
     */
    public static void initDriver(String browser) {
        String targetBrowser = (browser == null || browser.isBlank()) ? "chrome" : browser.trim().toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless", false);

        log.info("Initializing {} driver (headless={}) on thread {}", targetBrowser, headless, Thread.currentThread().getId());

        WebDriver driver = switch (targetBrowser) {
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--window-size=1920,1080");
                }
                yield new EdgeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                yield new FirefoxDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--window-size=1920,1080");
                }
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + targetBrowser);
        };

        // Keep implicit wait at 0 so explicit WebDriverWait durations remain predictable.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if (!headless) {
            driver.manage().window().maximize();
        }

        TL_DRIVER.set(driver);
    }

    /**
     * Returns the WebDriver instance bound to the current thread.
     */
    public static WebDriver getDriver() {
        return TL_DRIVER.get();
    }

    /**
     * Quits the WebDriver instance and clears the {@link ThreadLocal} reference.
     */
    public static void quitDriver() {
        WebDriver driver = TL_DRIVER.get();
        if (driver != null) {
            log.info("Quitting driver on thread {}", Thread.currentThread().getId());
            driver.quit();
            TL_DRIVER.remove();
        }
    }
}
