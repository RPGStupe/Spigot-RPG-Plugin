package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.rpgstupe.rpgplugin.Main;
import de.rpgstupe.rpgplugin.PlayerWrapper;

public class PlayerInventory implements Listener {

	private final FileConfiguration playerInvConfig;

	private int maxMoneyStackSmall;
	private int maxMoneyStackMedium;
	private int maxMoneyStackLarge;

	public PlayerInventory(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		PlayerInventoryConfig pic = new PlayerInventoryConfig(plugin);
		this.playerInvConfig = pic.getInvConfig();
		loadConfig();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerWrapper pw = new PlayerWrapper(e.getPlayer());
		Main.PLAYER_WRAPPER_LIST.add(pw);

		Inventory inv = p.getInventory();
		ItemStack[] playerContents = inv.getContents();

		int moneySmallAmount = 0;
		int moneyMediumAmount = 0;
		int moneyBigAmount = 0;

		for (ItemStack stack : playerContents) {
			if (stack != null) {
				if (Material.IRON_NUGGET.equals(stack.getType())) {
					moneySmallAmount += stack.getAmount();
					inv.remove(stack);
				} else if (Material.GOLD_NUGGET.equals(stack.getType())) {
					moneyMediumAmount += stack.getAmount();
					inv.remove(stack);
				} else if (Material.GOLD_INGOT.equals(stack.getType())) {
					moneyBigAmount += stack.getAmount();
					inv.remove(stack);
				}
			}
		}

		mergeMoneyAndWriteToPlayerWrapper(pw, moneySmallAmount, moneyMediumAmount, moneyBigAmount);

		p.getInventory().setItem(0, new ItemStack(Material.IRON_NUGGET, pw.getMoneySmallAmount()));
		p.getInventory().setItem(1, new ItemStack(Material.GOLD_NUGGET, pw.getMoneyMediumAmount()));
		p.getInventory().setItem(2, new ItemStack(Material.GOLD_INGOT, pw.getMoneyBigAmount()));
	}

	private void loadConfig() {
		//TODO restliche Config laden
		maxMoneyStackSmall = playerInvConfig.getInt("money.exchangerate.small_to_medium");
		maxMoneyStackMedium = playerInvConfig.getInt("money.exchangerate.medium_to_large");
		maxMoneyStackLarge = playerInvConfig.getInt("money.exchangerate.max_amount_large");
	}
	
	private void mergeMoneyAndWriteToPlayerWrapper(PlayerWrapper pw, int small, int medium, int big) {

		int tempSmall = small;
		int tempMedium = medium;
		int tempBig = big;

		if (tempSmall >= maxMoneyStackSmall) {
			tempSmall %= maxMoneyStackSmall;
			tempMedium += small / maxMoneyStackSmall;
		}

		if (tempMedium >= maxMoneyStackMedium) {
			tempMedium %= maxMoneyStackMedium;
			tempBig += medium / maxMoneyStackMedium;
		}

		if (tempBig > maxMoneyStackLarge) {
			tempBig = maxMoneyStackLarge;
		}

		pw.setMoneySmallAmount(tempSmall);
		pw.setMoneyMediumAmount(tempMedium);
		pw.setMoneyBigAmount(tempBig);
	}
}
