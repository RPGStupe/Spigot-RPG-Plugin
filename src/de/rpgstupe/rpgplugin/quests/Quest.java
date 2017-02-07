package de.rpgstupe.rpgplugin.quests;

import java.util.List;

import de.rpgstupe.rpgplugin.player.CustomBook;

public class Quest {

	private List<QuestGoal> questGoals;
	private int id;
	private List<Integer> dependencies;
	private CustomBook questBook;

	public List<QuestGoal> getQuestGoals() {
		return questGoals;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getDependencies() {
		return dependencies;
	}

	public CustomBook getQuestBook() {
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

	public void setQuestBook(CustomBook questBook) {
		this.questBook = questBook;
	}
}
