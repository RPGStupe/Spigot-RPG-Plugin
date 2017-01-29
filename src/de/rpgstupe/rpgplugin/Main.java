package de.rpgstupe.rpgplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import de.rpgstupe.rpgplugin.configuration.ConfigHandler;
import de.rpgstupe.rpgplugin.customentities.CustomVillager;
import de.rpgstupe.rpgplugin.database.DatabaseHandler;
import de.rpgstupe.rpgplugin.database.entities.CharacterEntity;
import de.rpgstupe.rpgplugin.database.entities.FakeInventoryEntity;
import de.rpgstupe.rpgplugin.database.entities.LocationEntity;
import de.rpgstupe.rpgplugin.database.entities.MoneyHandlerEntity;
import de.rpgstupe.rpgplugin.database.entities.PlayerWrapperEntity;
import de.rpgstupe.rpgplugin.exception.NoSuchPlayerInWrapperListException;
import de.rpgstupe.rpgplugin.inventorymenu.InventoryMenu;
import de.rpgstupe.rpgplugin.player.Character;
import de.rpgstupe.rpgplugin.player.MoneyHandler;
import de.rpgstupe.rpgplugin.player.PlayerWrapper;
import de.rpgstupe.rpgplugin.player.inventory.CharacterInventoryHandler;
import de.rpgstupe.rpgplugin.player.inventory.FakeInventory;
import de.rpgstupe.rpgplugin.util.BookUtil;
import de.rpgstupe.rpgplugin.util.NMSUtil;
import net.minecraft.server.v1_11_R1.World;

public class Main extends JavaPlugin implements Listener {

	public static final List<PlayerWrapper> PLAYER_WRAPPER_LIST = new ArrayList<PlayerWrapper>();
	Inventory myInventory;
	private static DatabaseHandler dbHandler;
	private CharacterInventoryHandler piHandler;

	/**
	 * Setup everything needed to run the plugin
	 * 
	 * - registering event classes - register custom entities - add all the
	 * online players to the wrapperlist (/reload clears the list)
	 */
	@Override
	public void onEnable() {
		dbHandler = new DatabaseHandler();

		NMSUtil.registerCustomEntity(120, "villager", CustomVillager.class);

		getServer().getPluginManager().registerEvents(this, this);

		new ConfigHandler(this);
		piHandler = new CharacterInventoryHandler();

		getServer().getPluginManager().registerEvents(piHandler, this);
		addPlayersToWrapperList();
	}

	/**
	 * store all the data from online players into the database and remove them
	 * from wrapperlist
	 */
	@Override
	public void onDisable() {
		Collection<? extends Player> playerCollection = Bukkit.getOnlinePlayers();
		for (Player p : playerCollection) {
			try {
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());

				writePlayerDataIntoDatabase(p, pw);

				Main.PLAYER_WRAPPER_LIST
						.remove(Main.PLAYER_WRAPPER_LIST.indexOf(Main.getPlayerWrapperFromUUID(p.getUniqueId())));
			} catch (NoSuchPlayerInWrapperListException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * load player from database (or create new if not existing) and update the
	 * inventory
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		addPlayerFromPlayerEntity(event.getPlayer());
		try {
			Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId()).updatePlayerInventory(0,
					event.getPlayer().getInventory().getContents().length);
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}

		//
		// this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new
		// Runnable() {
		//
		// @Override
		// public void run() {
		// // Block b = e.getPlayer().getTargetBlock((Set<Material>) null,
		// // 5);
		// // Collection<Entity> lookEntities =
		// // e.getPlayer().getWorld().getNearbyEntities(b.getLocation(),
		// // 1.5, 2,
		// // 1.5);
		// // for (Entity entity : lookEntities) {
		// // if (entity.getType() == EntityType.ARMOR_STAND) {
		// // entity.setCustomNameVisible(true);
		// // Collection<Entity> entities = entity.getNearbyEntities(10, 2,
		// // 10);
		// // for (Entity entity2 : entities) {
		// // if (entity2.getType() == EntityType.ARMOR_STAND) {
		// // entity2.setCustomNameVisible(false);
		// // }
		// // }
		// // }
		// // }
		// }
		//
		// }, 1L, 1L);
	}

