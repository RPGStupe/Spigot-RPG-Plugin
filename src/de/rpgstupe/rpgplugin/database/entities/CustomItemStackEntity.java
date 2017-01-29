package de.rpgstupe.rpgplugin.database.entities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import de.rpgstupe.rpgplugin.player.inventory.CustomItemStack;
import de.rpgstupe.rpgplugin.player.inventory.EnumCustomItemStackType;

public class CustomItemStackEntity {
	@Id
	public int id;

	public Material materialType;
	public int amount;

	@Embedded
	public DataEntity data;

	public short durability;

	public ItemMetaEntity itemMeta;

	public EnumCustomItemStackType type;

	public CustomItemStackEntity() {
	}

	public CustomItemStackEntity(Material materialType, int amount, DataEntity data, short durability,
			ItemMetaEntity itemMeta) {
		this.materialType = materialType;
		this.amount = amount;
		this.data = data;
		this.durability = durability;
		this.itemMeta = itemMeta;
	}

	public CustomItemStackEntity(CustomItemStack stack) {
		if (stack.getItemStack() != null) {
			this.materialType = stack.getItemStack().getType();
			this.amount = stack.getItemStack().getAmount();
			this.durability = stack.getItemStack().getDurability();
			this.data = new DataEntity(stack);
			this.itemMeta = new ItemMetaEntity(stack);
		}
	}

	public CustomItemStack toCustomItemStack() {
		if (materialType == null) {
			return new CustomItemStack();
		}
		CustomItemStack stack = new CustomItemStack(new ItemStack(materialType, amount, durability));
		stack.getItemStack().setData(data.toMaterialData());
		//stack.getItemStack().setItemMeta();
		return stack;
	}
}
