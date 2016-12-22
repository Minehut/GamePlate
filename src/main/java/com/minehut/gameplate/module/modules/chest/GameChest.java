package com.minehut.gameplate.module.modules.chest;

import java.util.ArrayList;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GameChest {

	private ArrayList<ItemStack> items;
	private int chance;
	
	public GameChest(ArrayList<ItemStack> items, int chance){
		this.items = items;
		this.chance = chance;
	}
	
	public void fillInventory(Inventory inv){
		inv.setContents((ItemStack[]) items.toArray());
	}
	
	public ArrayList<ItemStack> getItems(){
		return this.items;
	}
	
	public int getChance(){
		return this.chance;
	}
	
}
