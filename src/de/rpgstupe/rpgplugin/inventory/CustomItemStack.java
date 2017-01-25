package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.inventory.ItemStack;

public class CustomItemStack {
	
	private ItemStack itemStack;
	
	public CustomItemStack() {
		this.itemStack = null;
	}
	
	public int getMaxStackSize(ItemStack stack) {
		int stackSize;
		if (MoneyStacksManager.moneySmallItem.equals(stack.getType().name())) {
			stackSize = MoneyStacksManager.moneySmallExchangeRate;
		} else if (MoneyStacksManager.moneyMediumItem.equals(stack.getType().name())) {
			stackSize = MoneyStacksManager.moneyMediumExchangeRate;
		} else if (MoneyStacksManager.moneyLargeItem.equals(stack.getType().name())) {
			stackSize = MoneyStacksManager.moneyLargeMaxAmount;
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
