package de.rpgstupe.rpgplugin.player;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.rpgstupe.rpgplugin.configuration.InventoryConfigHandler;

public class MoneyHandlerTest {

	@DataProvider(name = "addDataProvider")
	public static Object[][] addData() {
		return new Object[][] { { 100, 100, 127, 0, 0, 0, 100, 100, 100, 0, 1, 101 },
				{ 100, 100, 127, 5, 10, 15, 115, 125, 1, 20, 36, 17 },
				{ 12, 13, 14, 1, 2, 3, 1, 2, 3, 2, 4, 6 },
				{ 12, 13, 14, 10, 15, 10, 20, 20, 2, 6, 11, 14 },
				{ 100, 100, 100, 1000, 100, 10, -50, -5, 2, 50, 4, 13 }};
	}

	@Test(dataProvider = "addDataProvider")
	public void addShouldMergeMoneyCorrectly(int smallExchangeRate, int mediumExchangeRate, int largeMaxAmount,
			int smallAmount, int mediumAmount, int largeAmount, int addSmall, int addMedium, int addLarge,
			int expectedSmallAmount, int expectedMediumAmount, int expectedLargeAmount) {

		InventoryConfigHandler.moneySmallExchangeRate = smallExchangeRate;
		InventoryConfigHandler.moneyMediumExchangeRate = mediumExchangeRate;
		InventoryConfigHandler.moneyLargeMaxAmount = largeMaxAmount;

		MoneyHandler moneyHandler = new MoneyHandler(smallAmount, mediumAmount, largeAmount);
		moneyHandler.add(addSmall, addMedium, addLarge);

		Assert.assertEquals(moneyHandler.getMoneySmallAmount(), expectedSmallAmount);
		Assert.assertEquals(moneyHandler.getMoneyMediumAmount(), expectedMediumAmount);
		Assert.assertEquals(moneyHandler.getMoneyLargeAmount(), expectedLargeAmount);
	}
}
