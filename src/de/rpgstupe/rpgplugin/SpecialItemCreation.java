package de.rpgstupe.rpgplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.rpgstupe.rpgplugin.player.inventory.CustomItemStack;

public class SpecialItemCreation {
	private SpecialItemCreation() {
	}
	
	public static CustomItemStack createHearthstone() {
		CustomItemStack hearthstone = new CustomItemStack(new ItemStack(Material.DOUBLE_PLANT));
		hearthstone.setString("type", "teleporter");
		hearthstone.setBoolean("notmoveable", true);
		hearthstone.setString("description", "Teleports you to the selected Spawnpoint");
		ItemMeta meta = hearthstone.getItemStack().getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(hearthstone.getString("description"));
		meta.setLore(lore);
		meta.setDisplayName("Hearthstone");
		hearthstone.getItemStack().setItemMeta(meta);
		hearthstone.setMaxStackSize(1);
		return hearthstone;
	}
	
	public static CustomItemStack createCompassMenu() {
		CustomItemStack compassMenu = new CustomItemStack(new ItemStack(Material.COMPASS));
		compassMenu.setString("type", "compassmenu");
		compassMenu.setBoolean("notmoveable", true);
		compassMenu.setString("description", "Opens the menu interface");
		ItemMeta meta = compassMenu.getItemStack().getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(compassMenu.getString("description"));
		meta.setLore(lore);
		meta.setDisplayName("Compass Menu");
		compassMenu.getItemStack().setItemMeta(meta);
		compassMenu.setMaxStackSize(1);
		return compassMenu;
	}
}
