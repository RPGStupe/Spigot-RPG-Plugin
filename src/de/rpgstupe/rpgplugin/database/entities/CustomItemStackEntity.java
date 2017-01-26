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
	
	public ItemMetaEntity itemMeta;
	
	public CustomItemStackEntity() {
	}
	
	public CustomItemStackEntity(Material type, int amount, DataEntity data, short durability, ItemMetaEntity itemMeta) {
		this.type = type;
		this.amount = amount;
		this.data = data;
		this.durability = durability;
		this.itemMeta = itemMeta;
	}
}
