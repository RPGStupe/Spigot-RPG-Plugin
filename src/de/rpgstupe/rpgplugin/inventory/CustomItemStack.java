package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CustomItemStack extends ItemStack {

	public CustomItemStack(ItemStack itemStack) {
		super(itemStack);
	}

	public CustomItemStack(Item item) {
		super(new CustomItemStack(new ItemStack(item.getItemStack().getType())));
	}

	@Override
	public int getMaxStackSize() {
		int stackSize;
		if (MoneyStacksManager.moneySmallItem.equals(this.getType().name())) {
			stackSize = MoneyStacksManager.moneySmallExchangeRate;
		} else if (MoneyStacksManager.moneyMediumItem.equals(this.getType().name())) {
			stackSize = MoneyStacksManager.moneyMediumExchangeRate;
		} else if (MoneyStacksManager.moneyLargeItem.equals(this.getType().name())) {
			stackSize = MoneyStacksManager.moneyLargeMaxAmount;
		} else {
			stackSize = super.getMaxStackSize();
		}
 		return stackSize;
	}

}
