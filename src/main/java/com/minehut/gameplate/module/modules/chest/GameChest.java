package com.minehut.gameplate.module.modules.chest;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GameChest {

	private List<ItemStack> items;
	private int chance;
	
	public GameChest(List<ItemStack> items, int chance){
		this.items = items;
		this.chance = chance;
	}
	
	public void fillInventory(Inventory inv){
		inv.setContents((ItemStack[]) items.toArray());
	}
	
	public List<ItemStack> getItems(){
		return this.items;
	}
	
	public int getChance(){
		return this.chance;
	}
	
}
