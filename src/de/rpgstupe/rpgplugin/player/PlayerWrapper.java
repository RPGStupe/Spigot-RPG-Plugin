package de.rpgstupe.rpgplugin.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.rpgstupe.rpgplugin.player.inventory.FakeInventory;

public class PlayerWrapper {

	private Player player;
	private Set<Character> characters;
	private Character activeCharacter;
	private boolean dropNextItem = false;
	private boolean inventoryOpen = false;
	private FakeInventory buildInventory;
	private boolean buildInventoryActive;

	// Constructor to pass on the player object
	public PlayerWrapper(Player player) {
		this(player, new HashSet<Character>(), new FakeInventory(player.getInventory().getSize()));
	}

	public PlayerWrapper(Player player, Set<Character> characters, FakeInventory buildInventory) {
		this.player = player;
		this.characters = characters;
		this.buildInventory = buildInventory;
	}

	public void updatePlayerInventory(int fromId, int toId) {
		for (int i = fromId; i < toId; i++) {
			player.getInventory().setItem(i, buildInventory.getInventoryArray()[i] == null ? null
					: buildInventory.getInventoryArray()[i].getItemStack());
		}
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	public Player getPlayer() {
		return player;
	}

	public Set<Character> getCharacters() {
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

	public FakeInventory getBuildInventory() {
		return buildInventory;
	}

	public Character getActiveCharacter() {
		return activeCharacter;
	}

	public void setActiveCharacter(Character activeCharacter) {
		this.activeCharacter = activeCharacter;
	}

	public boolean isBuildInventoryActive() {
		return buildInventoryActive;
	}

	public void setBuildInventoryActive(boolean buildInventoryActive) {
		this.buildInventoryActive = buildInventoryActive;
	}
}