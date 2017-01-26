package de.rpgstupe.rpgplugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import de.rpgstupe.rpgplugin.database.entities.CustomItemStackEntity;
import de.rpgstupe.rpgplugin.database.entities.DataEntity;
import de.rpgstupe.rpgplugin.database.entities.ItemMetaEntity;
import de.rpgstupe.rpgplugin.inventory.CustomItemStack;
import de.rpgstupe.rpgplugin.inventory.FakeInventory;
import de.rpgstupe.rpgplugin.inventory.MoneyStacksHandler;

public class PlayerWrapper {

	public boolean isDropNextItem() {
		return dropNextItem;
	}

	public void setDropNextItem(boolean dropNextItem) {
		this.dropNextItem = dropNextItem;
	}

	// Create a variable to store the player
	private Player player;

	private int moneySmallAmount;
	private int moneyMediumAmount;
	private int moneyLargeAmount;

	private Location respawnLocation;
	
	private boolean isInventoryOpen = false;
	
	private FakeInventory fakeInventory;

	private boolean dropNextItem = false;
	
	// Constructor to pass on the player object
	public PlayerWrapper(Player player) {
		// Store the player object
		this.player = player;

		this.moneySmallAmount = 0;
		this.moneyMediumAmount = 0;
		this.moneyLargeAmount = 0;

		this.respawnLocation = new Location(Bukkit.getWorld("world"), 4000, 100, 4000);
		
		this.fakeInventory = new FakeInventory(41);
	}

	public PlayerWrapper(Player player, int moneySmallAmount, int moneyMediumAmount, int moneyLargeAmount,
			CustomItemStackEntity[] fakeInv, Location respawnLocation) {
		this.player = player;

		this.moneySmallAmount = moneySmallAmount;
		this.moneyMediumAmount = moneyMediumAmount;
		this.moneyLargeAmount = moneyLargeAmount;

		this.respawnLocation = respawnLocation;
		
		ItemStack[] stack = new ItemStack[fakeInv.length];
		for (int i = 0; i < fakeInv.length; i++) {
			if (fakeInv[i] != null) {
				ItemStack s = new ItemStack(fakeInv[i].type, fakeInv[i].amount, fakeInv[i].durability);
				MaterialData md = new MaterialData(fakeInv[i].data.type);
				s.setData(md);
				stack[i] = s;
			} else {
				stack[i] = null;
			}
		}
		this.fakeInventory = new FakeInventory(fakeInv.length);
		this.fakeInventory.setComplete(stack);
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

		CustomItemStack cStackMoneySmall = new CustomItemStack();
		CustomItemStack cStackMoneyMedium = new CustomItemStack();
		CustomItemStack cStackMoneyLarge = new CustomItemStack();

		cStackMoneySmall.setItemStack(
				new ItemStack(Material.getMaterial(MoneyStacksHandler.moneySmallItem), this.moneySmallAmount));
		cStackMoneyMedium.setItemStack(
				new ItemStack(Material.getMaterial(MoneyStacksHandler.moneyMediumItem), this.moneyMediumAmount));
		cStackMoneyLarge.setItemStack(
				new ItemStack(Material.getMaterial(MoneyStacksHandler.moneyLargeItem), this.moneyLargeAmount));

		this.getFakeInventory().getFakeInventoryArray()[MoneyStacksHandler.moneySmallSlot] = cStackMoneySmall;
		this.getFakeInventory().getFakeInventoryArray()[MoneyStacksHandler.moneyMediumSlot] = cStackMoneyMedium;
		this.getFakeInventory().getFakeInventoryArray()[MoneyStacksHandler.moneyLargeSlot] = cStackMoneyLarge;
	}

	public void updatePlayerInventory(int fromId, int toId) {
		for (int i = fromId; i < toId; i++) {
			player.getInventory().setItem(i, getFakeInventory().getFakeInventoryArray()[i] == null ? null
					: getFakeInventory().getFakeInventoryArray()[i].getItemStack());
		}

	}

	public boolean isInventoryOpen() {
		return isInventoryOpen;
	}

	public void setInventoryOpen(boolean isInventoryOpen) {
		this.isInventoryOpen = isInventoryOpen;
	}

	public CustomItemStackEntity[] invToArray() {
		CustomItemStackEntity[] stackArrayEntity = new CustomItemStackEntity[this.getFakeInventory()
				.getFakeInventoryArray().length];
		for (int i = 0; i < this.getFakeInventory().getFakeInventoryArray().length; i++) {
			
			CustomItemStackEntity tempStackEntity = itemStackToCustomItemStackEntity(
					this.getFakeInventory().getFakeInventoryArray()[i] == null ? null
							: getFakeInventory().getFakeInventoryArray()[i].getItemStack());
			
			
			stackArrayEntity[i] = tempStackEntity;
		}

		return stackArrayEntity;
	}

	private CustomItemStackEntity itemStackToCustomItemStackEntity(ItemStack itemStack) {

		if (itemStack == null) {
			return null;
		}
		return new CustomItemStackEntity(itemStack.getType(), itemStack.getAmount(), new DataEntity(itemStack.getData() == null ? null : itemStack.getData().getItemType()), itemStack.getDurability(), new ItemMetaEntity());
	}
}