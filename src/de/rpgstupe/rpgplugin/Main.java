package de.rpgstupe.rpgplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		ItemStack[] itemStack = e.getPlayer().getInventory().getContents();
		System.out.println("Got Inventory");
		int emeralds = 0;
		for (int i = 0; i < itemStack.length; i++) {
			if (itemStack[i] != null) {
				if (itemStack[i].getType().equals(Material.EMERALD)) {
					emeralds += itemStack[i].getAmount();
				}
			}
		}
		System.out.println("emeralds: " + emeralds);

		e.getPlayer().getInventory().setItem(6, new ItemStack(Material.EMERALD, emeralds));

		final Player p = e.getPlayer();
		Server s = this.getServer();
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				Block b = e.getPlayer().getTargetBlock((Set<Material>)null, 5);
				Collection<Entity> lookEntities = e.getPlayer().getWorld().getNearbyEntities(b.getLocation(), 1.5, 2, 1.5);
				for (Entity entity : lookEntities)  {
					if (entity.getType() ==  EntityType.ARMOR_STAND ) {
						entity.setCustomNameVisible(true);
						Collection <Entity> entities = entity.getNearbyEntities(10, 2, 10);
						for (Entity entity2 : entities)  {
							if (entity2.getType() ==  EntityType.ARMOR_STAND ) {
								entity2.setCustomNameVisible(false);
							}
						}
					}
				}
			}
			
		}, 1L, 1L);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("mycommand")) {
			//Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "summon ArmorStand ~ ~ ~ {Invulnerable:1b,NoGravity:1b,Invisible:0,CustomNameVisible:0,CustomName:I AM AWESOME}");
			Player p = (Player) sender;
			// BookUtil.openBook(createBook("HI", "MEGAHIGH"), p);
			// p.getInventory().setItem(1, createMap());
			KrasserZombie z = (KrasserZombie) this.getServer().getWorld("world").spawn(p.getLocation(), KrasserZombie.class);
			z.setCustomName("hi");
			z.setCustomNameVisible(true);
			this.getServer().broadcastMessage("Spawning Zombie with 1 Health");
			return true;
		}
		return false;
	}

	public ItemStack createMap() {
		ItemStack map = new ItemStack(Material.MAP);
		MapMeta mapMeta = (MapMeta) map.getItemMeta();
		mapMeta.setColor(Color.AQUA);
		map.setItemMeta(mapMeta);
		return map;
	}

	public static ItemStack createBook(String title, String author) {
		ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
		bookMeta.setTitle(title);
		bookMeta.setAuthor(author);
		bookMeta.setPages("!TETSTSTSTETSTST");
		writtenBook.setItemMeta(bookMeta);
		List<String> pages = new ArrayList<String>();
		bookMeta.addPage("Hello, welcome to TimeVisualSale's server!"); // Page
																		// 1
		bookMeta.addPage("Website: timevisualsales.com"); // Page 2
		bookMeta.addPage("Hope you enjoy your stay/play!"); // Page 3
		return writtenBook;
	}

}