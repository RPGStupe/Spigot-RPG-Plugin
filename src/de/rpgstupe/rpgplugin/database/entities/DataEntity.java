package de.rpgstupe.rpgplugin.database.entities;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import de.rpgstupe.rpgplugin.player.inventory.CustomItemStack;

public class DataEntity {
	public Material type;
	
	public DataEntity() {
	}
	
	public DataEntity(Material type) {
		this.type = type;
	}

	public DataEntity(CustomItemStack stack) {
		this(stack.getItemStack().getData());
	}

	public DataEntity(MaterialData data) {
		this(data.getItemType());
	}

	public Byte toData() {
		// TODO Auto-generated method stub
		return null;
	}

	public MaterialData toMaterialData() {
		return new MaterialData(type);
	}
}
