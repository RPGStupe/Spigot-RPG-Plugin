package de.rpgstupe.rpgplugin.database.entities;

import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

public class CustomItemStackEntity {
	@Id
	public int id;
	
	@Embedded
	public ItemStack stack;
}
