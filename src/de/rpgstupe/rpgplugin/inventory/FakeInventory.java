package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;

public class FakeInventory {
	public CustomItemStack[] getFakeInventoryArray() {
		return fakeInventoryArray;
	}

	public void setFakeInventoryArray(CustomItemStack[] fakeInventoryArray) {
		this.fakeInventoryArray = fakeInventoryArray;
	}

	private int inventorySize;
	private CustomItemStack[] fakeInventoryArray;

	/**
	 * this is an exact copy of the Player Ingame Inventory. It makes it easier
	 * to handle the stacksizes and slots of certain items (e.g. the money or
	 * skills)
	 * 
	 * @param inventorySize
	 *            the size of the inventory (should be the ingame inventory size
	 *            for players)
	 */
	public FakeInventory(int inventorySize) {
		this.inventorySize = inventorySize;
		fakeInventoryArray = new CustomItemStack[this.inventorySize];
		createEmptyInventory();
	}

	/**
	 * adds an CustomItemStack to the fakeinventory (merges into existing
	 * stacks)
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
		} else {
			throw new ItemDoesNotFitException();
		}
		return true;
	}

	/**
	 * checks if the fakeinventory has space for the stack
	 * 
	 * @param itemStack
	 *            the stack that should fit
	 * @return
	 */
	public boolean isCustomItemStackFitInInventory(CustomItemStack itemStack) {
		// TODO Money Slots rausnehmen
		int tempStackSize = itemStack.getItemStack().getAmount();

		for (CustomItemStack i : fakeInventoryArray) {
			if (i == null) {
				tempStackSize -= itemStack.getItemStack().getMaxStackSize();
			} else if (i.getItemStack().isSimilar(itemStack.getItemStack())) {
				if (i.getItemStack().getAmount() < i.getItemStack().getMaxStackSize()) {
					tempStackSize -= i.getItemStack().getMaxStackSize() - i.getItemStack().getAmount();
				}
			}
		}

		return tempStackSize <= 0 ? true : false;
	}

	public void setComplete(ItemStack[] itemStacks) {
		int counter = 0;
		for (ItemStack s : itemStacks) {
			if (s != null) {
				CustomItemStack cStack = new CustomItemStack();
				cStack.setItemStack(s);
				fakeInventoryArray[counter] = cStack;
			} else {
				fakeInventoryArray[counter] = null;
			}
			counter++;
		}
	}

	private void mergeStackWithInventory(CustomItemStack itemStack) {
		int stackSizeTemp = itemStack.getItemStack().getAmount();

		for (CustomItemStack stackInFakeInv : fakeInventoryArray) {
			if (stackInFakeInv != null && itemStack.getItemStack().isSimilar(stackInFakeInv.getItemStack())
					&& stackInFakeInv.getItemStack().getAmount() < stackInFakeInv.getItemStack().getMaxStackSize()) {
				if (stackSizeTemp + stackInFakeInv.getItemStack().getAmount() <= stackInFakeInv.getItemStack()
						.getMaxStackSize()) {
					stackInFakeInv.getItemStack().setAmount(stackSizeTemp + stackInFakeInv.getItemStack().getAmount());
					stackSizeTemp = 0;
					break;
				} else {
					stackSizeTemp = (stackInFakeInv.getItemStack().getAmount() + stackSizeTemp)
							% stackInFakeInv.getItemStack().getMaxStackSize();
					stackInFakeInv.getItemStack().setAmount(stackInFakeInv.getItemStack().getMaxStackSize());
				}
			}
		}
		if (stackSizeTemp > 0) {
			CustomItemStack tempStack = new CustomItemStack();
			tempStack.setItemStack(itemStack.getItemStack());
			tempStack.getItemStack().setAmount(stackSizeTemp);
			fakeInventoryArray[indexOf(null)] = tempStack;
		}

	}

	public int indexOf(CustomItemStack cStack) {
		for (int i = 0; i < inventorySize; i++) {
			if (cStack == null && fakeInventoryArray[i] == null
					|| cStack != null && cStack.equals(fakeInventoryArray[i])) {
				return i;
			}
		}
		return -1;
	}

	private void createEmptyInventory() {
		for (int i = 0; i < inventorySize; i++) {
			fakeInventoryArray[i] = new CustomItemStack();
			fakeInventoryArray[i].setItemStack(null);
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (CustomItemStack stack : fakeInventoryArray) {
			s += stack != null ? stack.getItemStack() : null + ", ";
		}
		return s;
	}
}
