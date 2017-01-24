package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.Achievement;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.rpgstupe.rpgplugin.Main;
import de.rpgstupe.rpgplugin.PlayerWrapper;
import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;
import de.rpgstupe.rpgplugin.exception.NoSuchPlayerInWrapperListException;

public class PlayerInventory implements Listener {

	private final FileConfiguration playerInvConfig;
	private final MoneyStacksManager moneyManager;

	public static int moneySmallInvSlot;
	public static int moneyMediumInvSlot;
	public static int moneyLargeInvSlot;

	JavaPlugin plugin;

	public PlayerInventory(JavaPlugin plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		PlayerInventoryConfig pic = new PlayerInventoryConfig(plugin);
		this.playerInvConfig = pic.getInvConfig();
		loadConfig();
		moneyManager = new MoneyStacksManager(playerInvConfig);
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
		// TODO Plugin für einzelne spieler disablen
		Player p = event.getPlayer();
		if (event.getAchievement().equals(Achievement.OPEN_INVENTORY)) {
			event.setCancelled(true);
			try {
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				pw.setInventoryOpen(true);
				updatePlayerInventory(p, pw, 0, p.getInventory().getContents().length);
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
			pw.setInventoryOpen(false);
			pw.getFakeInventory().setComplete(p.getInventory().getContents());
			clearPlayerInventory(p, 9, 35);
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
		if (event.getRawSlot() >= 1 && event.getRawSlot() <= 4) {
			event.setCancelled(true);
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
		if (event.getRawSlots().contains(1) || event.getRawSlots().contains(2) || event.getRawSlots().contains(3)
				|| event.getRawSlots().contains(4)) {
			event.setCancelled(true);
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

		PlayerWrapper pw = new PlayerWrapper(event.getPlayer());
		Main.PLAYER_WRAPPER_LIST.add(pw);

		// Inventory inv = p.getInventory();
		// ItemStack[] playerContents = inv.getContents();
		//
		// int moneySmallAmount = 0;
		// int moneyMediumAmount = 0;
		// int moneyBigAmount = 0;
		//
		// for (ItemStack stack : playerContents) {
		// if (stack != null) {
		// if (Material.IRON_NUGGET.equals(stack.getType())) {
		// moneySmallAmount += stack.getAmount();
		// inv.remove(stack);
		// } else if (Material.GOLD_NUGGET.equals(stack.getType())) {
		// moneyMediumAmount += stack.getAmount();
		// inv.remove(stack);
		// } else if (Material.GOLD_INGOT.equals(stack.getType())) {
		// moneyBigAmount += stack.getAmount();
		// inv.remove(stack);
		// }
		// }
		// }
		//
		// moneyManager.mergeMoneyAndWriteToPlayerWrapper(pw, moneySmallAmount,
		// moneyMediumAmount, moneyBigAmount);
		//
		// p.getInventory().setItem(0,
		// new
		// ItemStack(Material.getMaterial(MoneyStacksManager.moneySmallItem),
		// pw.getMoneySmallAmount()));
		// p.getInventory().setItem(1,
		// new
		// ItemStack(Material.getMaterial(MoneyStacksManager.moneyMediumItem),
		// pw.getMoneyMediumAmount()));
		// p.getInventory().setItem(2,
		// new
		// ItemStack(Material.getMaterial(MoneyStacksManager.moneyLargeItem),
		// pw.getMoneyLargeAmount()));
	}

	/**
	 * remove player from PlayerWrapperList if he quits
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		try {
			Main.PLAYER_WRAPPER_LIST.remove(
					Main.PLAYER_WRAPPER_LIST.indexOf(Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId())));
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	/**
	 * if an item is about to be picked up it is added into the FakeInventory
	 * and then the drop is removed. The inventory needs to be updated after this
	 * 
	 * @param event
	 */
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
		Player p = event.getPlayer();
		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId());
			if (isItemTypeMoney(event.getItem())) {
				MoneyStacksManager.mergeMoneyAndWriteToPlayerWrapper(pw, MoneyStacksManager.moneySmallItem.equals(event.getItem().getItemStack().getType().name()) ? event.getItem().getItemStack().getAmount() : 0, medium, large);
			} else {
				if (pw.getFakeInventory()
						.isCustomItemStackFitInInventory(new CustomItemStack(event.getItem().getItemStack()))) {
					event.getItem().remove();
					// TODO Tonlage fixen
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.2F);
					pw.getFakeInventory().addCustomItemStack(new CustomItemStack(event.getItem().getItemStack()));

					// update whole inventory if open, otherwise only the hotbar
					if (pw.isInventoryOpen()) {
						updatePlayerInventory(p, pw, 0, p.getInventory().getContents().length);
					} else {
						updatePlayerInventory(p, pw, 0, 8);
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
			if (!pw.isInventoryOpen()) {
				event.setCancelled(true);
			}
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	private boolean isItemTypeMoney(Item item) {
		String itemName = item.getType().name();
		if (MoneyStacksManager.moneySmallItem.equals(itemName)
				|| MoneyStacksManager.moneyMediumItem.equals(itemName)
				|| MoneyStacksManager.moneyLargeItem.equals(itemName)) {
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

	private void updatePlayerInventory(Player p, PlayerWrapper pw, int fromId, int toId) {
		for (int i = fromId; i < toId; i++) {
			if (pw.getFakeInventory().get(i) != null) {
				p.getInventory().setItem(i, new ItemStack(pw.getFakeInventory().get(i)));
			} else {
				p.getInventory().setItem(i, null);
			}
		}
	}

	private void loadConfig() {
		// TODO restliche Config laden
	}
}
