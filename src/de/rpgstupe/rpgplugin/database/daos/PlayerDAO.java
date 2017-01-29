package de.rpgstupe.rpgplugin.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import de.rpgstupe.rpgplugin.database.entities.PlayerWrapperEntity;

public class PlayerDAO extends BasicDAO<PlayerWrapperEntity, String> {

	public PlayerDAO(Class<PlayerWrapperEntity> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

}
