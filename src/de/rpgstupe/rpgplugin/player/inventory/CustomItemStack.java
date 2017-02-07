package de.rpgstupe.rpgplugin.player.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.util.NBTReflectionUtil;

public class CustomItemStack {

	private ItemStack itemStack;
	private int maxStackSize;
	private Map<String, Object> customNBTTags;

	public CustomItemStack() {
		itemStack = null;
		this.maxStackSize = -1;
		this.customNBTTags = new HashMap<String, Object>();
	}

	public CustomItemStack(CustomItemStack stack) {
		this(stack.getItemStack(), stack.getMaxStackSize(), stack.getCustomNBTTags());
	}

	public CustomItemStack(ItemStack stack, int maxStackSize, Map<String, Object> customNBTTags) {
		this.itemStack = new ItemStack(stack);
		this.maxStackSize = maxStackSize;
		this.customNBTTags = (customNBTTags == null ? new HashMap<String, Object>() : customNBTTags);
	}

	public CustomItemStack(ItemStack stack) {
		this(stack, stack.getMaxStackSize(), new HashMap<String, Object>());
	}

	public CustomItemStack(ItemStack stack, Map<String, Object> customNBTTags) {
		this(stack, stack.getMaxStackSize(), customNBTTags);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		if (itemStack != null) {
			this.maxStackSize = itemStack.getMaxStackSize();
		}
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}

	public void setString(String key, String value) {
		itemStack = NBTReflectionUtil.setString(itemStack, key, value);
		customNBTTags.put(key, value);
	}

	public String getString(String key) {
		return NBTReflectionUtil.getString(itemStack, key);
	}

	public void setInteger(String key, int value) {
		itemStack = NBTReflectionUtil.setInt(itemStack, key, value);
		customNBTTags.put(key, value);
	}

	public Integer getInteger(String key) {
		return NBTReflectionUtil.getInt(itemStack, key);
	}

	public void setDouble(String key, double value) {
		itemStack = NBTReflectionUtil.setDouble(itemStack, key, value);
		customNBTTags.put(key, value);
	}

	public double getDouble(String key) {
		return NBTReflectionUtil.getDouble(itemStack, key);
	}

	public void setBoolean(String key, boolean value) {
		itemStack = NBTReflectionUtil.setBoolean(itemStack, key, value);
		customNBTTags.put(key, value);
	}

	public boolean getBoolean(String key) {
		return NBTReflectionUtil.getBoolean(itemStack, key);
	}

	public boolean hasKey(String key) {
		return NBTReflectionUtil.hasKey(itemStack, key);
	}

	public void removeKey(String key) {
		itemStack = NBTReflectionUtil.remove(itemStack, key);
	}

	public Set<String> getKeys() {
		return NBTReflectionUtil.getKeys(itemStack);
	}

	public Map<String, Object> getCustomNBTTags() {
		return customNBTTags;
	}
}
