package de.rpgstupe.rpgplugin.player.inventory;

import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.Main;
import de.rpgstupe.rpgplugin.SpecialItemCreation;
import de.rpgstupe.rpgplugin.configuration.ConfigHandler;
import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;
import de.rpgstupe.rpgplugin.exception.NoSuchPlayerInWrapperListException;
import de.rpgstupe.rpgplugin.player.PlayerWrapper;

public class CharacterInventoryHandler implements Listener {

	/**
	 * this class handles the players inventory by writing into the fake
	 * inventory and putting items (e.g. money) into fixed slots
	 * 
	 * @param pic
	 * 
	 * @param plugin
	 */
	public CharacterInventoryHandler() {
	}

	/**
	 * workaround for getting the players inventory opening event (clientside).
	 * the "open inventory" achievement is deleted onPlayerJoin. when the player
	 * gets the achievement (opening the inventory) this event gets fired
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryOpenEventAchievement(PlayerAchievementAwardedEvent event) {
		Player p = event.getPlayer();
		if (event.getAchievement().equals(Achievement.OPEN_INVENTORY)) {
			try {
				event.setCancelled(true);
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				if (!pw.isDisableFakeInventory()) {
					pw.setInventoryOpen(true);
					pw.updatePlayerInventory(0, p.getInventory().getContents().length);
				}
			} catch (NoSuchPlayerInWrapperListException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * only allow chest inventory to be opened by the player (no workbench etc.)
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if (!InventoryType.CHEST.equals(event.getView().getType())) {
			event.setCancelled(true);
		}
	}

	/**
	 * replace the FakeInventory with the ingame inventory if it is closed.
	 * Clear the ingame inventory afterwards
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();

		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
			if (!pw.isDisableFakeInventory()) {
				if (pw.isInventoryOpen()) {
					pw.getActiveInventory().setComplete(p.getInventory().getContents());
					clearPlayerInventory(p, 9, 35);
				}
				pw.setInventoryOpen(false);
				if (!Material.AIR.equals(event.getPlayer().getItemOnCursor().getType())) {
					pw.setDropNextItem(true);
				}
			}
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	/**
	 * do not allow player to place items in the survival crafting slots by
	 * clicking
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
			if (!pw.isDisableFakeInventory()) {
				if (event.getRawSlot() >= 1 && event.getRawSlot() <= 4) {
					event.setCancelled(true);
				}
				if (ClickType.SHIFT_LEFT.equals(event.getClick()) || ClickType.SHIFT_RIGHT.equals(event.getClick())) {
					event.setCancelled(true);
				}
				if (ConfigHandler.moneySlotsUsed) {
					if (ConfigHandler.moneySmallSlot == event.getSlot()
							|| ConfigHandler.moneyMediumSlot == event.getSlot()
							|| ConfigHandler.moneyLargeSlot == event.getSlot()) {
						event.setCancelled(true);
					}
				}
				if (pw.getActiveInventory().getIndex(event.getSlot()).getBoolean("notmoveable")) {
					event.setCancelled(true);
				}

			}
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onplayerInteraction(PlayerInteractEvent event) {
		Player p = event.getPlayer();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			CustomItemStack stackInHand = new CustomItemStack(p.getInventory().getItemInMainHand());
			if (stackInHand.hasKey("type")) {
				switch (stackInHand.getString("type")) {
				case "compassmenu":
					// TODO COMPASSMENU
					break;
				case "teleporter":
					// TODO TELEPORTER
					break;
				}
			}
		}
	}

	/**
	 * do not allow player to place items in the survival crafting slots by
	 * dragging
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Player p = (Player) event.getWhoClicked();
		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
			if (!pw.isDisableFakeInventory()) {
				if (event.getRawSlots().contains(1) || event.getRawSlots().contains(2)
						|| event.getRawSlots().contains(3) || event.getRawSlots().contains(4)) {
					event.setCancelled(true);
				}
				if (ConfigHandler.moneySlotsUsed) {
					if (event.getInventorySlots().contains(ConfigHandler.moneySmallSlot)
							|| event.getInventorySlots().contains(ConfigHandler.moneyMediumSlot)
							|| event.getInventorySlots().contains(ConfigHandler.moneyLargeSlot)) {
						event.setCancelled(true);
					}
				}
			}
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	/**
	 * remove player achievement for opening inventory (needed for
	 * InventoryOpenEvent workaround)
	 * 
	 * add the player to the WrapperList
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		p.removeAchievement(Achievement.OPEN_INVENTORY);
	}

	/**
	 * if an item is about to be picked up it is added into the FakeInventory
	 * and the drop is removed. The inventory needs to be updated after this
	 * 
	 * @param event
	 */
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId());
			if (!pw.isDisableFakeInventory()) {
				event.setCancelled(true);
				if (!pw.isInventoryOpen()) {
					// Special treatment if item is of type money
					if (isItemTypeMoney(event.getItem().getItemStack())) {
						// TODO Does not work as intended when Money does not
						// fit
						// into
						// the inventory
						// ConfigHandler.mergeMoneyIntoPlayerWrapper(pw,
						// ConfigHandler.moneySmallItem.equals(event.getItem().getItemStack().getType().name())
						// ? event.getItem().getItemStack().getAmount() : 0,
						// ConfigHandler.moneyMediumItem.equals(event.getItem().getItemStack().getType().name())
						// ? event.getItem().getItemStack().getAmount() : 0,
						// ConfigHandler.moneyLargeItem.equals(event.getItem().getItemStack().getType().name())
						// ? event.getItem().getItemStack().getAmount() : 0);
						// pw.setMoneyInFakeInv();

						event.getItem().remove();
						// Custom pickup sound
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F,
								4F);
					} else {
						CustomItemStack cStack = new CustomItemStack();
						cStack.setItemStack(event.getItem().getItemStack());
						if (pw.getActiveInventory().isCustomItemStackFitInInventory(cStack)) {
							event.getItem().remove();
							// Custom pickup sound
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F,
									1.2F);
							pw.getActiveInventory().addCustomItemStack(cStack);
							// update only the hotbar
							pw.updatePlayerInventory(0, 9);
						}
					}
				}
			}
		} catch (NoSuchPlayerInWrapperListException | ItemDoesNotFitException e) {
			e.printStackTrace();
		}

	}

	/**
	 * prevent items from being dropped with "q" if the inventory is not open
	 * 
	 * @param event
	 */
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId());
			if (!pw.isDisableFakeInventory()) {
				if (!pw.isInventoryOpen()) {
					if (pw.isDropNextItem()) {
						pw.getActiveInventory().setComplete(event.getPlayer().getInventory().getContents());
						pw.setDropNextItem(false);
					} else {
						event.setCancelled(true);
					}
				} else {
					pw.getActiveInventory().setComplete(event.getPlayer().getInventory().getContents());
				}
			}
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	private boolean isItemTypeMoney(ItemStack stack) {
		String itemName = stack.getType().name();
		if (ConfigHandler.moneySmallItem.equals(itemName) || ConfigHandler.moneyMediumItem.equals(itemName)
				|| ConfigHandler.moneyLargeItem.equals(itemName)) {
			return true;
		}
		return false;
	}

	private void clearPlayerInventory(Player p, int fromId, int toId) {
		Inventory inv = p.getInventory();
		for (int i = fromId; i <= toId; i++) {
			inv.setItem(i, null);
		}
	}
}
