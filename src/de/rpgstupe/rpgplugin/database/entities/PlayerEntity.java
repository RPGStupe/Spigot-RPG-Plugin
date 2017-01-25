package de.rpgstupe.rpgplugin.database.entities;


import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "players", noClassnameStored = true)
public class PlayerEntity {
	@Id
	public int id;
	
	@Indexed(options = @IndexOptions(unique = true))
	public String uuid;
	
	public String ip;

	public int moneySmallAmount;
	public int moneyMediumAmount;
	public int moneyLargeAmount;
	
	@Reference
	public CustomItemStackEntity[] fakeInv;
}