package com.monefy.utils;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumBy;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Utility class providing common test functionality
 */
public class TestUtils {
    private static final int DEFAULT_WAIT = 20;
    private static final int FLUENT_WAIT = 30;
    private static final int POLLING_INTERVAL = 500; // milliseconds
    private static final String SCREENSHOT_DIR = "screenshots";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
    
    static {
        // Ensure screenshot directory exists
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
    }

    /**
     * Wait for an element to be visible
     * @param element The element to wait for
     */
    public static void waitForElementToBeVisible(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            log("WARN: Element not visible after waiting: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for an element to be clickable
     * @param element The element to wait for
     */
    public static void waitForElementToBeClickable(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            log("WARN: Element not clickable after waiting: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for an element to be located using a By locator
     * @param locator The locator to find the element
     * @return The located WebElement
     */
    public static WebElement waitForElement(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            log("WARN: Element not found after waiting: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for an element with retry mechanism using FluentWait
     * @param locator The locator to find the element
     * @return The located WebElement or null if not found
     */
    public static WebElement fluentWaitForElement(By locator) {
        try {
            FluentWait<AndroidDriver> wait = new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(FLUENT_WAIT))
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
                
            return wait.until(driver -> driver.findElement(locator));
        } catch (TimeoutException e) {
            log("WARN: Element not found after fluent waiting: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Try to find an element by text using UiAutomator
     * @param text The text to search for
     * @return The located WebElement or null if not found
     */
    public static WebElement findElementByText(String text) {
        try {
            return DriverManager.getDriver().findElement(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"" + text + "\")"));
        } catch (Exception e) {
            log("WARN: Element with text '" + text + "' not found: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Try to click an element safely with retries
     * @param element The element to click
     * @param retries Number of retries
     * @return true if click succeeded, false otherwise
     */
    public static boolean safeClick(WebElement element, int retries) {
        Exception lastException = null;
        
        for (int i = 0; i < retries; i++) {
            try {
                waitForElementToBeClickable(element);
                element.click();
                return true;
            } catch (Exception e) {
                lastException = e;
                log("WARN: Click attempt " + (i+1) + " failed: " + e.getMessage());
                sleep(1000);
            }
        }
        
        if (lastException != null) {
            log("ERROR: All click attempts failed. Last error: " + lastException.getMessage());
        }
        return false;
    }

    /**
     * Sleep for the specified amount of time
     * @param millis Time to sleep in milliseconds
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("WARN: Sleep interrupted: " + e.getMessage());
        }
    }

    /**
     * Take a screenshot with timestamp
     * @param testName Name of the test for the screenshot
     * @return Path to the saved screenshot
     */
    public static String takeScreenshot(String testName) {
        try {
            File file = ((AndroidDriver) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            String timestamp = DATE_FORMAT.format(new Date());
            String destPath = SCREENSHOT_DIR + "/" + testName + "_" + timestamp + ".png";
            File destination = new File(destPath);
            FileUtils.copyFile(file, destination);
            log("Screenshot saved to: " + destPath);
            return destPath;
        } catch (IOException e) {
            log("ERROR: Failed to take screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            log("ERROR: Failed to take screenshot due to driver issue: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Log a message with timestamp
     * @param message The message to log
     */
    public static void log(String message) {
        String timestamp = DATE_FORMAT.format(new Date());
        System.out.println("[" + timestamp + "] " + message);
    }
    
    /**
     * Create a directory if it doesn't exist
     * @param dirPath Path to the directory
     * @return true if directory exists or was created, false otherwise
     */
    public static boolean createDirectoryIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                log("Created directory: " + dirPath);
                return true;
            } else {
                log("ERROR: Failed to create directory: " + dirPath);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retry an operation with a specified number of attempts
     * @param operation The operation to retry as a functional interface
     * @param retries Number of retry attempts
     * @param sleepBetweenRetries Time to sleep between retries in milliseconds
     * @return true if operation succeeded, false if all retries failed
     */
    public static boolean retry(Runnable operation, int retries, long sleepBetweenRetries) {
        for (int i = 0; i < retries; i++) {
            try {
                operation.run();
                return true;
            } catch (Exception e) {
                log("WARN: Retry attempt " + (i+1) + " failed: " + e.getMessage());
                if (i < retries - 1) {
                    sleep(sleepBetweenRetries);
                }
            }
        }
        return false;
    }
}