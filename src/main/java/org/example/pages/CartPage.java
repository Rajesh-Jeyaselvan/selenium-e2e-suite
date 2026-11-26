package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the SauceDemo shopping cart page.
 */
public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector(".cart_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public boolean isProductInCart(String productName) {
        List<WebElement> names = driver.findElements(itemNames);
        return names.stream().anyMatch(el -> el.getText().trim().equals(productName));
    }

    public CartPage removeProduct(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='remove-" + slug + "']"));
        return this;
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage();
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage();
    }
}
