package com.monefy.pages;

import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import com.monefy.utils.DriverManager;
import com.monefy.utils.Constants;
import com.monefy.utils.TestUtils;
import org.openqa.selenium.NoSuchElementException;

/**
 * Page object for the onboarding flow screens
 * Handles all interactions with the onboarding process
 */
public class OnboardingPage extends BasePage {

    @AndroidFindBy(id = "com.monefy.app.lite:id/buttonClose")
    private WebElement offerCloseButton;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/income_button")
    private WebElement incomeButton;

    /**
     * Click on GET STARTED button on first onboarding screen
     */
    public void clickGetStartedButton() {
        try {
            // Try multiple locator strategies for better reliability
            WebElement getStartedButton = findElementWithRetry(
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//*[@text='" + Constants.GET_STARTED_TEXT + "']")),
                () -> DriverManager.getDriver().findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"STARTED\")")),
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//android.widget.Button[contains(@text, 'STARTED')]"))
            );
            click(getStartedButton);
        } catch (Exception e) {
            TestUtils.log("WARN: Error clicking GET STARTED button: " + e.getMessage());
            TestUtils.takeScreenshot("getstarted_button_error");
            throw e;
        }
    }

    /**
     * Click on AMAZING button on second onboarding screen
     */
    public void clickAmazingButton() {
        try {
            WebElement amazingButton = findElementWithRetry(
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//*[@text='" + Constants.AMAZING_TEXT + "']")),
                () -> DriverManager.getDriver().findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"AMAZING\")")),
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//android.widget.Button[contains(@text, 'AMAZING')]"))
            );
            click(amazingButton);
        } catch (Exception e) {
            TestUtils.log("WARN: Error clicking AMAZING button: " + e.getMessage());
            TestUtils.takeScreenshot("amazing_button_error");
            throw e;
        }
    }

    /**
     * Click on YES, PLEASE! button on third onboarding screen
     */
    public void clickYesPleaseButton() {
        try {
            WebElement yesPleaseButton = findElementWithRetry(
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//*[@text='" + Constants.YES_PLEASE_TEXT + "']")),
                () -> DriverManager.getDriver().findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"YES\")")),
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//android.widget.Button[contains(@text, 'YES')]"))
            );
            click(yesPleaseButton);
        } catch (Exception e) {
            TestUtils.log("WARN: Error clicking YES, PLEASE! button: " + e.getMessage());
            TestUtils.takeScreenshot("yes_please_button_error");
            throw e;
        }
    }

    /**
     * Click on I'M READY button on final onboarding screen
     */
    public void clickImReadyButton() {
        try {
            WebElement imReadyButton = findElementWithRetry(
                () -> DriverManager.getDriver().findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"READY\")")),
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//android.widget.Button[contains(@text, 'READY')]")),
                () -> findElementByText("READY")
            );
            click(imReadyButton);
        } catch (Exception e) {
            TestUtils.log("WARN: Error clicking I'M READY button: " + e.getMessage());
            TestUtils.takeScreenshot("im_ready_button_error");
            throw e;
        }
    }

    /**
     * Close the promotional offer screen
     */
    public void closeOfferScreen() {
        try {
            // First try using the @AndroidFindBy element
            if (isElementDisplayed(offerCloseButton)) {
                click(offerCloseButton);
                return;
            }
            
            // Then try other strategies
            WebElement closeButton = findElementWithRetry(
                () -> DriverManager.getDriver().findElement(AppiumBy.className("android.widget.ImageButton")),
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//android.widget.ImageButton[contains(@resource-id, 'close')]")),
                () -> DriverManager.getDriver().findElement(AppiumBy.xpath("//android.widget.ImageButton[contains(@resource-id, 'cancel')]"))
            );
            click(closeButton);
        } catch (Exception e) {
            TestUtils.log("WARN: Error closing offer screen: " + e.getMessage());
            TestUtils.takeScreenshot("offer_close_error");
            // Don't throw here as this is often optional
        }
    }

    /**
     * Check if home page is displayed
     * @return true if home page is displayed, false otherwise
     */
    public boolean isHomePageDisplayed() {
        try {
            // First try using the @AndroidFindBy element
            if (isElementDisplayed(incomeButton)) {
                return true;
            }
            
            // Try looking for income text
            WebElement incomeText = DriverManager.getDriver().findElement(
                AppiumBy.xpath("//*[@text='" + Constants.INCOME_TEXT + "']"));
            return incomeText.isDisplayed();
        } catch (Exception e) {
            TestUtils.log("WARN: Home page check failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Complete the entire onboarding flow
     * @return true if onboarding completed successfully, false otherwise
     */
    public boolean completeOnboarding() {
        try {
            // Take screenshot at the start
            TestUtils.takeScreenshot("onboarding_start");
            
            // Step 1: Click GET STARTED
            clickGetStartedButton();
            
            // Step 2: Click AMAZING
            clickAmazingButton();
            
            // Step 3: Click YES, PLEASE!
            clickYesPleaseButton();
            
            // Step 4: Click I'M READY
            clickImReadyButton();
            
            // Step 5: Close offer screen (wait is handled implicitly by isHomePageDisplayed)
            closeOfferScreen();
            
            // Verify we reached home page
            boolean success = isHomePageDisplayed();
            
            // Take screenshot at the end
            TestUtils.takeScreenshot(success ? "onboarding_complete_success" : "onboarding_complete_failure");
            
            return success;
        } catch (Exception e) {
            TestUtils.log("ERROR: Complete onboarding flow failed: " + e.getMessage());
            TestUtils.takeScreenshot("onboarding_complete_error");
            return false;
        }
    }
    
    /**
     * Helper method to find an element using multiple strategies
     * @param strategies Element finding functions to try in order
     * @return The found WebElement
     * @throws NoSuchElementException if no strategy works
     */
    @SafeVarargs
    private WebElement findElementWithRetry(java.util.function.Supplier<WebElement>... strategies) {
        for (java.util.function.Supplier<WebElement> strategy : strategies) {
            try {
                return strategy.get();
            } catch (Exception e) {
                // Try next strategy
            }
        }
        throw new NoSuchElementException("Element not found using any of the provided strategies");
    }
}