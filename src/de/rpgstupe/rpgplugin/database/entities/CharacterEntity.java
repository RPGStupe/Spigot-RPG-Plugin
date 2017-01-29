package de.rpgstupe.rpgplugin.database.entities;

import org.mongodb.morphia.annotations.Id;

public class CharacterEntity {
	@Id
	public int id;

	public MoneyHandlerEntity moneyHandler;
	public LocationEntity respawnLocation;
	public FakeInventoryEntity characterInventory;
	// public RPGClassEntity rpgclass;

	public int exp;
	public int level;
	public double damage;
	public double armor;

	public CharacterEntity() {
		
	}
	
	public CharacterEntity(MoneyHandlerEntity moneyHandler, LocationEntity respawnLocation,
			FakeInventoryEntity characterInventory, int exp, int level, double damage, double armor) {
		this.moneyHandler = moneyHandler;
		this.respawnLocation = respawnLocation;
		this.characterInventory = characterInventory;
		this.exp = exp;
		this.level = level;
		this.damage = damage;
		this.armor = armor;
	}
}
