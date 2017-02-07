package de.rpgstupe.rpgplugin.database.entities;

import org.mongodb.morphia.annotations.Id;
import de.rpgstupe.rpgplugin.player.Character;

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

	public Character toCharacter() {
		Character character = new Character(moneyHandler.toMoneyHandler(), respawnLocation.toLocation(), characterInventory.toFakeInventory(), null, null, exp, level, damage, armor);
		return character;
	}
}
