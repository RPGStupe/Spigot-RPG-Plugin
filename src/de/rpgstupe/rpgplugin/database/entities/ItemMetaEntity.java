package de.rpgstupe.rpgplugin.database.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.rpgstupe.rpgplugin.player.inventory.CustomItemStack;

public class ItemMetaEntity {
	public String displayName;
	public List<String> lore;
	public Set<ItemFlag> itemflags;
	public boolean unbreakable;
	
	
	
	public ItemMetaEntity(String displayName, List<String> lore, Set<ItemFlag> itemflags, boolean unbreakable) {
		this.displayName = displayName;
		this.lore = lore;
		this.itemflags = itemflags;
		this.unbreakable = unbreakable;
	}

	public ItemMetaEntity() {
		this(null, new ArrayList<String>(), new HashSet<ItemFlag>(), false);
	}

	public ItemMetaEntity(CustomItemStack stack) {
		this(stack.getItemStack().getItemMeta());
	}

	public ItemMetaEntity(ItemMeta itemMeta) {
		this(itemMeta.getDisplayName(), itemMeta.getLore(), itemMeta.getItemFlags(), itemMeta.isUnbreakable());
	}

	public ItemMeta toItemMeta(Material m) {
		ItemMeta meta = new ItemStack(m).getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		meta.setUnbreakable(unbreakable);
		return meta;
	}
}
