package de.rpgstupe.rpgplugin.quests;

import java.util.List;

import de.rpgstupe.rpgplugin.player.CustomBookPage;

public abstract class QuestGoal {
	protected int id;
	protected List<Integer> dependencies;
	protected CustomBookPage questGoalPage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Integer> dependencies) {
		this.dependencies = dependencies;
	}

	public CustomBookPage getQuestGoalPage() {
		return questGoalPage;
	}

	public void setQuestGoalPage(CustomBookPage questGoalPage) {
		this.questGoalPage = questGoalPage;
	}
}
