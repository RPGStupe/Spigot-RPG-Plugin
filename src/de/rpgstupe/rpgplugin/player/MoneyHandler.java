package de.rpgstupe.rpgplugin.player;

import de.rpgstupe.rpgplugin.configuration.ConfigHandler;

public class MoneyHandler {
	private int moneySmallAmount;
	private int moneyMediumAmount;
	private int moneyLargeAmount;

	public MoneyHandler() {
		this(0, 0, 0);
	}

	public MoneyHandler(int smallAmount, int mediumAmount, int largeAmount) {
		this.moneySmallAmount = smallAmount;
		this.moneyMediumAmount = mediumAmount;
		this.moneyLargeAmount = largeAmount;
	}

	public void add(int smallAmount, int mediumAmount, int largeAmount) {
		this.moneySmallAmount += smallAmount;
		this.moneyMediumAmount += mediumAmount;
		this.moneyLargeAmount += largeAmount;

		mergeMoney();
	}

	public int getMoneySmallAmount() {
		return moneySmallAmount;
	}

	public int getMoneyMediumAmount() {
		return moneyMediumAmount;
	}

	public int getMoneyLargeAmount() {
		return moneyLargeAmount;
	}
	
	private void mergeMoney() {
		moneyMediumAmount += moneySmallAmount / ConfigHandler.moneySmallExchangeRate;
		moneySmallAmount %= ConfigHandler.moneySmallExchangeRate;

		moneyLargeAmount += moneyMediumAmount / ConfigHandler.moneyMediumExchangeRate;
		moneyMediumAmount %= ConfigHandler.moneyMediumExchangeRate;
	}
}
