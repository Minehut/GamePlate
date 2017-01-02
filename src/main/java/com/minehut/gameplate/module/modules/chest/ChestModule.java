package com.minehut.gameplate.module.modules.chest;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import com.minehut.gameplate.module.Module;

public class ChestModule extends Module {

	private ArrayList<Chest> chests = new ArrayList<>();
	private ArrayList<GameChest> finds = new ArrayList<>();
	
	private int totalChance = 0;
	
	public ChestModule(ArrayList<GameChest> finds){
		this.finds = finds;
		
		finds.forEach(c -> totalChance+=c.getChance());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getClickedBlock().getType().equals(Material.CHEST)){
			if(event.getClickedBlock().getState() instanceof Chest){
				Chest chest = (Chest) event.getClickedBlock().getState();
				if(this.chests.contains(chest))
					return;
				this.chests.add(chest);
				populate(chest.getInventory());
				chest.update();
			}
		}
	}
	
	private GameChest getRandomInventory(){
		Random random = new Random();
		int r = random.nextInt(totalChance);
		for(GameChest chest : finds){
			r -= chest.getChance();
			if(r <= 0)
				return chest;
		}
		return null;
	}
	
	private void populate(Inventory inventory){
		GameChest randomInv = getRandomInventory();
		if (randomInv == null) return;
		randomInv.fillInventory(inventory);
	}
	
}
