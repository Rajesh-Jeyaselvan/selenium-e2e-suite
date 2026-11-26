package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CheckoutPage;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * End-to-end checkout flow tests on SauceDemo.
 */
public class CheckoutTest extends BaseTest {

    @Test(description = "Verify end-to-end purchase completes with order confirmation")
    public void completeEndToEndCheckoutFlow() {
        CheckoutPage checkoutPage = new LoginPage()
                .loginAs("standard_user", "secret_sauce")
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .proceedToCheckout()
                .fillCustomerInfo("Rajesh", "J", "560001")
                .clickContinue()
                .clickFinish();

        Assert.assertEquals(
                checkoutPage.getConfirmationMessage(),
                "Thank you for your order!",
                "Order confirmation header should be displayed after finishing checkout."
        );
    }

    @Test(description = "Verify checkout requires customer first name")
    public void checkoutWithoutFirstNameShowsValidationError() {
        CheckoutPage checkoutPage = new LoginPage()
                .loginAs("standard_user", "secret_sauce")
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .proceedToCheckout()
                .fillCustomerInfo("", "J", "560001")
                .clickContinue();

        Assert.assertTrue(
                checkoutPage.getErrorMessage().contains("First Name is required"),
                "Expected validation error for missing first name."
        );
    }
}
