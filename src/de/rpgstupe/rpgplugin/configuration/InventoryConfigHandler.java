package de.rpgstupe.rpgplugin.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryConfigHandler {
	private File configFile;
	private FileConfiguration config;
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
	public static int healthPotionsMaxAmount;
	public static Location spawnLocation;
	
	public InventoryConfigHandler(JavaPlugin plugin) {
		loadFiles(plugin);
		loadConfig();
	}
	
	/**
	 * ALWAYS save the config after writing
	 * 
	 * @throws IOException
	 */
	public void saveConfig() throws IOException {
		config.save(configFile);
	}

	public FileConfiguration getInvConfig() {
		return this.config;
	}

	private void loadFiles(JavaPlugin plugin) {
		// get file resource
		configFile = new File(plugin.getDataFolder(), "inventory_config.yml");

		// create file if it does not exist
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("inventory_config.yml", false);
		}

		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getServer().getLogger().info(e.toString());
		}
	}
	
	private void loadConfig() {
		InventoryConfigHandler.moneySmallExchangeRate = config.getInt("money.exchangerate.small_to_medium");
		InventoryConfigHandler.moneyMediumExchangeRate = config.getInt("money.exchangerate.medium_to_large");
		InventoryConfigHandler.moneyLargeMaxAmount = config.getInt("money.exchangerate.max_amount_large");

		InventoryConfigHandler.useDurabilityItems = config.getBoolean("money.durabilities.use_durability_item");

		InventoryConfigHandler.moneySmallItem = config.getString("money.items.money_small_item");
		InventoryConfigHandler.moneyMediumItem = config.getString("money.items.money_medium_item");
		InventoryConfigHandler.moneyLargeItem = config.getString("money.items.money_large_item");

		InventoryConfigHandler.moneySmallSlot = config.getInt("inventory.slots.money_small");
		InventoryConfigHandler.moneyMediumSlot = config.getInt("inventory.slots.money_medium");
		InventoryConfigHandler.moneyLargeSlot = config.getInt("inventory.slots.money_large");
		
		InventoryConfigHandler.moneySlotsUsed = config.getBoolean("inventory.slots.use_money_slots");
		
		InventoryConfigHandler.healthPotionsMaxAmount = config.getInt("inventory.items.max_amount_healthpotion");
		
		InventoryConfigHandler.spawnLocation = new Location(Bukkit.getWorld("world"), 5000, 5000, 5000);
	}
}
