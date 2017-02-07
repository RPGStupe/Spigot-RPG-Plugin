package de.rpgstupe.rpgplugin.player.inventory;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import de.rpgstupe.rpgplugin.configuration.InventoryConfigHandler;
import de.rpgstupe.rpgplugin.exception.ItemDoesNotFitException;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

public class FakeInventory {
	public CustomItemStack[] getInventoryArray() {
		return inventoryArray;
	}

	private CustomItemStack[] inventoryArray;
	private int inventorySize;

	public FakeInventory(int size) {
		inventoryArray = new CustomItemStack[size];
		for (int i = 0; i < inventoryArray.length; i++) {
			inventoryArray[i] = new CustomItemStack();
		}
		this.inventorySize = inventoryArray.length;
	}

	public FakeInventory(FakeInventory fakeInventory) {
		this(fakeInventory.getInventoryArray().length);
		setComplete(fakeInventory.getInventoryArray());
	}

	/**
	 * checks if the fakeinventory has space for the stack
	 * 
	 * @param itemStack
	 *            the stack that should fit
	 * @return
	 */
	public boolean isCustomItemStackFitInInventory(CustomItemStack itemStack) {
		int stackSize = itemStack.getItemStack().getAmount();
		for (int i = 0; i < inventorySize; i++) {
			if (InventoryConfigHandler.moneySlotsUsed && i != InventoryConfigHandler.moneySmallSlot && i != InventoryConfigHandler.moneyMediumSlot
					&& i != InventoryConfigHandler.moneyLargeSlot) {
				if (inventoryArray[i].getItemStack() == null) {
					stackSize -= itemStack.getItemStack().getMaxStackSize();
				} else if (inventoryArray[i].getItemStack() != null
						&& inventoryArray[i].getItemStack().isSimilar(itemStack.getItemStack())) {
					if (inventoryArray[i].getItemStack().getAmount() < inventoryArray[i].getItemStack()
							.getMaxStackSize()) {
						stackSize -= inventoryArray[i].getItemStack().getMaxStackSize()
								- inventoryArray[i].getItemStack().getAmount();
					}
				}
			}
		}
		return stackSize <= 0 ? true : false;
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
		updateMaxStackSizes();
		return true;
	}

	public CustomItemStack getIndex(int index) {
		return inventoryArray[index];
	}

	public void set(int index, CustomItemStack customItemStack) {
		if (customItemStack.getItemStack() == null) {
			inventoryArray[index] = new CustomItemStack();
		} else {
			inventoryArray[index] = new CustomItemStack(customItemStack);
		}
		updateMaxStackSizes();
	}

	public void removeIndex(int index) {
		// TODO
		updateMaxStackSizes();
	}

	public int first(CustomItemStack stack) {
		for (int i = 0; i < inventoryArray.length; i++) {
			if (stack.getItemStack() == null && inventoryArray[i].getItemStack() == null || stack.getItemStack() != null
					&& stack.getItemStack().isSimilar(inventoryArray[i].getItemStack())) {
				return i;
			}
		}
		return -1;
	}

	public void setComplete(CustomItemStack[] stackArray) {
		ItemStack[] stacks = new ItemStack[stackArray.length];
		for (int i = 0; i < stackArray.length; i++) {
			if (stackArray[i].getItemStack() == null) {
				stacks[i] = null;
			} else {
				stacks[i] = new ItemStack(stackArray[i].getItemStack());
			}
		}
		setComplete(stacks);
	}

	public void setComplete(ItemStack[] stacks) {
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] != null) {
				CustomItemStack cStack = new CustomItemStack();
				cStack.setItemStack(new ItemStack(stacks[i]));
				updateNBTTags(cStack, stacks[i]);
				inventoryArray[i] = cStack;
			} else {
				inventoryArray[i].setItemStack(null);
			}
		}
		updateMaxStackSizes();
	}

	public void updateAllNBTTags() {
		for (int i = 0; i < this.inventorySize; i++) {
			if (this.getIndex(i).getItemStack() != null) {
				System.out.println(this.getIndex(i).getMaxStackSize());
				updateNBTTags(this.getIndex(i), this.getIndex(i).getItemStack());
			}
		}
	}

	private void updateNBTTags(CustomItemStack cstack, ItemStack stack) {
		if (cstack.getCustomNBTTags() != null) {
			CraftItemStack craftItemStack = CraftItemStack.asCraftCopy(stack);
			if (craftItemStack.getType() != Material.AIR) {
				net.minecraft.server.v1_11_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(craftItemStack);
				NBTTagCompound compound = nmsItemStack.getTag();
				if (compound == null) {
					nmsItemStack.setTag(compound = new NBTTagCompound());
				}
				Set<String> keys = compound.c();
				for (String key : keys) {
					if (!"display".equals(key) && !"description".equals(key)) {
						if (compound.get(key).getTypeId() == 8) {
							cstack.setString(key.toString(), compound.getString(key));
						} else if (compound.get(key).getTypeId() == 1) {
							cstack.setBoolean(key, compound.getByte(key) != 0);
						}
					}
				}

				// Do whatever with your NBT data

				// System.out.println(compound.toString());
				nmsItemStack.setTag(compound);
			}
		}
	}

	public int getInventorySize() {
		return inventorySize;
	}

	@Override
	public String toString() {
		String s = "";
		for (CustomItemStack stack : inventoryArray) {
			s += stack.getItemStack() + ", ";
		}
		return s;
	}

	private void mergeStackWithInventory(CustomItemStack itemStack) {
		int stackSizeTemp = itemStack.getItemStack().getAmount();

		for (CustomItemStack stackInFakeInv : inventoryArray) {
			if (stackInFakeInv.getItemStack() != null
					&& itemStack.getItemStack().isSimilar(stackInFakeInv.getItemStack())
					&& stackInFakeInv.getItemStack().getAmount() < stackInFakeInv.getMaxStackSize()) {
				if (stackSizeTemp + stackInFakeInv.getItemStack().getAmount() <= stackInFakeInv.getMaxStackSize()) {
					stackInFakeInv.getItemStack().setAmount(stackSizeTemp + stackInFakeInv.getItemStack().getAmount());
					stackSizeTemp = 0;
					break;
				} else {
					stackSizeTemp = (stackInFakeInv.getItemStack().getAmount() + stackSizeTemp)
							% stackInFakeInv.getMaxStackSize();
					stackInFakeInv.getItemStack().setAmount(stackInFakeInv.getItemStack().getMaxStackSize());
				}
			}
		}

		if (stackSizeTemp > 0) {
			CustomItemStack tempStack = new CustomItemStack();
			tempStack.setItemStack(new ItemStack(itemStack.getItemStack()));
			tempStack.getItemStack().setAmount(stackSizeTemp);
			inventoryArray[first(new CustomItemStack())] = tempStack;
		}
	}

	private void updateMaxStackSizes() {
		// TODO NBT Tags
	}
}
