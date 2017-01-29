package de.rpgstupe.rpgplugin.database.entities;


import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value = "players", noClassnameStored = true)
public class PlayerWrapperEntity {
	
	@Id
	private ObjectId id;
	
	@Indexed(options = @IndexOptions(unique = true))
	public String uuid;
	
	public String ip;
	
	@Embedded
	public Set<CharacterEntity> characters;
	
	@Embedded
	public FakeInventoryEntity buildInventory;
}