package de.rpgstupe.rpgplugin.database.entities;

import org.bukkit.Material;

public class DataEntity {
	public Material type;
	
	public DataEntity() {
		this(null);
	}
	
	public DataEntity(Material type) {
		this.type = type;
	}
}
