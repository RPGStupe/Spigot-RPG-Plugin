package de.rpgstupe.rpgplugin;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Embedded;

import de.rpgstupe.rpgplugin.inventory.CustomItemStack;
import de.rpgstupe.rpgplugin.inventory.FakeInventory;
import de.rpgstupe.rpgplugin.inventory.MoneyStacksManager;

@Embedded
public class PlayerWrapper {

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

		this.moneySmallAmount = 0;
		this.moneyMediumAmount = 0;
		this.moneyLargeAmount = 0;

		this.fakeInventory = new FakeInventory(41);
	}

	public PlayerWrapper(Player player, int moneySmallAmount, int moneyMediumAmount, int moneyLargeAmount) {
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

	public void setMoneyInFakeInv() {

		this.getFakeInventory().set(MoneyStacksManager.moneySmallSlot, new CustomItemStack(
				new ItemStack(Material.getMaterial(MoneyStacksManager.moneySmallItem), this.moneySmallAmount)));
		System.out.println(
				"Added " + this.moneySmallAmount + " small Money to slot " + MoneyStacksManager.moneySmallSlot);
		this.getFakeInventory().set(MoneyStacksManager.moneyMediumSlot, new CustomItemStack(
				new ItemStack(Material.getMaterial(MoneyStacksManager.moneyMediumItem), this.moneyMediumAmount)));
		System.out.println(
				"Added " + this.moneyMediumAmount + " small Money to slot " + MoneyStacksManager.moneyMediumSlot);

		this.getFakeInventory().set(MoneyStacksManager.moneyLargeSlot, new CustomItemStack(
				new ItemStack(Material.getMaterial(MoneyStacksManager.moneyLargeItem), this.moneyLargeAmount)));
		System.out.println(
				"Added " + this.moneyLargeAmount + " small Money to slot " + MoneyStacksManager.moneyLargeSlot);
	}
	
	public void updatePlayerInventory(int fromId, int toId) {
		for (int i = fromId; i < toId; i++) {
			if (getFakeInventory().get(i) != null) {
				player.getInventory().setItem(i, new ItemStack(getFakeInventory().get(i)));
			} else {
				player.getInventory().setItem(i, null);
			}
		}
	}
	
	public boolean isInventoryOpen() {
		return isInventoryOpen;
	}

	public void setInventoryOpen(boolean isInventoryOpen) {
		this.isInventoryOpen = isInventoryOpen;
	}
}