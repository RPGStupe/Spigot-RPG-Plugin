package de.rpgstupe.rpgplugin.player.inventory;

import org.bukkit.inventory.ItemStack;

public class CustomItemStack {

	private ItemStack itemStack;
	private int maxStackSize;
	private EnumCustomItemStackType type;
	private boolean moveable;

	public CustomItemStack() {
		itemStack = null;
		this.setType(EnumCustomItemStackType.NULL);
	}

	public CustomItemStack(CustomItemStack stack) {
		this(stack.getItemStack());
	}

	public CustomItemStack(ItemStack stack, int maxStackSize, EnumCustomItemStackType type, boolean moveable) {
		this.itemStack = new ItemStack(stack);
		this.maxStackSize = maxStackSize;
		this.type = type;
		this.moveable = moveable;
	}

	public CustomItemStack(ItemStack stack) {
		this(stack, stack.getMaxStackSize(), EnumCustomItemStackType.NORMAL, true);
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

	public EnumCustomItemStackType getType() {
		return type;
	}

	public void setType(EnumCustomItemStackType type) {
		this.type = type;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}
}
