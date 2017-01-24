package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItemStack extends ItemStack {

	public CustomItemStack(ItemStack itemStack) {
		super(itemStack);
	}

	@Override
	public int getMaxStackSize() {
		int stackSize;
		if (Material.DIRT.equals(this.getType())) {
			stackSize = 100;
		} else {
			stackSize = super.getMaxStackSize();
		}
 		return stackSize;
	}

}
