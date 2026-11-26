package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Product catalog & sorting tests on SauceDemo inventory page.
 */
public class InventoryTest extends BaseTest {

    @Test(description = "Verify sorting products by Price (low to high) orders prices ascending")
    public void sortProductsByPriceLowToHigh() {
        InventoryPage inventoryPage = new LoginPage()
                .loginAs("standard_user", "secret_sauce")
                .sortBy("Price (low to high)");

        List<Double> actualPrices = inventoryPage.getProductPrices();
        List<Double> expectedSorted = new ArrayList<>(actualPrices);
        Collections.sort(expectedSorted);

        Assert.assertEquals(actualPrices, expectedSorted, "Products should be sorted by price ascending.");
    }
}
