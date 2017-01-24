package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.Achievement;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
	public void onInventoryOpenEvent(PlayerAchievementAwardedEvent event) {
		// TODO Creative hotbar fixen (wahrscheinlich wegen mülleimer slot)
		// TODO Plugin für einzelne spieler disablen
		Player p = event.getPlayer();
		if (event.getAchievement().equals(Achievement.OPEN_INVENTORY)) {
			event.setCancelled(true);
			PlayerWrapper pw;
			try {
				pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				pw.setInventoryOpen(true);
				int counter = 0;
				for (ItemStack stack : pw.getFakeInventory()) {
					if (stack != null) {
						p.getInventory().setItem(counter, new ItemStack(stack));
					}
					counter++;
				}
			} catch (NoSuchPlayerInWrapperListException e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();

		try {

			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
			pw.setInventoryOpen(false);
			pw.getFakeInventory().setComplete(p.getInventory().getContents());

			p.getInventory().clear();
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		p.removeAchievement(Achievement.OPEN_INVENTORY);

		PlayerWrapper pw = new PlayerWrapper(e.getPlayer());
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

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		try {
			Main.PLAYER_WRAPPER_LIST.remove(
					Main.PLAYER_WRAPPER_LIST.indexOf(Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId())));
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		// TODO Item Pickup offenes Inventar
		event.setCancelled(true);
		try {
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId());
			if (pw.getFakeInventory().isCustomItemStackFitInInventory(new CustomItemStack(event.getItem().getItemStack()))) {
				event.getItem().remove();
				//TODO Tonlage
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 6.0F, 5F);
				pw.getFakeInventory().addCustomItemStack(new CustomItemStack(event.getItem().getItemStack()));
				for (int i = 0; i < 9; i++) {
					if (pw.getFakeInventory().get(i) != null) {
						event.getPlayer().getInventory().setItem(i, new ItemStack(pw.getFakeInventory().get(i)));
					} else {
						event.getPlayer().getInventory().setItem(i, null);
					}
				}
			}
		} catch (NoSuchPlayerInWrapperListException | ItemDoesNotFitException e) {
			e.printStackTrace();
		}
	}

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

	private void loadConfig() {
		// TODO restliche Config laden
	}
}
