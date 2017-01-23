package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.configuration.file.FileConfiguration;

import de.rpgstupe.rpgplugin.PlayerWrapper;

public class MoneyStacksManager {

	public static int moneySmallExchangeRate;
	public static int moneyMediumExchangeRate;
	public static int moneyLargeMaxAmount;
	
	public static boolean useDurabilityItems;

	public static String moneySmallItem;
	public static String moneyMediumItem;
	public static String moneyLargeItem;
	
	private FileConfiguration playerInvConfig;
	
	
	
	public MoneyStacksManager(FileConfiguration playerInvConfig) {
		this.playerInvConfig = playerInvConfig;
		loadConfigDataIntoVariables();
	}

	private void loadConfigDataIntoVariables() {
		MoneyStacksManager.moneySmallExchangeRate = playerInvConfig.getInt("money.exchangerate.small_to_medium");
		MoneyStacksManager.moneyMediumExchangeRate = playerInvConfig.getInt("money.exchangerate.medium_to_large");
		MoneyStacksManager.moneyLargeMaxAmount = playerInvConfig.getInt("money.exchangerate.max_amount_large");

		MoneyStacksManager.useDurabilityItems = playerInvConfig.getBoolean("money.durabilities.use_durability_item");

		MoneyStacksManager.moneySmallItem = playerInvConfig.getString("money.items.money_small_item");
		MoneyStacksManager.moneyMediumItem = playerInvConfig.getString("money.items.money_medium_item");
		MoneyStacksManager.moneyLargeItem = playerInvConfig.getString("money.items.money_large_item");
	}
	
	/**
	 * This method merges the amount of money given in the parameters using the stacksizes of the config file
	 * 
	 * @param pw the player wrapper object of the specific player to merge
	 * @param small the amount of moneySmall in the inventory
	 * @param medium the amount of moneyMedium in the inventory
	 * @param large the amount of moneyLarge in the inventory
	 */
	public void mergeMoneyAndWriteToPlayerWrapper(PlayerWrapper pw, int small, int medium, int large) {
		int tempSmall = small;
		int tempMedium = medium;
		int tempLarge = large;

		if (tempSmall >= MoneyStacksManager.moneySmallExchangeRate) {
			tempSmall %= MoneyStacksManager.moneySmallExchangeRate;
			tempMedium += small / MoneyStacksManager.moneySmallExchangeRate;
		}

		if (tempMedium >= MoneyStacksManager.moneyMediumExchangeRate) {
			tempMedium %= MoneyStacksManager.moneyMediumExchangeRate;
			tempLarge += medium / MoneyStacksManager.moneyMediumExchangeRate;
		}

		if (tempLarge > MoneyStacksManager.moneyLargeMaxAmount) {
			tempLarge = MoneyStacksManager.moneyLargeMaxAmount;
		}

		pw.setMoneySmallAmount(tempSmall);
		pw.setMoneyMediumAmount(tempMedium);
		pw.setMoneyLargeAmount(tempLarge);
	}

}
