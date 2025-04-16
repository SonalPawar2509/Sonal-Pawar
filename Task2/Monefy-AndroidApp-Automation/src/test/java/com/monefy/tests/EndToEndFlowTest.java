package com.monefy.tests;

import com.monefy.pages.OnboardingPage;
import com.monefy.pages.HomePage;
import com.monefy.pages.IncomePage;
import com.monefy.pages.ExpensePage;
import com.monefy.utils.TestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

/**
 * Tests for the end-to-end user flow of the Monefy app
 */
public class EndToEndFlowTest extends BaseTest {

    // Declare Page Objects as class members
    private OnboardingPage onboardingPage;
    private HomePage homePage;
    private IncomePage incomePage;
    private ExpensePage expensePage;
    private Faker faker;

    @BeforeClass
    public void initializePages() {
        TestUtils.log("Initializing page objects for EndToEndFlowTest");
        onboardingPage = new OnboardingPage();
        homePage = new HomePage();
        incomePage = new IncomePage();
        expensePage = new ExpensePage();
        faker = new Faker();
    }

    @Test(description = "Complete onboarding flow", priority = 1)
    public void testOnboardingFlow() {
        TestUtils.log("Starting Test: Onboarding Flow");

        // --- Onboarding Flow ---
        TestUtils.log("Executing onboarding...");
        boolean onboardingCompleted = onboardingPage.completeOnboarding();
        Assert.assertTrue(onboardingCompleted, "Onboarding flow should complete successfully");
        
        TestUtils.log("Verifying home screen after onboarding...");
        Assert.assertTrue(homePage.isIncomeButtonDisplayed(), "Income button should be displayed after onboarding");
        TestUtils.takeScreenshot("home_page_after_onboarding");
        TestUtils.log("Finished Test: Onboarding Flow");
    }

    @Test(description = "Add multiple income transactions (Salary, Savings, Deposits)", priority = 2, dependsOnMethods = "testOnboardingFlow")
    public void testIncomeFlow() {
        TestUtils.log("Starting Test: Income Flow");

        // --- Add Income Transactions ---
        TestUtils.log("*** Adding Income Transactions ***");
        addIncomeTransaction(String.valueOf(faker.number().numberBetween(100, 5000)), faker.lorem().sentence(2, 4), "Salary");
        addIncomeTransaction(String.valueOf(faker.number().numberBetween(50, 1000)), faker.lorem().sentence(2, 4), "Savings");
        addIncomeTransaction(String.valueOf(faker.number().numberBetween(10, 500)), faker.lorem().sentence(2, 4), "Deposits");
        TestUtils.log("Finished adding income transactions.");
        TestUtils.takeScreenshot("home_page_after_adding_all_income");
        TestUtils.log("Finished Test: Income Flow");
    }

    @Test(description = "Add multiple expense transactions (Food, Bills, Car)", priority = 3, dependsOnMethods = "testIncomeFlow")
    public void testExpenseFlow() {
        TestUtils.log("Starting Test: Expense Flow");

        // --- Add Expense Transactions ---
        TestUtils.log("*** Adding Expense Transactions ***");
        addExpenseTransaction(String.valueOf(faker.number().numberBetween(10, 500)), faker.lorem().sentence(2, 4), "Food");
        TestUtils.log("Finished adding expense transactions.");
        TestUtils.takeScreenshot("home_page_after_adding_all_expenses");
        TestUtils.log("Finished Test: Expense Flow");
    }

    /**
     * Helper method 
     */
    private void addIncomeTransaction(String amount, String note, String category) {
        // TestUtils.log("Starting Add Income: Category=" + category + ", Amount=" + amount); // Removed
        // Navigate to Add Income Screen
        homePage.clickIncomeButton();
        // TestUtils.sleep(1000); // Use explicit wait

        // Verify Add Income Screen
        // TestUtils.sleep(1500); // Use explicit wait
        Assert.assertTrue(incomePage.isIncomeScreenDisplayed(), "New Income screen should be displayed for " + category);
        TestUtils.takeScreenshot("new_income_screen_" + category.toLowerCase());

        // Add Income Transaction
        incomePage.addIncome(amount, note, category);
        // TestUtils.sleep(1000); // Use explicit wait

        // Verify Return to Home Screen
        // TestUtils.sleep(1500); // Use explicit wait
        Assert.assertTrue(homePage.isIncomeButtonDisplayed(), "Income button should be displayed after adding " + category);
        // TestUtils.log("Finished Add Income: Category=" + category); // Removed
    }

    /**
     * Helper method 
     */
    private void addExpenseTransaction(String amount, String note, String category) {
        // TestUtils.log("Starting Add Expense: Category=" + category + ", Amount=" + amount); // Removed
        // Navigate to Add Expense Screen
        homePage.clickExpenseButton();
        // TestUtils.sleep(1000); // Use explicit wait

        // Verify Add Expense Screen (Move verification *after* sleep)
        // TestUtils.sleep(1500); // Wait for screen transition - Use explicit wait
        Assert.assertTrue(expensePage.isExpenseScreenDisplayed(), "New Expense screen should be displayed for " + category);
        TestUtils.takeScreenshot("new_expense_screen_" + category.toLowerCase());

        // Add Expense Transaction
        expensePage.addExpense(amount, note, category);
        // TestUtils.sleep(1000); // Use explicit wait

        // Verify Return to Home Screen
        // TestUtils.sleep(1500); // Use explicit wait
        Assert.assertTrue(homePage.isExpenseButtonDisplayed(), "Expense button should be displayed after adding " + category);
        // TestUtils.log("Finished Add Expense: Category=" + category); // Removed
    }
} 