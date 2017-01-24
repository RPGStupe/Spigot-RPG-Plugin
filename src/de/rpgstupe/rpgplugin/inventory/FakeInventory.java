package de.rpgstupe.rpgplugin.inventory;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;

@SuppressWarnings("serial")
public class FakeInventory extends ArrayList<CustomItemStack> {
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
	 * adds an CustomItemStack to the fakeinventory (merges into existing stacks)
	 * 
	 * @param itemStack
	 *            the stack to add
	 * @return
	 * @throws ItemDoesNotFitException
	 *             thrown if the CustomItemStack does not fit
	 */
	public boolean addCustomItemStack(CustomItemStack itemStack) throws ItemDoesNotFitException {
		if (isCustomItemStackFitInInventory(itemStack)) {
			mergeStackWithInventory(itemStack);
			return true;
		} else {
			throw new ItemDoesNotFitException();
		}
	}

	/**
	 * checks if the fakeinventory has space for the stack
	 * 
	 * @param itemStack
	 *            the stack that should fit
	 * @return
	 */
	public boolean isCustomItemStackFitInInventory(CustomItemStack itemStack) {
		int tempStackSize = itemStack.getAmount();

		for (CustomItemStack i : this) {
			if (i == null) {
				tempStackSize -= itemStack.getMaxStackSize();
			} else if (i.isSimilar(itemStack)) {
				if (i.getAmount() < i.getMaxStackSize()) {
					tempStackSize -= i.getMaxStackSize() - i.getAmount();
				}
			}
		}
		return tempStackSize <= 0 ? true : false;
	}

	private void mergeStackWithInventory(CustomItemStack itemStack) {
		int stackSizeTemp = itemStack.getAmount();

		for (CustomItemStack stackInFakeInv : this) {
			if (itemStack.isSimilar(stackInFakeInv)
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
			CustomItemStack tempStack = new CustomItemStack(itemStack);
			tempStack.setAmount(stackSizeTemp);
			this.set(this.indexOf(null), tempStack);
		}
	}

	public void setComplete(ItemStack[] itemStacks) {
		int counter = 0;
		for (ItemStack s : itemStacks) {
			if (s != null) {
				this.set(counter, new CustomItemStack(s));
			} else {
				this.set(counter, null);
			}
			counter++;
		}
	}

}
