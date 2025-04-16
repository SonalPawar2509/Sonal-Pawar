package com.monefy.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumBy;
import com.monefy.utils.DriverManager;
import com.monefy.utils.TestUtils;

public class IncomePage extends BasePage {

    @AndroidFindBy(id = "com.monefy.app.lite:id/amount_text")
    private WebElement amountText;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/textViewNote")
    private WebElement noteText;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/keyboard_action_button")
    private WebElement addButton;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/toolbar_title")
    private WebElement screenTitle;
    
    // Various income category buttons
    @AndroidFindBy(id = "com.monefy.app.lite:id/income_category_salary")
    private WebElement salaryCategory;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/income_category_deposits")
    private WebElement depositsCategory;
    
    /**
     * Check if the New Income screen is displayed
     * @return true if the screen is displayed, false otherwise
     */
    public boolean isIncomeScreenDisplayed() {
        try {
            return screenTitle.isDisplayed() && 
                   "New income".equalsIgnoreCase(screenTitle.getText());
        } catch (Exception e) {
            // Alternative check if title element is not found
            return amountText.isDisplayed() && 
                   noteText.isDisplayed() && 
                   addButton.isDisplayed();
        }
    }
    
    /**
     * Enter amount in the amount field
     * @param amount The amount to enter
     */
    public void enterAmount(String amount) {
        try {
            // First try to click on the amount field to ensure it's focused
            click(amountText);
            TestUtils.sleep(500);
            
            // Enter digits one by one using the numeric keypad
            for (char digit : amount.toCharArray()) {
                WebElement digitButton = DriverManager.getDriver().findElement(
                    AppiumBy.id("com.monefy.app.lite:id/buttonKeyboard" + digit));
                click(digitButton);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter amount: " + e.getMessage(), e);
        }
    }
    
    /**
     * Enter note in the note field
     * @param note The note to enter
     */
    public void enterNote(String note) {
        try {
            // Click on the note field to focus it
            click(noteText);
            TestUtils.sleep(500);
            
            // Clear and enter the text
            noteText.clear();
            noteText.sendKeys(note);
            
            // Dismiss keyboard by clicking outside
            clickOutsideToDissmissKeyboard();
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter note: " + e.getMessage(), e);
        }
    }
    
    /**
     * Click add button to add the income
     */
    public void clickAddButton() {
        click(addButton);
    }
    
    /**
     * Select salary category for income
     */
    public void selectSalaryCategory() {
        click(salaryCategory);
    }
    
    /**
     * Select deposits category for income
     */
    public void selectDepositsCategory() {
        click(depositsCategory);
    }
    
    /**
     * Select a category for the income
     * @param categoryName The category name to select
     */
    public void selectCategory(String categoryName) {
        try {
            // Click on choose category button first
            WebElement chooseCategoryButton = DriverManager.getDriver().findElement(
                AppiumBy.xpath("//*[@text='CHOOSE CATEGORY']"));
            click(chooseCategoryButton);
            
            TestUtils.sleep(1000);
            
            // Then select the specific category by text
            WebElement category = DriverManager.getDriver().findElement(
                AppiumBy.xpath("//*[contains(@text, '" + categoryName + "')]"));
            click(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to select income category: " + categoryName, e);
        }
    }
    
    /**
     * Add income with specified amount, note, and category
     * @param amount The amount to add
     * @param note The note for the income
     * @param category The category to select (e.g., "salary", "deposits")
     */
    public void addIncome(String amount, String note, String category) {
        try {
            // Take a screenshot before starting
            TestUtils.takeScreenshot("before_adding_income_" + category);
            
            // Enter amount using the keypad method
            enterAmount(amount);
            
            // Enter note with keyboard handling
            enterNote(note);
            
            // Select category based on name
            selectCategory(category);
            
            // Take a screenshot after completing
            TestUtils.takeScreenshot("after_adding_income_" + category);
        } catch (Exception e) {
            TestUtils.takeScreenshot("income_add_error_" + category);
            throw new RuntimeException("Failed to add income: " + e.getMessage(), e);
        }
    }
    
    /**
     * Click outside of input fields to dismiss keyboard
     */
    public void clickOutsideToDissmissKeyboard() {
        click(amountText);
    }
}