package de.rpgstupe.rpgplugin.database.entities;

import org.bukkit.Material;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

public class CustomItemStackEntity {
	@Id
	public int id;
	public Material type;
	public int amount;
	
	@Embedded
	public DataEntity data;
	
	public short durability;
}
