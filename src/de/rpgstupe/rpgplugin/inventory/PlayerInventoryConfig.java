package de.rpgstupe.rpgplugin.inventory;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class handles the config file <code>inventory_config.yml</code> for the
 * {@link PlayerInventoryHandler}
 * 
 * @author RPGAlpacca
 *
 */
public class PlayerInventoryConfig {

	private File playerInvConfigFile;
	private FileConfiguration invConfig;

	public PlayerInventoryConfig(JavaPlugin plugin) {
		loadFiles(plugin);
	}

	/**
	 * ALWAYS save the config after writing
	 * 
	 * @throws IOException
	 */
	public void saveInvConfig() throws IOException {
		invConfig.save(playerInvConfigFile);
	}

	public FileConfiguration getInvConfig() {
		return this.invConfig;
	}

	private void loadFiles(JavaPlugin plugin) {
		// get file resource
		playerInvConfigFile = new File(plugin.getDataFolder(), "inventory_config.yml");

		// create file if it does not exist
		if (!playerInvConfigFile.exists()) {
			playerInvConfigFile.getParentFile().mkdirs();
			plugin.saveResource("inventory_config.yml", false);
		}

		invConfig = new YamlConfiguration();
		try {
			invConfig.load(playerInvConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getServer().getLogger().info(e.toString());
		}
	}
}
