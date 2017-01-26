package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.configuration.file.FileConfiguration;

import de.rpgstupe.rpgplugin.PlayerWrapper;

public class MoneyStacksHandler {

	public static int moneySmallExchangeRate;
	public static int moneyMediumExchangeRate;
	public static int moneyLargeMaxAmount;

	public static boolean useDurabilityItems;

	public static String moneySmallItem;
	public static String moneyMediumItem;
	public static String moneyLargeItem;

	public static int moneySmallSlot;
	public static int moneyMediumSlot;
	public static int moneyLargeSlot;
	
	public static boolean moneySlotsUsed;

	private FileConfiguration playerInvConfig;

	public MoneyStacksHandler(FileConfiguration playerInvConfig) {
		this.playerInvConfig = playerInvConfig;
		loadConfigData();
	}

	/**
	 * This method merges the amount of money given in the parameters using the
	 * stacksizes of the config file
	 * 
	 * @param pw
	 *            the player wrapper object of the specific player to merge
	 * @param small
	 *            the amount of moneySmall in the inventory
	 * @param medium
	 *            the amount of moneyMedium in the inventory
	 * @param large
	 *            the amount of moneyLarge in the inventory
	 */
	public static void mergeMoneyIntoPlayerWrapper(PlayerWrapper pw, int small, int medium, int large) {
		int tempSmall = small + pw.getMoneySmallAmount();
		int tempMedium = medium + pw.getMoneyMediumAmount();
		int tempLarge = large + pw.getMoneyLargeAmount();

		if (tempSmall >= MoneyStacksHandler.moneySmallExchangeRate) {
			tempMedium += tempSmall / MoneyStacksHandler.moneySmallExchangeRate;
			tempSmall %= MoneyStacksHandler.moneySmallExchangeRate;
		}

		if (tempMedium >= MoneyStacksHandler.moneyMediumExchangeRate) {
			tempLarge += tempMedium / MoneyStacksHandler.moneyMediumExchangeRate;
			tempMedium %= MoneyStacksHandler.moneyMediumExchangeRate;

		}

		if (tempLarge > MoneyStacksHandler.moneyLargeMaxAmount) {
			tempLarge = MoneyStacksHandler.moneyLargeMaxAmount;
		}

		pw.setMoneySmallAmount(tempSmall);
		pw.setMoneyMediumAmount(tempMedium);
		pw.setMoneyLargeAmount(tempLarge);
	}

	private void loadConfigData() {
		MoneyStacksHandler.moneySmallExchangeRate = playerInvConfig.getInt("money.exchangerate.small_to_medium");
		MoneyStacksHandler.moneyMediumExchangeRate = playerInvConfig.getInt("money.exchangerate.medium_to_large");
		MoneyStacksHandler.moneyLargeMaxAmount = playerInvConfig.getInt("money.exchangerate.max_amount_large");

		MoneyStacksHandler.useDurabilityItems = playerInvConfig.getBoolean("money.durabilities.use_durability_item");

		MoneyStacksHandler.moneySmallItem = playerInvConfig.getString("money.items.money_small_item");
		MoneyStacksHandler.moneyMediumItem = playerInvConfig.getString("money.items.money_medium_item");
		MoneyStacksHandler.moneyLargeItem = playerInvConfig.getString("money.items.money_large_item");

		MoneyStacksHandler.moneySmallSlot = playerInvConfig.getInt("inventory.slots.money_small");
		MoneyStacksHandler.moneyMediumSlot = playerInvConfig.getInt("inventory.slots.money_medium");
		MoneyStacksHandler.moneyLargeSlot = playerInvConfig.getInt("inventory.slots.money_large");
		
		MoneyStacksHandler.moneySlotsUsed = playerInvConfig.getBoolean("inventory.slots.use_money_slots");
	}
}
