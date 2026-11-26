package org.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility for capturing PNG screenshots on test failures.
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    private ScreenshotUtils() {
    }

    /**
     * Captures a screenshot and saves it under {@code target/screenshots/}.
     *
     * @return the saved file path, or {@code null} if capture failed.
     */
    public static Path captureScreenshot(WebDriver driver, String testName) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            log.warn("WebDriver does not support screenshots for test: {}", testName);
            return null;
        }

        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = safeName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".png";
            Path destination = SCREENSHOT_DIR.resolve(fileName);

            Path source = screenshotDriver.getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("Saved failure screenshot: {}", destination.toAbsolutePath());
            return destination;
        } catch (IOException e) {
            log.error("Failed to save screenshot for test: {}", testName, e);
            return null;
        }
    }
}
