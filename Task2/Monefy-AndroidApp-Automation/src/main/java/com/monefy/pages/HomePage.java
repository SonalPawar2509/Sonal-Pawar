package com.monefy.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import com.monefy.utils.DriverManager;
import io.appium.java_client.AppiumBy;

public class HomePage extends BasePage {

    @AndroidFindBy(id = "com.monefy.app.lite:id/expense_button")
    private WebElement expenseButton;

    @AndroidFindBy(id = "com.monefy.app.lite:id/income_button")
    private WebElement incomeButton;

    @AndroidFindBy(id = "com.monefy.app.lite:id/balance_amount")
    private WebElement balanceAmount;
    
    @AndroidFindBy(id = "com.monefy.app.lite:id/deposit_amount_text")
    private WebElement depositAmountText;
    
    // Main amount displayed in the center of the pie chart
    @AndroidFindBy(id = "com.monefy.app.lite:id/total_balance_amount")
    private WebElement totalBalanceAmount;

    public void clickExpenseButton() {
        click(expenseButton);
    }

    public void clickIncomeButton() {
        click(incomeButton);
    }

    public String getBalanceAmount() {
        return getText(balanceAmount);
    }
    
    public String getTotalBalanceAmount() {
        return getText(totalBalanceAmount);
    }
    
    /**
     * Get the amount for the deposits category
     * @return The deposits amount as a string
     */
    public String getDepositsAmount() {
        try {
            return getText(depositAmountText);
        } catch (Exception e) {
            // If we can't find the depositAmountText directly, try to find it by text "Deposits"
            try {
                // Find the Deposits category item
                WebElement depositCategory = DriverManager.getDriver().findElement(
                    AppiumBy.xpath("//*[contains(@text, 'Deposits')]"));
                
                // Get the parent element and find the amount text within it
                WebElement depositAmount = depositCategory.findElement(
                    AppiumBy.xpath(".//*[contains(@resource-id, 'amount_text')]"));
                    
                return getText(depositAmount);
            } catch (Exception e2) {
                return "0";
            }
        }
    }
    
    /**
     * Verify if the given amount is reflected in the deposits category
     * @param expectedAmount The expected amount
     * @return true if the amount matches, false otherwise
     */
    public boolean verifyDepositsAmount(String expectedAmount) {
        String actualAmount = getDepositsAmount().replaceAll("[^0-9.]", "");
        return actualAmount.contains(expectedAmount);
    }
    
    /**
     * Verify if the total balance reflects the given amount
     * @param expectedAmount The expected amount
     * @return true if the total balance contains the expected amount, false otherwise
     */
    public boolean verifyTotalBalance(String expectedAmount) {
        String actualBalance = getTotalBalanceAmount().replaceAll("[^0-9.]", "");
        return actualBalance.contains(expectedAmount);
    }
    
    public boolean isExpenseButtonDisplayed() {
        return isElementDisplayed(expenseButton);
    }
    
    public boolean isIncomeButtonDisplayed() {
        return isElementDisplayed(incomeButton);
    }
}