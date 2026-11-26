package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CartPage;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Shopping cart add/remove tests on SauceDemo.
 */
public class CartTest extends BaseTest {

    @Test(description = "Verify adding and removing products updates cart badge and cart items")
    public void addAndRemoveProductsFromCart() {
        InventoryPage inventoryPage = new LoginPage()
                .loginAs("standard_user", "secret_sauce")
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light");

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge should show 2 items.");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"), "Backpack should be in cart.");
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Bike Light"), "Bike Light should be in cart.");

        cartPage.removeProduct("Sauce Labs Bike Light");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item after removal.");
        Assert.assertFalse(cartPage.isProductInCart("Sauce Labs Bike Light"), "Bike Light should be removed.");
    }
}
