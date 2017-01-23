package de.rpgstupe.rpgplugin;

import java.util.UUID;

import org.bukkit.entity.Player;

import de.rpgstupe.rpgplugin.inventory.FakeInventory;

public class PlayerWrapper {

	public boolean isInventoryOpen() {
		return isInventoryOpen;
	}

	public void setInventoryOpen(boolean isInventoryOpen) {
		this.isInventoryOpen = isInventoryOpen;
	}

	// Create a variable to store the player
	private Player player;

	private int moneySmallAmount;
	private int moneyMediumAmount;
	private int moneyLargeAmount;
	
	private boolean isInventoryOpen = false;
	
	private FakeInventory fakeInventory;

	// Constructor to pass on the player object
	public PlayerWrapper(Player player) {
		// Store the player object
		this.player = player;
		
		this.moneySmallAmount = moneySmallAmount;
		this.moneyMediumAmount = moneyMediumAmount;
		this.moneyLargeAmount = moneyLargeAmount;
		
		this.fakeInventory = new FakeInventory(41);
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

	public int getMoneyLargeAmount() {
		return moneyLargeAmount;
	}

	public void setMoneyLargeAmount(int moneyBigAmount) {
		this.moneyLargeAmount = moneyBigAmount;
	}
	
	public FakeInventory getFakeInventory() {
		return fakeInventory;
	}
}