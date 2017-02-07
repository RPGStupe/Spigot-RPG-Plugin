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
	public ConfigHandler(JavaPlugin plugin) {
		new InventoryConfigHandler(plugin);
		new QuestsConfigHandler(plugin);
	}
}
