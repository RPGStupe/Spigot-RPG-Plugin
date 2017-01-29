package de.rpgstupe.rpgplugin.database.entities;

import org.bukkit.Location;
import org.mongodb.morphia.annotations.Id;

public class LocationEntity {
	@Id
	public int id;

	public double x;
	public double y;
	public double z;
	
	public float yaw;
	public float pitch;

	public LocationEntity() {
		
	}
	
	public LocationEntity(Location respawnLocation) {
		this(respawnLocation.getX(), respawnLocation.getY(), respawnLocation.getZ(), respawnLocation.getYaw(), respawnLocation.getPitch());
	}

	public LocationEntity(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
}
