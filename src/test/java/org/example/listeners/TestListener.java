package org.example.listeners;

import io.qameta.allure.Allure;
import org.example.factory.DriverFactory;
import org.example.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * TestNG listener that cleans previous Allure results per suite run,
 * logs test lifecycle events, and captures/attaches failure screenshots.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);
    private static final Path ALLURE_RESULTS_DIR = Paths.get("target", "allure-results");

    @Override
    public void onStart(ISuite suite) {
        cleanDirectory(ALLURE_RESULTS_DIR);
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("START: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("PASS: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("FAIL: {}", testName, result.getThrowable());

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            Path screenshotPath = ScreenshotUtils.captureScreenshot(driver, testName);
            if (screenshotPath != null) {
                try {
                    byte[] bytes = Files.readAllBytes(screenshotPath);
                    Allure.addAttachment("Failure Screenshot - " + testName, "image/png", new ByteArrayInputStream(bytes), ".png");
                } catch (IOException e) {
                    log.warn("Could not attach screenshot to Allure report for {}", testName, e);
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("SKIP: {}", result.getMethod().getMethodName());
    }

    private void cleanDirectory(Path dir) {
        if (!Files.exists(dir)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder())
                    .filter(p -> !p.equals(dir))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            log.warn("Failed to delete old Allure file: {}", p, e);
                        }
                    });
            log.info("Cleaned previous Allure results in {}", dir);
        } catch (IOException e) {
            log.warn("Failed to clean Allure results directory: {}", dir, e);
        }
    }
}
