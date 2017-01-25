package de.rpgstupe.rpgplugin.database;

import java.util.List;

import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import de.rpgstupe.rpgplugin.database.daos.PlayerDAO;
import de.rpgstupe.rpgplugin.database.entities.PlayerEntity;

public class DatabaseHandler {
	private MongoClient mc;
	private Morphia morphia;
	private Datastore datastore;
	private PlayerDAO playerDAO;

	public DatabaseHandler() {
		mc = new MongoClient();

		morphia = new Morphia();

		morphia.map(PlayerEntity.class);

		datastore = morphia.createDatastore(mc, "dbPlayers");
		datastore.ensureIndexes();

		playerDAO = new PlayerDAO(PlayerEntity.class, datastore);
	}

	// public PlayerWrapper getPlayerWrapperByPlayer(Player player) {
	// PlayerWrapper pw = playerDAO.findOne("playerWrapper",
	// player.getUniqueId().toString());
	// if (du == null) {
	// du = new DUser();
	// du.setUUID(player.getUniqueId().toString());
	// du.setIp(PlayerUtils.inetAddressAsInteger(player.getAddress().getAddress()));
	// du.setUsername(player.getName());
	// userDAO.save(du);
	// }
	// return du;
	// }

	public void savePlayerEntity(PlayerEntity pe) {
		playerDAO.save(pe);
	}

	public PlayerEntity getPlayerEntityByPlayer(Player p) {
		System.out.println("PLayer db size: "  + getAllPlayers().size());
		return playerDAO.findOne("uuid", p.getUniqueId().toString());
	}
	
	public List<PlayerEntity> getAllPlayers()
    {
        return playerDAO.find().asList();
    }

}
