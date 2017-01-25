package de.rpgstupe.rpgplugin.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import de.rpgstupe.rpgplugin.database.entities.PlayerEntity;

public class PlayerDAO extends BasicDAO<PlayerEntity, String> {

	public PlayerDAO(Class<PlayerEntity> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

}
