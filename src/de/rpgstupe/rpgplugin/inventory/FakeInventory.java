package de.rpgstupe.rpgplugin.inventory;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;
import net.minecraft.server.v1_11_R1.Material;

@SuppressWarnings("serial")
public class FakeInventory extends ArrayList<ItemStack> {
	private int inventorySize;

	public FakeInventory(int inventorySize) {
		this.inventorySize = inventorySize;
		createEmptyInventory();
	}

	private void createEmptyInventory() {
		for (int i = 0; i < inventorySize; i++) {
			this.add(null);
		}
	}

	/**
	 * adds an itemstack to the fakeinventory (merges into existing stacks)
	 * 
	 * @param stack
	 *            the stack to add
	 * @return
	 * @throws ItemDoesNotFitException
	 *             thrown if the itemstack does not fit
	 */
	public boolean addItemStack(ItemStack stack) throws ItemDoesNotFitException {
		if (isItemStackFitInInventory(stack)) {
			mergeStackWithInventory(stack);
			return true;
		} else {
			throw new ItemDoesNotFitException();
		}
	}

	/**
	 * checks if the fakeinventory has space for the stack
	 * 
	 * @param stack
	 *            the stack that should fit
	 * @return
	 */
	public boolean isItemStackFitInInventory(ItemStack stack) {
		int tempStackSize = stack.getAmount();

		for (ItemStack i : this) {
			if (i == null) {
				tempStackSize -= stack.getMaxStackSize();
			} else if (i.isSimilar(stack)) {
				if (i.getAmount() < i.getMaxStackSize()) {
					tempStackSize -= i.getMaxStackSize() - i.getAmount();
				}
			}
		}
		return tempStackSize <= 0 ? true : false;
	}

	private void mergeStackWithInventory(ItemStack stackToMerge) {
		int stackSizeTemp = stackToMerge.getAmount();

		for (ItemStack stackInFakeInv : this) {
			if (stackToMerge.isSimilar(stackInFakeInv)
					&& stackInFakeInv.getAmount() < stackInFakeInv.getMaxStackSize()) {
				if (stackSizeTemp + stackInFakeInv.getAmount() <= stackInFakeInv.getMaxStackSize()) {
					// passt komplett auf den Stack
					stackInFakeInv.setAmount(stackSizeTemp + stackInFakeInv.getAmount());
					stackSizeTemp = 0;
					break;
				} else {
					stackSizeTemp = (stackInFakeInv.getAmount() + stackSizeTemp) % stackInFakeInv.getMaxStackSize();
					stackInFakeInv.setAmount(stackInFakeInv.getMaxStackSize());
				}
			}
		}
		if (stackSizeTemp > 0) {
			ItemStack tempStack = new ItemStack(stackToMerge);
			tempStack.setAmount(stackSizeTemp);
			this.set(this.indexOf(null), tempStack);
		}
	}

	public void setComplete(ItemStack[] contents) {
		int counter = 0;
		for (ItemStack s : contents) {
			if (s != null) {
				this.set(counter, new ItemStack(s));
			} else {
				this.set(counter, null);
			}
			counter++;
		}
	}
}
