package de.rpgstupe.rpgplugin.player;

import org.bukkit.Location;

import de.rpgstupe.rpgplugin.RPGClass;
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
