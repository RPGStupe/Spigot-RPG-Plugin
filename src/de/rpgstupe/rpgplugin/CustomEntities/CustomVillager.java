package de.rpgstupe.rpgplugin.CustomEntities;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_11_R1.util.UnsafeList;

import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_11_R1.PathfinderGoalInteract;
import net.minecraft.server.v1_11_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_11_R1.PathfinderGoalLookAtTradingPlayer;
import net.minecraft.server.v1_11_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_11_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_11_R1.World;

public class CustomVillager extends EntityVillager {

	private int armor;
	private float healthPoints;

	public CustomVillager(World world, int armor) {
		super(world);

		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);

			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
		} catch (Exception e) {
			// e.printStackTrace();
		}

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalLookAtTradingPlayer(this));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
		this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
		this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityVillager.class, 5.0F, 0.02F));
		this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));
		this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.armor = armor;
	}

	public void applyDamage(float damage) {
		this.healthPoints -= damage;
		if (getHealthPoints() <= 0f) {
			this.setHealth(0);
		}
	}
	
	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	public float getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(float healthPoints) {
		this.healthPoints = healthPoints;
	}
}