	/**
	 * remove player from PlayerWrapperList if he quits and write the playerdata
	 * to the database
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		try {
			Player p = event.getPlayer();
			PlayerWrapper pw = Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId());

			writePlayerDataIntoDatabase(p, pw);

			Main.PLAYER_WRAPPER_LIST.remove(
					Main.PLAYER_WRAPPER_LIST.indexOf(Main.getPlayerWrapperFromUUID(event.getPlayer().getUniqueId())));
		} catch (NoSuchPlayerInWrapperListException e) {
			e.printStackTrace();
		}
	}

	/**
	 * handling the commands
	 */
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
		} else if (command.getName().equalsIgnoreCase("rpgclearinv")) {
			// try {
			// Main.getPlayerWrapperFromUUID(p.getUniqueId()).getFakeInventory().setComplete(new
			// ItemStack[p.getInventory().getSize()]);
			// } catch (NoSuchPlayerInWrapperListException e) {
			// e.printStackTrace();
			// }
		} else if (command.getName().equalsIgnoreCase("rpginventory")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("disable")) {
					try {
						PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
						pw.setDisableFakeInventory(true);
					} catch (NoSuchPlayerInWrapperListException e) {
						e.printStackTrace();
					}
				} else if (args[0].equalsIgnoreCase("enable")) {
					try {
						PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
						pw.setDisableFakeInventory(false);
					} catch (NoSuchPlayerInWrapperListException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (command.getName().equalsIgnoreCase("rpgcreate")) {
			try {
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				pw.getCharacters().put(pw.getCharacters().size(), new Character(new MoneyHandler(),
						ConfigHandler.spawnLocation, new FakeInventory(pw.getPlayer().getInventory().getSize()), null));
			} catch (NoSuchPlayerInWrapperListException e) {
				e.printStackTrace();
			}
		} else if (command.getName().equalsIgnoreCase("rpgchangecharacter")) {
			try {
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				int num = Integer.parseInt(args[0]);
				pw.changeCharacter(num);
			} catch (NoSuchPlayerInWrapperListException e) {
				e.printStackTrace();
			}
		} else if (command.getName().equalsIgnoreCase("rpgchangeinventory")) {
			try {
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				if (pw.isBuildInventoryActive()) {
					pw.setBuildInventoryActive(false);
				} else {
					pw.setBuildInventoryActive(true);
				}
			} catch (NoSuchPlayerInWrapperListException e) {
				e.printStackTrace();
			}
		} else if (command.getName().equalsIgnoreCase("rpgcharacterselect")) {
			try {
				PlayerWrapper pw = Main.getPlayerWrapperFromUUID(p.getUniqueId());
				pw.getCharacterSelect().open(p);
			} catch (NoSuchPlayerInWrapperListException e) {
			}
		}
		return false;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		// for (PlayerWrapper testPlayer : PLAYER_WRAPPER_LIST) {
		// if (testPlayer.getUniqueId().equals(event.getPlayer().getUniqueId()))
		// {
		// if (event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
		// testPlayer.castSpell) {
		// testPlayer.sendMessage("You clicked kek. Nomma geht erst nach
		// casten hoffe ich...");
		// testPlayer.castSpell = false;
		// }
		// }
		// }
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

	/**
	 * test for a custom menu inventory
	 * 
	 * @param event
	 */
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

	/**
	 * opening a custom book
	 * 
	 * @param p
	 */
	public void openCustomBook(Player p) {
		BookUtil.openBook(createBook("HI", "MEGAHIGH"), p);
	}

	/**
	 * spawning a custom entity
	 * 
	 * @param loc
	 *            the location to spawn it at
	 */
	public void spawnCustomEntity(Location loc) {
		World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();

		CustomVillager vill = new CustomVillager(nmsWorld, 5);

		vill.setPosition(loc.getX(), loc.getY(), loc.getZ());
		vill.setHealthPoints(100);
		nmsWorld.addEntity(vill);
	}

	/**
	 * custom damage calculation test
	 * 
	 * @param event
	 */
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

	public static ItemStack createBook(String title, String author) {
		ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
		bookMeta.setTitle(title);
		bookMeta.setAuthor(author);
		bookMeta.setPages("!TETSTSTSTETSTST");
		writtenBook.setItemMeta(bookMeta);
		// List<String> pages = new ArrayList<String>();
		bookMeta.addPage("Hello, welcome to TimeVisualSale's server!"); // Page
																		// 1
		bookMeta.addPage("Website: timevisualsales.com"); // Page 2
		bookMeta.addPage("Hope you enjoy your stay/play!"); // Page 3
		return writtenBook;
	}

	/**
	 * getting a playerwrapper from the players uuid
	 * 
	 * @param uuid
	 * @return
	 * @throws NoSuchPlayerInWrapperListException
	 */
	public static PlayerWrapper getPlayerWrapperFromUUID(UUID uuid) throws NoSuchPlayerInWrapperListException {
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

	private void writePlayerDataIntoDatabase(Player p, PlayerWrapper pw) {
		PlayerWrapperEntity pwe = Main.getDbHandler().getPlayerEntityByPlayer(p);
		if (pwe == null) {
			pwe = new PlayerWrapperEntity();
		}
		pwe.ip = p.getAddress().getHostName();
		pwe.uuid = p.getUniqueId().toString();
		pwe.characters = createCharacterSetEntities(pw);
		pwe.buildInventory = new FakeInventoryEntity(pw.getBuildInventory());
		pwe.disableFakeInventory = pw.isDisableFakeInventory();
		pwe.buildInventoryActive = pw.isBuildInventoryActive();
		pwe.activeCharacter = pw.getActiveCharacter();
		Main.getDbHandler().savePlayerEntity(pwe);
	}

	private Map<Integer, CharacterEntity> createCharacterSetEntities(PlayerWrapper pw) {
		Map<Integer, CharacterEntity> characters = new HashMap<Integer, CharacterEntity>();
		for (int i = 0; i < pw.getCharacters().size(); i++) {
			characters.put(i,
					new CharacterEntity(new MoneyHandlerEntity(pw.getCharacters().get(i).getMoneyHandler()),
							new LocationEntity(pw.getCharacters().get(i).getRespawnLocation()),
							new FakeInventoryEntity(pw.getCharacters().get(i).getCharacterInventory()),
							pw.getCharacters().get(i).getExp(), pw.getCharacters().get(i).getLevel(),
							pw.getCharacters().get(i).getDamage(), pw.getCharacters().get(i).getArmor()));
		}
		return characters;
	}

	private void addPlayersToWrapperList() {
		Collection<? extends Player> playerCollection = Bukkit.getOnlinePlayers();
		for (Player p : playerCollection) {
			addPlayerFromPlayerEntity(p);
		}
	}

	private void addPlayerFromPlayerEntity(Player p) {
		PlayerWrapperEntity pe = dbHandler.getPlayerEntityByPlayer(p);
		PlayerWrapper pw;
		if (pe == null) {
			pw = new PlayerWrapper(p, new HashMap<Integer, Character>(), new FakeInventory(p.getInventory().getSize()),
					true, false, 0);
			PLAYER_WRAPPER_LIST.add(pw);
		} else {
			FakeInventory buildInventory = new FakeInventory(pe.buildInventory.toFakeInventory());
			pw = new PlayerWrapper(p, createCharacterMapFromCharacterEntity(pe), buildInventory,
					pe.buildInventoryActive, pe.disableFakeInventory, pe.activeCharacter);
			PLAYER_WRAPPER_LIST.add(pw);
		}

		pw.setCharacterSelect(new InventoryMenu(this, new InventoryMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(InventoryMenu.OptionClickEvent event) {
				switch (event.getName()) {
				case "Create Character":
					pw.getCharacters().put(pw.getCharacters().size(),
							new Character(new MoneyHandler(), ConfigHandler.spawnLocation,
									new FakeInventory(pw.getPlayer().getInventory().getSize()), null));
					//TODO muss neu connecten, damit es angezeigt wird
					break;
				default:
					pw.changeCharacter(event.getPosition() - 2);
					break;
				}
				event.setWillClose(true);
			}
		}, 27, "Character Selection"));

		for (int i = 0; i < 5; i++) {
			if (pw.getCharacters().get(i) != null) {
				pw.getCharacterSelect().setOption(2 + i, new ItemStack(Material.IRON_SWORD), "Character Slot " + i,
						"Open this Character");
			} else {
				pw.getCharacterSelect().setOption(2 + i, new ItemStack(Material.PAPER), "Create Character",
						"Create a new Character");
			}
		}

	}

	private Map<Integer, Character> createCharacterMapFromCharacterEntity(PlayerWrapperEntity pe) {
		Map<Integer, Character> chars = new HashMap<Integer, Character>();
		if (pe.characters != null) {
			for (int i = 0; i < pe.characters.size(); i++) {
				chars.put(i, pe.characters.get(i).toCharacter());
			}
		}
		return chars;
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
					if (counter == 20) {
						stand.remove();
					}
					counter++;
					stand.setVelocity(new Vector(0, 0.07, 0));
				}

			}, 0, 1L);
		}
	}

}