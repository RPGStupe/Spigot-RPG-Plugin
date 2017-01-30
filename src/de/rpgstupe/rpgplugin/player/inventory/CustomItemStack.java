package de.rpgstupe.rpgplugin.player.inventory;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.util.NBTReflectionUtil;

public class CustomItemStack {

	private ItemStack itemStack;
	private int maxStackSize;
	private boolean moveable;

	public CustomItemStack() {
		itemStack = null;
	}

	public CustomItemStack(CustomItemStack stack) {
		this(stack.getItemStack());
	}

	public CustomItemStack(ItemStack stack, int maxStackSize, boolean moveable) {
		this.itemStack = new ItemStack(stack);
		this.maxStackSize = maxStackSize;
		this.moveable = moveable;
	}

	public CustomItemStack(ItemStack stack) {
		this(stack, stack.getMaxStackSize(), true);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}
	
	public void setString(String key, String value) {
        itemStack = NBTReflectionUtil.setString(itemStack, key, value);
    }

    public String getString(String key) {
        return NBTReflectionUtil.getString(itemStack, key);
    }

    public void setInteger(String key, int value) {
        itemStack = NBTReflectionUtil.setInt(itemStack, key, value);
    }

    public Integer getInteger(String key) {
        return NBTReflectionUtil.getInt(itemStack, key);
    }

    public void setDouble(String key, double value) {
        itemStack = NBTReflectionUtil.setDouble(itemStack, key, value);
    }

    public double getDouble(String key) {
        return NBTReflectionUtil.getDouble(itemStack, key);
    }

    public void setBoolean(String key, boolean value) {
        itemStack = NBTReflectionUtil.setBoolean(itemStack, key, value);
    }

    public boolean getBoolean(String key) {
        return NBTReflectionUtil.getBoolean(itemStack, key);
    }

    public boolean hasKey(String key) {
        return NBTReflectionUtil.hasKey(itemStack, key);
    }
    
    public void removeKey(String key){
        itemStack = NBTReflectionUtil.remove(itemStack, key);
    }
    
    public Set<String> getKeys(){
        return NBTReflectionUtil.getKeys(itemStack);
    }
}
