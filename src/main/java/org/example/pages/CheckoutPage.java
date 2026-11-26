package org.example.pages;

import org.openqa.selenium.By;

/**
 * Page Object covering SauceDemo checkout steps and order confirmation.
 */
public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By confirmationHeader = By.cssSelector("[data-test='complete-header']");
    private final By errorBanner = By.cssSelector("[data-test='error']");

    public CheckoutPage fillCustomerInfo(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        click(continueButton);
        return this;
    }

    public CheckoutPage clickFinish() {
        click(finishButton);
        return this;
    }

    public String getConfirmationMessage() {
        return getText(confirmationHeader);
    }

    public String getErrorMessage() {
        return getText(errorBanner);
    }
}
