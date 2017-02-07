package de.rpgstupe.rpgplugin.quests;

import org.bukkit.Location;

public class QuestGoalGoto extends QuestGoal {
	private Location destination1;
	private Location destination2;

	public Location getDestination1() {
		return destination1;
	}

	public void setDestination1(Location destination1) {
		this.destination1 = destination1;
	}

	public Location getDestination2() {
		return destination2;
	}

	public void setDestination2(Location destination2) {
		this.destination2 = destination2;
	}
}
