package de.rpgstupe.rpgplugin.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.rpgstupe.rpgplugin.quests.Quest;
import de.rpgstupe.rpgplugin.quests.QuestGoal;
import de.rpgstupe.rpgplugin.quests.QuestGoalGoto;

public class QuestsConfigHandler {
	private File configFile;
	private FileConfiguration config;

	public QuestsConfigHandler(JavaPlugin plugin) {
		loadFiles(plugin);
		createQuests();
		loadConfig();
	}

	private void createQuests() {
		Set<String> quests = config.getKeys(false);
		for (String questname : quests) {
			ConfigurationSection questsection = config.getConfigurationSection(questname);
			Quest quest = new Quest();

			quest.setId(questsection.getInt("id"));
			quest.setDependencies(getDependencyList(questsection));

			quest.setQuestGoals(createQuestGoalList(questsection));
		}
	}

	private List<QuestGoal> createQuestGoalList(ConfigurationSection questsection) {
		List<QuestGoal> questgoals = new ArrayList<QuestGoal>();

		for (String questgoalstring : questsection.getConfigurationSection("questgoals").getKeys(false)) {
			QuestGoal questgoal = createQuestGoal(
					questsection.getConfigurationSection("questgoals").getConfigurationSection(questgoalstring));
			questgoals.add(questgoal);
		}
		return questgoals;
	}

	private QuestGoal createQuestGoal(ConfigurationSection section) {
		QuestGoal questgoal = null;
		switch (section.getString("type")) {
		case "goto":
			questgoal = createQuestGoalGoto(section);
			break;
		}
		questgoal.setId(section.getInt("id"));
		questgoal.setDependencies(getDependencyList(section));
		questgoal.setQuestGoalPage(null);
		return questgoal;
	}

	private QuestGoal createQuestGoalGoto(ConfigurationSection section) {
		QuestGoalGoto questgoal = new QuestGoalGoto();
		ConfigurationSection tasksection = section.getConfigurationSection("task");
		questgoal.setDestination1(new Location(Bukkit.getWorld(tasksection.getString("world")),
				(float) tasksection.getInt("x1"), (float) tasksection.getInt("y1"), (float) tasksection.getInt("z1")));
		questgoal.setDestination2(new Location(Bukkit.getWorld(tasksection.getString("world")),
				(float) tasksection.getInt("x2"), (float) tasksection.getInt("y2"), (float) tasksection.getInt("z2")));
		return questgoal;
	}

	private List<Integer> getDependencyList(ConfigurationSection section) {
		List<Integer> dependencies = new ArrayList<Integer>();
		if (section.getConfigurationSection("dependencies") != null) {
			for (String dependenciestring : section.getConfigurationSection("dependencies").getKeys(false)) {
				dependencies.add(section.getConfigurationSection("dependencies").getInt(dependenciestring));
			}
		}
		return dependencies;
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	private void loadFiles(JavaPlugin plugin) {
		// get file resource
		configFile = new File(plugin.getDataFolder(), "quests.yml");

		// create file if it does not exist
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("quests.yml", false);
		}

		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getServer().getLogger().info(e.toString());
		}
	}

	private void loadConfig() {
		// QuestsConfigHandler.moneySmallExchangeRate =
		// config.getInt("money.exchangerate.small_to_medium");
	}
}
