package de.rpgstupe.rpgplugin.database.entities;

import org.mongodb.morphia.annotations.Id;

import de.rpgstupe.rpgplugin.player.inventory.FakeInventory;

public class FakeInventoryEntity {

	@Id
	public int id;
	
	public CustomItemStackEntity[] inventoryArray;

	public FakeInventoryEntity() {
		
	}
	
	public FakeInventoryEntity(FakeInventory characterInventory) {
		inventoryArray = new CustomItemStackEntity[characterInventory.getInventorySize()];
		System.out.println(characterInventory.toString());
		for (int i = 0; i < characterInventory.getInventorySize(); i++) {
			inventoryArray[i] = new CustomItemStackEntity(characterInventory.getIndex(i));
		}
	}

	public FakeInventory toFakeInventory() {
		FakeInventory inv = new FakeInventory(inventoryArray.length);
		for (int i = 0; i < inv.getInventorySize(); i++) {
			inv.set(i, inventoryArray[i].toCustomItemStack());
		}
		return inv;
	}
}
