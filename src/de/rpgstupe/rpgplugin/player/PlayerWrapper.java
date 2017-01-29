package de.rpgstupe.rpgplugin.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.inventorymenu.EnumInventoryMenu;
import de.rpgstupe.rpgplugin.inventorymenu.InventoryMenu;
import de.rpgstupe.rpgplugin.player.inventory.FakeInventory;

public class PlayerWrapper {
	public InventoryMenu getCharacterSelect() {
		return characterSelect;
	}

	public void setCharacterSelect(InventoryMenu characterSelect) {
		this.characterSelect = characterSelect;
	}

	private Player player;
	private Map<Integer, Character> characters;
	private int activeCharacter;
	private boolean dropNextItem = false;
	private boolean inventoryOpen = false;
	private FakeInventory buildInventory;
	private boolean buildInventoryActive;
	private boolean disableFakeInventory;
	private InventoryMenu characterSelect;
	private FakeInventory activeInventory;

	// Constructor to pass on the player object
	public PlayerWrapper(Player player) {
		this(player, new HashMap<Integer, Character>(), new FakeInventory(player.getInventory().getSize()), true, false,
				0);
	}

	public PlayerWrapper(Player player, Map<Integer, Character> characters, FakeInventory buildInventory,
			boolean buildInventoryActive, boolean disableFakeInventory, int activeCharacter) {
		this.player = player;
		this.characters = characters;
		this.buildInventory = buildInventory;
		this.buildInventoryActive = buildInventoryActive;
		this.disableFakeInventory = disableFakeInventory;
		this.activeCharacter = activeCharacter;
		if (this.buildInventoryActive) {
			this.setActiveInventory(this.buildInventory);
		} else {
			this.setActiveInventory(this.characters.get(this.activeCharacter).getCharacterInventory());
		}
	}

	public void updatePlayerInventory(int fromId, int toId) {
		for (int i = fromId; i < toId; i++) {
			player.getInventory().setItem(i, activeInventory.getInventoryArray()[i] == null ? null
					: activeInventory.getInventoryArray()[i].getItemStack());
		}
	}

	public void changeCharacter(int key) {
		this.activeCharacter = key;
		if (this.buildInventoryActive) {
			this.setActiveInventory(this.buildInventory);
		} else {
			this.setActiveInventory(this.characters.get(this.activeCharacter).getCharacterInventory());
		}
		updatePlayerInventory(0, getActiveInventory().getInventorySize());
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	public Player getPlayer() {
		return player;
	}

	public Map<Integer, Character> getCharacters() {
		return characters;
	}

	public boolean isDropNextItem() {
		return dropNextItem;
	}

	public void setDropNextItem(boolean dropNextItem) {
		this.dropNextItem = dropNextItem;
	}

	public boolean isInventoryOpen() {
		return inventoryOpen;
	}

	public void setInventoryOpen(boolean inventoryOpen) {
		this.inventoryOpen = inventoryOpen;
	}

	public int getActiveCharacter() {
		return activeCharacter;
	}

	public void setActiveCharacter(int activeCharacter) {
		this.activeCharacter = activeCharacter;
	}

	public boolean isDisableFakeInventory() {
		return disableFakeInventory;
	}

	public void setDisableFakeInventory(boolean disableFakeInventory) {
		this.disableFakeInventory = disableFakeInventory;
	}

	public FakeInventory getBuildInventory() {
		return buildInventory;
	}

	public boolean isBuildInventoryActive() {
		return buildInventoryActive;
	}

	public void setBuildInventoryActive(boolean buildInventoryActive) {
		this.buildInventoryActive = buildInventoryActive;
		if (this.buildInventoryActive) {
			this.setActiveInventory(this.buildInventory);
		} else {
			this.setActiveInventory(this.characters.get(this.activeCharacter).getCharacterInventory());
		}
		updatePlayerInventory(0, getActiveInventory().getInventorySize());
	}

	public FakeInventory getActiveInventory() {
		return activeInventory;
	}

	public void setActiveInventory(FakeInventory activeInventory) {
		this.activeInventory = activeInventory;
	}
}