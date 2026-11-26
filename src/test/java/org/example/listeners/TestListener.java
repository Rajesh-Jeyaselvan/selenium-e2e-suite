package org.example.listeners;

import org.example.factory.DriverFactory;
import org.example.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that logs test lifecycle events and captures screenshots on failure.
 */
public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

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
            ScreenshotUtils.captureScreenshot(driver, testName);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("SKIP: {}", result.getMethod().getMethodName());
    }
}
