package de.rpgstupe.rpgplugin.player;

import org.bukkit.Location;

import de.rpgstupe.rpgplugin.RPGClass;
import de.rpgstupe.rpgplugin.SpecialItemCreation;
import de.rpgstupe.rpgplugin.player.inventory.FakeInventory;

public class Character {
	private MoneyHandler moneyHandler;
	private Location respawnLocation;
	private FakeInventory characterInventory;
	private RPGClass rpgclass;

	private int exp;
	private int level;
	private double damage;
	private double armor;
	
	public Character(MoneyHandler moneyHandler, Location respawnLocation, FakeInventory characterInventory, RPGClass rpgclass) {
		this(moneyHandler, respawnLocation, characterInventory, rpgclass, 0, 1, 0, 0);
	}
	
	public Character(MoneyHandler moneyHandler, Location respawnLocation, FakeInventory characterInventory, RPGClass rpgclass, int exp, int level,
			double damage, double armor) {
		this.moneyHandler = moneyHandler;
		this.respawnLocation = respawnLocation;
		this.characterInventory = characterInventory;
		this.rpgclass = rpgclass;
		this.exp = exp;
		this.level = level;
		this.damage = damage;
		this.armor = armor;
		this.characterInventory.set(7, SpecialItemCreation.createHearthstone());
		this.characterInventory.set(8, SpecialItemCreation.createCompassMenu());
	}

	public MoneyHandler getMoneyHandler() {
		return moneyHandler;
	}

	public Location getRespawnLocation() {
		return respawnLocation;
	}

	public FakeInventory getCharacterInventory() {
		return characterInventory;
	}

	public RPGClass getRpgclass() {
		return rpgclass;
	}

	public int getExp() {
		return exp;
	}

	public int getLevel() {
		return level;
	}

	public double getDamage() {
		return damage;
	}

	public double getArmor() {
		return armor;
	}
}
