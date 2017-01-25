package de.rpgstupe.rpgplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import de.rpgstupe.rpgplugin.customentities.CustomVillager;
import de.rpgstupe.rpgplugin.database.DatabaseHandler;
import de.rpgstupe.rpgplugin.database.daos.PlayerDAO;
import de.rpgstupe.rpgplugin.database.entities.PlayerEntity;
import de.rpgstupe.rpgplugin.exception.NoSuchPlayerInWrapperListException;
import de.rpgstupe.rpgplugin.inventory.PlayerInventory;
import de.rpgstupe.rpgplugin.util.BookUtil;
import de.rpgstupe.rpgplugin.util.NMSUtil;
import net.minecraft.server.v1_11_R1.World;

public class Main extends JavaPlugin implements Listener {

	public static final List<PlayerWrapper> PLAYER_WRAPPER_LIST = new ArrayList<PlayerWrapper>();
	Inventory myInventory;
	private static DatabaseHandler dbHandler;

	@Override
	public void onEnable() {
		dbHandler = new DatabaseHandler();

		NMSUtil.registerCustomEntity(120, "villager", CustomVillager.class);
		getServer().getPluginManager().registerEvents(this, this);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
				Arrays.asList(PacketType.Status.Server.SERVER_INFO), ListenerOptions.ASYNC) {
			@Override
			public void onPacketSending(PacketEvent event) {
				handleServerPingAsync(event, event.getPacket().getServerPings().read(0));
			}
		});
		new PlayerInventory(this);
	}

	public void handleServerPingAsync(PacketEvent e, WrappedServerPing ping) {
		ping.setPlayersMaximum(0);
		ping.setPlayersOnline(9000);
		ping.setPlayers(Arrays.asList(new WrappedGameProfile(UUID.randomUUID(), "Krass kek")));
	}

	@Override
	public void onDisable() {

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		System.out.println("Main onJoin");
		PlayerEntity pe = null;//dbHandler.getPlayerEntityByPlayer(event.getPlayer());
		if (pe != null) {
			System.out.println("Added Player with UUID " + event.getPlayer().getUniqueId() + " to List");
			PLAYER_WRAPPER_LIST.add(new PlayerWrapper(event.getPlayer(), pe.moneySmallAmount, pe.moneyMediumAmount, pe.moneyLargeAmount));
		} else {
			Main.PLAYER_WRAPPER_LIST.add(new PlayerWrapper(event.getPlayer()));
			System.out.println("NEW: Added Player with UUID " + event.getPlayer().getUniqueId() + " to List");
		}

		try {
			Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId()).updatePlayerInventory(0, event.getPlayer().getInventory().getContents().length);
		} catch (NoSuchPlayerInWrapperListException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ItemStack[] itemStack = e.getPlayer().getInventory().getContents();
//		int emeralds = 0;
//		for (int i = 0; i < itemStack.length; i++) {
//			if (itemStack[i] != null) {
//				if (itemStack[i].getType().equals(Material.EMERALD)) {
//					emeralds += itemStack[i].getAmount();
//				}
//			}
//		}
//
//		e.getPlayer().getInventory().setItem(6, new ItemStack(Material.EMERALD, emeralds));
//
//		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
//
//			@Override
//			public void run() {
//				// Block b = e.getPlayer().getTargetBlock((Set<Material>) null,
//				// 5);
//				// Collection<Entity> lookEntities =
//				// e.getPlayer().getWorld().getNearbyEntities(b.getLocation(),
//				// 1.5, 2,
//				// 1.5);
//				// for (Entity entity : lookEntities) {
//				// if (entity.getType() == EntityType.ARMOR_STAND) {
//				// entity.setCustomNameVisible(true);
//				// Collection<Entity> entities = entity.getNearbyEntities(10, 2,
//				// 10);
//				// for (Entity entity2 : entities) {
//				// if (entity2.getType() == EntityType.ARMOR_STAND) {
//				// entity2.setCustomNameVisible(false);
//				// }
//				// }
//				// }
//				// }
//			}
//
//		}, 1L, 1L);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		if (command.getName().equalsIgnoreCase("mycommand")) {
			// Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
			// "summon ArmorStand ~ ~ ~
			// {Invulnerable:1b,NoGravity:1b,Invisible:0,CustomNameVisible:0,CustomName:I
			// AM AWESOME}");
			// Location loc = p.getLocation();
			// CustomVillager vill = new CustomVillager(((CraftWorld)
			// getServer().getWorld("world")).getHandle(), 5);
			// vill.setHealthPoints(20);
			// vill.setLocation(loc.getX(), loc.getY(), loc.getZ(),
			// loc.getYaw(), loc.getPitch());
			// vill.setCustomName("test");
			// ((CraftWorld)
			// getServer().getWorld("world")).getHandle().addEntity(vill);
			ItemStack stack = new ItemStack(Material.GOLD_SWORD);
			ItemMeta meta = stack.getItemMeta();
			meta.setUnbreakable(true);
			stack.setDurability((short) 1);
			stack.setItemMeta(meta);
			p.getInventory().setHelmet(stack);
			p.getInventory().setChestplate(stack);
			p.getInventory().setLeggings(stack);
			p.getInventory().setBoots(stack);
			return true;
		} else if (command.getName().equalsIgnoreCase("myinventory")) {
			myInventory = Bukkit.createInventory(null, 9, "Custom inventory Menu Kompassmenü KEK!");
			myInventory.setItem(0, new ItemStack(Material.DIRT, 1));
			myInventory.setItem(7, new ItemStack(Material.BOOK, 1));
			p.openInventory(myInventory);
		} else if (command.getName().equalsIgnoreCase("myskill")) {
			p.sendMessage("Click 3 Mouse Buttons kek");
			// PlayerWrapper testPlayer = new PlayerWrapper(p);
			// playerWrapperList.add(testPlayer);
		} else if (command.getName().equalsIgnoreCase("mydamagetext")) {
			damageText(p);

		} else if (command.getName().equalsIgnoreCase("killall")) {
			Collection<Entity> coll = p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 50, 50, 50);
			for (Entity e : coll) {
				if (e instanceof Bat) {
					((Monster) e).setHealth(0);
				}
			}
		}
		return false;
	}

	private void damageText(Entity entity) {
		if (!EntityType.ARMOR_STAND.equals(entity.getType())) {
			ArmorStand stand = entity.getLocation().getWorld()
					.spawn(new Location(entity.getWorld(), entity.getLocation().getX() + ((Math.random() * 2) - 1),
							entity.getLocation().getY() + ((Math.random() * 2)),
							entity.getLocation().getZ() + ((Math.random() * 2) - 1)), ArmorStand.class);
			stand.setMarker(true);
			stand.setGravity(true);
			stand.setCustomName("§c" + (int) ((LivingEntity) entity).getHealth());
			stand.setCustomNameVisible(true);
			stand.setVisible(false);
			stand.setInvulnerable(true);

			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				int counter = 0;

				@Override
				public void run() {
					if (counter == 2000) {
						stand.remove();
					}
					counter++;
					stand.setVelocity(new Vector(0, 0.07, 0));
				}

			}, 0, 1L);
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		for (PlayerWrapper testPlayer : PLAYER_WRAPPER_LIST) {
			if (testPlayer.getUniqueId().equals(event.getPlayer().getUniqueId())) {
				// if (event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
				// testPlayer.castSpell) {
				// testPlayer.sendMessage("You clicked kek. Nomma geht erst nach
				// casten hoffe ich...");
				// testPlayer.castSpell = false;
				// }
			}
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Location loc = event.getClickedBlock().getLocation();
			loc.setY(loc.getY() - 1);
			if (event.getClickedBlock().getType().equals(Material.GLASS)) {
				event.getPlayer().sendBlockChange(loc, Material.STAINED_GLASS, (byte) 5);
			} else if (event.getClickedBlock().getType().equals(Material.STAINED_GLASS)) {
				event.getPlayer().sendBlockChange(loc, Material.GLASS, (byte) 0);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (myInventory != null) {
			Player player = (Player) event.getWhoClicked();
			ItemStack clicked = event.getCurrentItem();
			Inventory inventory = event.getInventory();
			if (inventory.getName().equals(myInventory.getName())) {
				if (clicked.getType() == Material.DIRT) {
					event.setCancelled(true);
					player.closeInventory();
					player.getInventory().addItem(new ItemStack(Material.DIRT, 1));
				} else if (clicked.getType() == Material.BOOK) {
					event.setCancelled(true);
					player.closeInventory();
					openCustomBook(player);
				}
			}
		}
	}

	public void openCustomBook(Player p) {
		BookUtil.openBook(createBook("HI", "MEGAHIGH"), p);
	}

	public void spawnCustomEntity(Location loc) {
		World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();

		CustomVillager vill = new CustomVillager(nmsWorld, 5);

		vill.setPosition(loc.getX(), loc.getY(), loc.getZ());
		vill.setHealthPoints(100);
		nmsWorld.addEntity(vill);
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damagee = event.getEntity();

		if (damager instanceof Player) {
			damageText(event.getEntity());
			if (damagee instanceof CraftArmorStand) {
				this.getServer().broadcastMessage("Cancel kek " + damagee.toString());
			}
			if (((CraftEntity) damagee).getHandle() instanceof CustomVillager) {
				CustomVillager vill = (CustomVillager) ((CraftEntity) damagee).getHandle();
				vill.applyDamage(20 - vill.getArmor());
				event.setDamage(0);

			}
		}
		event.setDamage(1);
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

	public static PlayerWrapper getPlayerWrapperFromUUID(UUID uuid) throws NoSuchPlayerInWrapperListException {
		System.out.println(PLAYER_WRAPPER_LIST.size());
		for (PlayerWrapper pw : Main.PLAYER_WRAPPER_LIST) {
			if (pw != null && uuid.equals(pw.getUniqueId())) {
				return pw;
			}
		}
		throw new NoSuchPlayerInWrapperListException();
	}

	public static DatabaseHandler getDbHandler() {
		return dbHandler;
	}

}