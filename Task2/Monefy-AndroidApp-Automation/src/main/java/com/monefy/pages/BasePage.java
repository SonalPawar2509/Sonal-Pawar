package com.monefy.pages;

import com.monefy.utils.DriverManager;
import com.monefy.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.AppiumBy;

/**
 * Base class for all page objects
 * Implements common methods and provides consistent error handling
 */
public class BasePage {
    
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_INTERVAL = 1000; // milliseconds

    /**
     * Constructor initializes page elements with AppiumFieldDecorator
     */
    public BasePage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
    }

    /**
     * Click on an element with retries
     * @param element The element to click
     */
    protected void click(WebElement element) {
        try {
            TestUtils.waitForElementToBeClickable(element);
            element.click();
        } catch (Exception e) {
            TestUtils.log("WARN: First click attempt failed, retrying: " + e.getMessage());
            retryClick(element);
        }
    }
    
    /**
     * Retry clicking on an element
     * @param element The element to click
     */
    private void retryClick(WebElement element) {
        boolean success = TestUtils.retry(() -> {
            TestUtils.waitForElementToBeClickable(element);
            element.click();
        }, MAX_RETRIES, RETRY_INTERVAL);
        
        if (!success) {
            throw new RuntimeException("Failed to click element after " + MAX_RETRIES + " attempts");
        }
    }

    /**
     * Enter text in an element
     * @param element The element to enter text in
     * @param text The text to enter
     */
    protected void sendKeys(WebElement element, String text) {
        try {
            TestUtils.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            TestUtils.log("WARN: First sendKeys attempt failed, retrying: " + e.getMessage());
            retrySendKeys(element, text);
        }
    }
    
    /**
     * Retry entering text in an element
     * @param element The element to enter text in
     * @param text The text to enter
     */
    private void retrySendKeys(WebElement element, String text) {
        boolean success = TestUtils.retry(() -> {
            TestUtils.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
        }, MAX_RETRIES, RETRY_INTERVAL);
        
        if (!success) {
            throw new RuntimeException("Failed to enter text after " + MAX_RETRIES + " attempts");
        }
    }

    /**
     * Get text from an element
     * @param element The element to get text from
     * @return The text of the element
     */
    protected String getText(WebElement element) {
        try {
            TestUtils.waitForElementToBeVisible(element);
            return element.getText();
        } catch (Exception e) {
            TestUtils.log("WARN: First getText attempt failed, retrying: " + e.getMessage());
            return retryGetText(element);
        }
    }
    
    /**
     * Retry getting text from an element
     * @param element The element to get text from
     * @return The text of the element
     */
    private String retryGetText(WebElement element) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                TestUtils.waitForElementToBeVisible(element);
                return element.getText();
            } catch (Exception e) {
                TestUtils.log("WARN: getText retry " + (i+1) + " failed: " + e.getMessage());
                TestUtils.sleep(RETRY_INTERVAL);
            }
        }
        throw new RuntimeException("Failed to get text after " + MAX_RETRIES + " attempts");
    }

    /**
     * Check if an element is displayed
     * @param element The element to check
     * @return true if the element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Wait for an element to be displayed with timeout
     * @param element The element to wait for
     * @param timeoutInSeconds Maximum time to wait in seconds
     * @return true if the element is displayed within the timeout, false otherwise
     */
    protected boolean waitForElementDisplayed(WebElement element, int timeoutInSeconds) {
        try {
            WebElement foundElement = TestUtils.fluentWaitForElement(
                By.xpath(element.toString().split("->")[1].trim().replace("]", "")));
            return foundElement != null && foundElement.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    /**
     * Find element by text using UiAutomator
     * @param text The text to search for
     * @return The WebElement found or null if not found
     */
    protected WebElement findElementByText(String text) {
        return TestUtils.findElementByText(text);
    }
    
    /**
     * Safely click on an element found by text
     * @param text The text to search for
     * @return true if click succeeded, false otherwise
     */
    protected boolean clickElementByText(String text) {
        WebElement element = findElementByText(text);
        if (element != null) {
            return TestUtils.safeClick(element, MAX_RETRIES);
        }
        return false;
    }
    
    /**
     * Take a screenshot with the page name
     * @param action The action being performed
     * @return Path to the saved screenshot
     */
    protected String takeScreenshot(String action) {
        String screenshotName = this.getClass().getSimpleName() + "_" + action;
        return TestUtils.takeScreenshot(screenshotName);
    }
    
    /**
     * Navigate back to previous screen using device back button
     * @return true if navigation was successful, false otherwise
     */
    public boolean navigateBack() {
        try {
            DriverManager.getDriver().navigate().back();
            TestUtils.sleep(1000); // Brief pause to allow navigation to complete
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Navigate back to previous screen using device back button with wait time
     * @param waitTimeMs Time to wait after navigation in milliseconds
     * @return true if navigation was successful, false otherwise
     */
    public boolean navigateBack(long waitTimeMs) {
        try {
            DriverManager.getDriver().navigate().back();
            TestUtils.sleep(waitTimeMs); // Custom wait time
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}