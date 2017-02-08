package de.rpgstupe.rpgplugin.quests;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.player.CustomBook;

public class Quest {

	private List<QuestGoal> questGoals;
	private int id;
	private List<Integer> dependencies;
	private ItemStack questBook;

	public List<QuestGoal> getQuestGoals() {
		return questGoals;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getDependencies() {
		return dependencies;
	}

	public ItemStack getQuestBook() {
		return questBook;
	}

	public void setQuestGoals(List<QuestGoal> questGoals) {
		this.questGoals = questGoals;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDependencies(List<Integer> dependencies) {
		this.dependencies = dependencies;
	}

	public void setQuestBook(ItemStack questBook) {
		this.questBook = questBook;
	}
}
