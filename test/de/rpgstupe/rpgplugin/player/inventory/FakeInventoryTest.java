package de.rpgstupe.rpgplugin.player.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.rpgstupe.rpgplugin.configuration.ConfigHandler;

public class FakeInventoryTest {

	@DataProvider
	public Object[][] dp() {
		return new Object[][] {
				{ Material.WOOD, Material.ACACIA_DOOR, Material.ACACIA_FENCE, Material.ACACIA_FENCE_GATE, Material.ACACIA_STAIRS, Material.WOOD } };
	}

	@Test(dataProvider = "dp")
	public void isCustomItemStackFitInInventoryShouldReturnFalseForInvalidCases(Material material1,
			Material material2, Material material3, Material material4, Material material5,
			Material materialFit) {

		ConfigHandler.moneySmallSlot = 1;
		ConfigHandler.moneyMediumSlot = 2;
		ConfigHandler.moneyLargeSlot = 3;
		ConfigHandler.moneySlotsUsed = true;


		FakeInventory inventory = new FakeInventory(5);
		
		ItemStack stack = Mockito.spy(new ItemStack(material1));
		Mockito.doReturn(false).when(stack).hasItemMeta();
		
		inventory.getInventoryArray()[0] = new CustomItemStack(stack);
		inventory.getInventoryArray()[0].setMaxStackSize(64);
		stack.setType(material2);
		inventory.getInventoryArray()[1] = new CustomItemStack(stack);
		inventory.getInventoryArray()[1].setMaxStackSize(64);
		stack.setType(material3);
		inventory.getInventoryArray()[2] = new CustomItemStack(stack);
		inventory.getInventoryArray()[2].setMaxStackSize(64);
		stack.setType(material4);
		inventory.getInventoryArray()[3] = new CustomItemStack(stack);
		inventory.getInventoryArray()[3].setMaxStackSize(64);
		stack.setType(material5);
		inventory.getInventoryArray()[4] = new CustomItemStack(stack);
		inventory.getInventoryArray()[4].setMaxStackSize(64);
		
		for (CustomItemStack c : inventory.getInventoryArray()) {
			//System.out.println(c.getItemStack().getType());
		}
		
		stack.setType(materialFit);
		Assert.assertFalse(inventory.isCustomItemStackFitInInventory(new CustomItemStack(stack)));
	}
}
