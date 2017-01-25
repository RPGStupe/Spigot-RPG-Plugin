package de.rpgstupe.rpgplugin.inventory;

import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Embedded;

import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;

@SuppressWarnings("serial")
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
			}
			counter++;
		}
	}

	private void mergeStackWithInventory(CustomItemStack itemStack) {
		int stackSizeTemp = itemStack.getItemStack().getAmount();

		for (CustomItemStack stackInFakeInv : fakeInventoryArray) {
			if (itemStack.getItemStack().isSimilar(stackInFakeInv.getItemStack()) && stackInFakeInv.getItemStack().getAmount() < stackInFakeInv.getItemStack().getMaxStackSize()) {
				if (stackSizeTemp + stackInFakeInv.getItemStack().getAmount() <= stackInFakeInv.getItemStack().getMaxStackSize()) {
					// passt komplett auf den Stack
					stackInFakeInv.getItemStack().setAmount(stackSizeTemp + stackInFakeInv.getItemStack().getAmount());
					stackSizeTemp = 0;
					break;
				} else {
					stackSizeTemp = (stackInFakeInv.getItemStack().getAmount() + stackSizeTemp) % stackInFakeInv.getItemStack().getMaxStackSize();
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
			if (fakeInventoryArray[i].equals(null)) {
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
	
	

	// public boolean isMoneyFitInInventory(CustomItemStack customItemStack,
	// PlayerWrapper pw) {
	// int tempStackSize = customItemStack.getAmount();
	// String itemName = customItemStack.getType().name();
	// if (MoneyStacksManager.moneySmallItem.equals(itemName)) {
	// if (pw.getMoneySmallAmount() + customItemStack.getAmount() <= )
	// } else if (MoneyStacksManager.moneyMediumItem.equals(itemName)) {
	//
	// } else if (MoneyStacksManager.moneyLargeItem.equals(itemName)) {
	//
	// }
	//
	// for (CustomItemStack i : this) {
	// if (i == null) {
	// tempStackSize -= customItemStack.getMaxStackSize();
	// } else if (i.isSimilar(customItemStack)) {
	// if (i.getAmount() < i.getMaxStackSize()) {
	// tempStackSize -= i.getMaxStackSize() - i.getAmount();
	// }
	// }
	// }
	// return tempStackSize <= 0 ? true : false;
	// }
}
