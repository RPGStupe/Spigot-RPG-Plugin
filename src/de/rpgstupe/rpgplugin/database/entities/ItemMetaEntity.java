package de.rpgstupe.rpgplugin.database.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemFlag;

public class ItemMetaEntity {
	public String displayName;
	public List<String> lore;
	public Set<ItemFlag> itemflags;
	public boolean unbreakable;
	public Map<String, String> customMetaTags;
	
	
	
	public ItemMetaEntity(String displayName, List<String> lore, Set<ItemFlag> itemflags, boolean unbreakable) {
		this.displayName = displayName;
		this.lore = lore;
		this.itemflags = itemflags;
		this.unbreakable = unbreakable;
		this.customMetaTags = new HashMap<String, String>();
	}

	public ItemMetaEntity() {
		this(null, new ArrayList<String>(), new HashSet<ItemFlag>(), false);
	}
}
