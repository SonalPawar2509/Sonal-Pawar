package com.monefy.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumBy;
import com.monefy.utils.DriverManager;
import com.monefy.utils.TestUtils;

public class ExpensePage extends BasePage {

    @AndroidFindBy(id = "com.monefy.app.lite:id/amount_text")
    private WebElement amountText;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/textViewNote")
    private WebElement noteText;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/keyboard_action_button")
    private WebElement addButton;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/toolbar_title")
    private WebElement screenTitle;
    
    // Various expense category buttons
    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_category_bills")
    private WebElement billsCategory;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_category_food")
    private WebElement foodCategory;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_category_car")
    private WebElement carCategory;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_category_clothes")
    private WebElement clothesCategory;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_category_communications")
    private WebElement communicationsCategory;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_category_entertainment")
    private WebElement entertainmentCategory;
    
    /**
     * Check if the New Expense screen is displayed
     * @return true if the screen is displayed, false otherwise
     */
    public boolean isExpenseScreenDisplayed() {
        try {
            return screenTitle.isDisplayed() && 
                   "New expense".equalsIgnoreCase(screenTitle.getText());
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
            TestUtils.log("Entering expense amount using numeric keypad: " + amount);
            // First try to click on the amount field to ensure it's focused
            click(amountText);
            TestUtils.sleep(500); 

            // Enter digits one by one using the numeric keypad
            for (char digit : amount.toCharArray()) {
                // Assuming the keypad IDs are the same as in IncomePage
                WebElement digitButton = DriverManager.getDriver().findElement(
                    AppiumBy.id("com.monefy.app.lite:id/buttonKeyboard" + digit));
                click(digitButton);
                // Add a small delay between clicks if needed, but often not required
                // TestUtils.sleep(100);
            }
        } catch (Exception e) {
            TestUtils.log("ERROR: Failed to enter expense amount: " + e.getMessage());
            // Optionally take a screenshot on error
            TestUtils.takeScreenshot("expense_amount_entry_error"); 
            throw new RuntimeException("Failed to enter expense amount: " + e.getMessage(), e);
        }
    }
    
    /**
     * Enter note in the note field
     * @param note The note to enter
     */
    public void enterNote(String note) {
        sendKeys(noteText, note);
    }
    
    /**
     * Click add button to add the expense
     */
    public void clickAddButton() {
        click(addButton);
    }
    
    /**
     * Select a category by name using a more robust approach
     * @param categoryName The category name to select (bills, car, clothes, communications, etc.)
     */
    public void selectCategory(String categoryName) {
        try {
            // Click on amount text to trigger category selection
            click(amountText);
            
            // First find and click the Choose Category button
            WebElement chooseCategoryButton = DriverManager.getDriver().findElement(
                AppiumBy.xpath("//*[@text='CHOOSE CATEGORY']"));
            click(chooseCategoryButton);
            
            TestUtils.takeScreenshot("category_selection_" + categoryName);
            TestUtils.sleep(1000);
            
            // Then select the specific category by text
            WebElement category = DriverManager.getDriver().findElement(
                AppiumBy.xpath("//*[contains(@text, '" + categoryName + "')]"));
            click(category);
            
        } catch (Exception e) {
            TestUtils.log("ERROR: Failed to select expense category - " + e.getMessage());
            throw new RuntimeException("Failed to select expense category: " + categoryName, e);
        }
    }
    
    /**
     * Add expense with specified amount, note, and category
     * @param amount The amount to add
     * @param note The note for the expense
     * @param category The category to select (bills, car, clothes, communications, etc.)
     */
    public void addExpense(String amount, String note, String category) {
        // Enter amount
        enterAmount(amount);
        
        // Enter note
        enterNote(note);
        
        // Select category based on name
        selectCategory(category);
    }
    
    /**
     * Click outside of input fields to dismiss keyboard
     */
    public void clickOutsideToDissmissKeyboard() {
        click(amountText);
    }
}