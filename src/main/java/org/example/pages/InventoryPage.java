package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the SauceDemo products/inventory page.
 */
public class InventoryPage extends BasePage {

    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && "Products".equals(getText(pageTitle));
    }

    public int getProductCount() {
        return waitForAllVisible(inventoryItems).size();
    }

    public InventoryPage sortBy(String optionText) {
        selectByVisibleText(sortDropdown, optionText);
        return this;
    }

    public List<Double> getProductPrices() {
        return waitForAllVisible(itemPrices).stream()
                .map(WebElement::getText)
                .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                .toList();
    }

    public InventoryPage addProductToCart(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='add-to-cart-" + slug + "']"));
        return this;
    }

    public int getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText().trim());
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }

    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage();
    }
}
