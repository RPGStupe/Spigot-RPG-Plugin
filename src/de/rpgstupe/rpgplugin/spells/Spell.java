package de.rpgstupe.rpgplugin.spells;

import java.util.List;

import de.rpgstupe.rpgplugin.EnumClass;

public abstract class Spell {
	protected List<EnumClass> classes;
	protected int cooldown;
	protected int cost;
	protected int level;
	
	public abstract void cast();
}
