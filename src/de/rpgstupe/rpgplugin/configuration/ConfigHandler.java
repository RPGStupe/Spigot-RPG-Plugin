package de.rpgstupe.rpgplugin.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.rpgstupe.rpgplugin.player.inventory.CharacterInventoryHandler;

/**
 * This class handles the config file <code>inventory_config.yml</code> for the
 * {@link CharacterInventoryHandler}
 * 
 * @author RPGAlpacca
 *
 */
public class ConfigHandler {

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

	
	
	
	public ConfigHandler(JavaPlugin plugin) {
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
		ConfigHandler.moneySmallExchangeRate = config.getInt("money.exchangerate.small_to_medium");
		ConfigHandler.moneyMediumExchangeRate = config.getInt("money.exchangerate.medium_to_large");
		ConfigHandler.moneyLargeMaxAmount = config.getInt("money.exchangerate.max_amount_large");

		ConfigHandler.useDurabilityItems = config.getBoolean("money.durabilities.use_durability_item");

		ConfigHandler.moneySmallItem = config.getString("money.items.money_small_item");
		ConfigHandler.moneyMediumItem = config.getString("money.items.money_medium_item");
		ConfigHandler.moneyLargeItem = config.getString("money.items.money_large_item");

		ConfigHandler.moneySmallSlot = config.getInt("inventory.slots.money_small");
		ConfigHandler.moneyMediumSlot = config.getInt("inventory.slots.money_medium");
		ConfigHandler.moneyLargeSlot = config.getInt("inventory.slots.money_large");
		
		ConfigHandler.moneySlotsUsed = config.getBoolean("inventory.slots.use_money_slots");
		
		ConfigHandler.healthPotionsMaxAmount = config.getInt("inventory.items.max_amount_healthpotion");
		
		ConfigHandler.spawnLocation = new Location(Bukkit.getWorld("world"), 5000, 5000, 5000);
	}
}
