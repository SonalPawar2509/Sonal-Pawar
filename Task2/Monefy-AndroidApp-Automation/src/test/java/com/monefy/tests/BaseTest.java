package com.monefy.tests;

import com.monefy.utils.DriverManager;
import com.monefy.utils.TestUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Base test class that all test classes should extend
 * Handles setup, teardown, and common test functionality
 */
@Listeners(com.monefy.utils.Listeners.class)
public class BaseTest {
    
    protected static final Map<String, Boolean> onboardingCompletedMap = new HashMap<>();
    
    @BeforeSuite
    public void suiteSetUp() {
        TestUtils.log("========== TEST SUITE SETUP ==========");
        TestUtils.createDirectoryIfNotExists("screenshots");
        TestUtils.createDirectoryIfNotExists("logs");
    }
    
    @BeforeTest
    public void setUp() {
        TestUtils.log("========== INITIALIZING DRIVER ==========");
        DriverManager.initializeDriver();
    }
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        TestUtils.log("========== STARTING TEST: " + method.getName() + " ==========");
        TestUtils.takeScreenshot("start_" + method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        TestUtils.log("========== ENDING TEST: " + result.getMethod().getMethodName() + " ==========");
        
        // Take a screenshot at the end of each test
        String status = result.isSuccess() ? "success" : "failure";
        TestUtils.takeScreenshot(status + "_" + result.getMethod().getMethodName());
        
        // Log test result
        if (result.isSuccess()) {
            TestUtils.log("Test PASSED: " + result.getMethod().getMethodName());
        } else {
            TestUtils.log("Test FAILED: " + result.getMethod().getMethodName());
            
            // Log exception if any
            if (result.getThrowable() != null) {
                TestUtils.log("Exception: " + result.getThrowable().getMessage());
            }
        }
    }
    
    @AfterTest
    public void tearDown() {
        TestUtils.log("========== QUITTING DRIVER ==========");
        DriverManager.quitDriver();
    }
    
    @AfterSuite
    public void suiteTearDown() {
        TestUtils.log("========== TEST SUITE TEARDOWN ==========");
        // Ensure driver is quit
        if (DriverManager.getDriver() != null) {
            DriverManager.quitDriver();
        }
    }
    
    /**
     * Mark onboarding as completed for a given test class
     * @param testClassName The name of the test class
     */
    protected void markOnboardingCompleted(String testClassName) {
        onboardingCompletedMap.put(testClassName, true);
        TestUtils.log("Marked onboarding as completed for: " + testClassName);
    }
    
    /**
     * Check if onboarding is completed for a given test class
     * @param testClassName The name of the test class
     * @return true if onboarding is completed, false otherwise
     */
    protected boolean isOnboardingCompleted(String testClassName) {
        Boolean completed = onboardingCompletedMap.get(testClassName);
        return completed != null && completed;
    }
    
    /**
     * Reset the app to its initial state
     * This can be useful between tests that need a fresh app state
     * @deprecated Use BasePage.navigateBack() for navigation instead. 
     * App resets should be avoided where possible as they impact test performance
     */
    @Deprecated
    protected void resetApp() {
        TestUtils.log("Resetting app");
        try {
            if (DriverManager.getDriver() != null) {
                DriverManager.getDriver().resetApp();
                TestUtils.log("App reset successful");
            }
        } catch (Exception e) {
            TestUtils.log("ERROR: App reset failed: " + e.getMessage());
        }
    }
    
    /**
     * Restart the app
     * This can be useful to refresh the app state between tests
     * @deprecated Use BasePage.navigateBack() for navigation instead.
     * App restarts should be avoided where possible as they impact test performance
     */
    @Deprecated
    protected void restartApp() {
        TestUtils.log("Restarting app");
        try {
            if (DriverManager.getDriver() != null) {
                String appPackage = DriverManager.getDriver().getCapabilities().getCapability("appPackage").toString();
                DriverManager.getDriver().terminateApp(appPackage);
                TestUtils.sleep(2000);
                DriverManager.getDriver().activateApp(appPackage);
                TestUtils.log("App restart successful");
            }
        } catch (Exception e) {
            TestUtils.log("ERROR: App restart failed: " + e.getMessage());
        }
    }
}