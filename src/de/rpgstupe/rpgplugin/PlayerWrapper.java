package de.rpgstupe.rpgplugin;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerWrapper {

	// Create a variable to store the player
	private Player player;

	private int moneySmallAmount;
	private int moneyMediumAmount;
	private int moneyBigAmount;

	// Constructor to pass on the player object
	public PlayerWrapper(Player player) {
		// Store the player object
		this.player = player;
		
		this.moneySmallAmount = moneySmallAmount;
		this.moneyMediumAmount = moneyMediumAmount;
		this.moneyBigAmount = moneyBigAmount;
	}

	// Getter to get the player object
	public Player getPlayer() {
		return player;
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	public int getMoneySmallAmount() {
		return moneySmallAmount;
	}

	public void setMoneySmallAmount(int moneySmallAmount) {
		this.moneySmallAmount = moneySmallAmount;
	}

	public int getMoneyMediumAmount() {
		return moneyMediumAmount;
	}

	public void setMoneyMediumAmount(int moneyMediumAmount) {
		this.moneyMediumAmount = moneyMediumAmount;
	}

	public int getMoneyBigAmount() {
		return moneyBigAmount;
	}

	public void setMoneyBigAmount(int moneyBigAmount) {
		this.moneyBigAmount = moneyBigAmount;
	}
}