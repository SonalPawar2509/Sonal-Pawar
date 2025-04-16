package com.monefy.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Test listener for capturing test execution events and providing detailed logging
 */
public class Listeners implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        TestUtils.log("=========== TEST STARTED ===========");
        TestUtils.log("Test method: " + result.getMethod().getMethodName());
        TestUtils.log("Test description: " + result.getMethod().getDescription());
        TestUtils.log("Test priority: " + result.getMethod().getPriority());
        TestUtils.log("Test groups: " + Arrays.toString(result.getMethod().getGroups()));
        
        // Take a screenshot at the start of each test
        TestUtils.takeScreenshot("start_" + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        TestUtils.log("=========== TEST SUCCEEDED ===========");
        TestUtils.log("Test method: " + result.getMethod().getMethodName());
        TestUtils.log("Test duration: " + formatDuration(duration));
        
        // Take a screenshot at the end of each successful test
        TestUtils.takeScreenshot("success_" + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        TestUtils.log("=========== TEST FAILED ===========");
        TestUtils.log("Test method: " + result.getMethod().getMethodName());
        TestUtils.log("Test duration: " + formatDuration(duration));
        
        // Log the full exception details
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            TestUtils.log("Exception: " + throwable.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            TestUtils.log("Stack trace: " + sw);
        }
        
        // Capture screenshot on failure
        String screenshotPath = TestUtils.takeScreenshot("failure_" + result.getMethod().getMethodName());
        TestUtils.log("Screenshot saved at: " + screenshotPath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TestUtils.log("=========== TEST SKIPPED ===========");
        TestUtils.log("Test method: " + result.getMethod().getMethodName());
        
        // Log why the test was skipped
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            TestUtils.log("Skip reason: " + throwable.getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        TestUtils.log("=========== TEST PARTIALLY SUCCEEDED ===========");
        TestUtils.log("Test method: " + result.getMethod().getMethodName());
        TestUtils.log("Success percentage: " + result.getMethod().getSuccessPercentage() + "%");
    }

    @Override
    public void onStart(ITestContext context) {
        TestUtils.log("=========== TEST SUITE STARTED ===========");
        TestUtils.log("Test suite: " + context.getName());
        TestUtils.log("Included groups: " + Arrays.toString(context.getIncludedGroups()));
        TestUtils.log("Excluded groups: " + Arrays.toString(context.getExcludedGroups()));
        
        // Create directories for artifacts
        TestUtils.createDirectoryIfNotExists("screenshots");
        TestUtils.createDirectoryIfNotExists("logs");
    }

    @Override
    public void onFinish(ITestContext context) {
        TestUtils.log("=========== TEST SUITE FINISHED ===========");
        TestUtils.log("Test suite: " + context.getName());
        TestUtils.log("Passed tests: " + context.getPassedTests().size());
        TestUtils.log("Failed tests: " + context.getFailedTests().size());
        TestUtils.log("Skipped tests: " + context.getSkippedTests().size());
        
        long duration = context.getEndDate().getTime() - context.getStartDate().getTime();
        TestUtils.log("Total duration: " + formatDuration(duration));
    }
    
    /**
     * Format duration in milliseconds to a human-readable string
     * @param durationInMillis Duration in milliseconds
     * @return Formatted duration string
     */
    private String formatDuration(long durationInMillis) {
        long seconds = durationInMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        return String.format("%d min, %d sec (%d ms)", minutes, seconds, durationInMillis);
    }
}