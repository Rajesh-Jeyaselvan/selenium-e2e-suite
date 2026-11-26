package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Authentication tests against SauceDemo (valid login, locked/invalid users, and logout).
 */
public class LoginTest extends BaseTest {

    @Test(description = "Verify standard_user can log in and view the Products page")
    public void validLoginShouldOpenProductsPage() {
        InventoryPage inventoryPage = new LoginPage()
                .loginAs("standard_user", "secret_sauce");

        Assert.assertTrue(inventoryPage.isPageLoaded(), "Products page should load after valid login.");
        Assert.assertEquals(inventoryPage.getProductCount(), 6, "Expected 6 products on inventory page.");
    }

    @DataProvider(name = "invalidLoginData", parallel = true)
    public Object[][] invalidLoginData() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out."},
                {"wrong_user", "wrong_pass", "Username and password do not match any user in this service"},
                {"", "secret_sauce", "Username is required"}
        };
    }

    @Test(dataProvider = "invalidLoginData", description = "Verify invalid or locked credentials show expected error")
    public void invalidLoginShouldShowError(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage()
                .loginExpectingError(username, password);

        Assert.assertTrue(
                loginPage.getErrorMessage().contains(expectedError),
                "Expected error not shown. Actual: " + loginPage.getErrorMessage()
        );
    }

    @Test(description = "Verify logged-in user can log out via the sidebar menu")
    public void userShouldBeAbleToLogout() {
        LoginPage loginPage = new LoginPage()
                .loginAs("standard_user", "secret_sauce")
                .logout();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button should be visible after logout.");
    }
}
