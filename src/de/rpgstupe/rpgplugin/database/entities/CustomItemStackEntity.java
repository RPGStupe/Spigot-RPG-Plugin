package de.rpgstupe.rpgplugin.database.entities;

import java.util.Map;
import java.util.Map.Entry;

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

	public Map<String, Object> customNBTTags;

	@Embedded
	public DataEntity data;

	public short durability;

	public ItemMetaEntity itemMeta;

	public EnumCustomItemStackType type;

	public CustomItemStackEntity() {
	}

	public CustomItemStackEntity(Material materialType, int amount, DataEntity data, short durability,
			ItemMetaEntity itemMeta, Map<String, Object> customNBTTags) {
		this.materialType = materialType;
		this.amount = amount;
		this.data = data;
		this.durability = durability;
		this.itemMeta = itemMeta;
		this.customNBTTags = customNBTTags;
	}

	public CustomItemStackEntity(CustomItemStack stack) {
		if (stack.getItemStack() != null) {
			this.materialType = stack.getItemStack().getType();
			this.amount = stack.getItemStack().getAmount();
			this.durability = stack.getItemStack().getDurability();
			this.data = new DataEntity(stack);
			this.itemMeta = new ItemMetaEntity(stack);
			System.out.println(stack.getCustomNBTTags());
			this.customNBTTags = stack.getCustomNBTTags();
		}
	}

	public CustomItemStack toCustomItemStack() {
		if (materialType == null) {
			return new CustomItemStack();
		}
		CustomItemStack stack = new CustomItemStack(new ItemStack(materialType, amount, durability), customNBTTags);
		stack.getItemStack().setData(data.toMaterialData());
		stack.getItemStack().setItemMeta(itemMeta.toItemMeta(stack.getItemStack().getType()));
		if (customNBTTags != null) {
			for (Entry<String, Object> entry : customNBTTags.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					stack.setString(key, (String) value);
				} else if (value instanceof Boolean) {
					stack.setBoolean(key, (boolean) value);
				}
			}
		}
		return stack;
	}
}
