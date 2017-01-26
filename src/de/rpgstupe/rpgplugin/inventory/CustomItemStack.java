package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.inventory.ItemStack;

public class CustomItemStack {
	
	private ItemStack itemStack;
	
	public CustomItemStack() {
		this.itemStack = null;
	}
	
	public int getMaxStackSize(ItemStack stack) {
		int stackSize;
		if (MoneyStacksHandler.moneySmallItem.equals(stack.getType().name())) {
			stackSize = MoneyStacksHandler.moneySmallExchangeRate;
		} else if (MoneyStacksHandler.moneyMediumItem.equals(stack.getType().name())) {
			stackSize = MoneyStacksHandler.moneyMediumExchangeRate;
		} else if (MoneyStacksHandler.moneyLargeItem.equals(stack.getType().name())) {
			stackSize = MoneyStacksHandler.moneyLargeMaxAmount;
		} else {
			stackSize = stack.getMaxStackSize();
		}
 		return stackSize;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
}
