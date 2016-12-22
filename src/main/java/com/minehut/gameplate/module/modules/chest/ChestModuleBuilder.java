package com.minehut.gameplate.module.modules.chest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.util.Items;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChestModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

    	ArrayList<GameChest> gameChests = new ArrayList<GameChest>();
    	
        if (!match.getJson().has("chests"))
        	return null;
        
        JsonArray array = match.getJson().get("chests").getAsJsonArray();
        ArrayList<JsonObject> chests = new ArrayList<>();
        array.forEach(o -> chests.add(array.getAsJsonObject()));
        
       	chests: for(JsonObject o : chests){
        	if(!o.has("items"))
        		continue chests;
        	int chance = 100;
        	if(o.has("chance"))
        		chance = o.get("chance").getAsInt();
        	JsonArray itemArray = o.get("items").getAsJsonArray();
            ArrayList<JsonObject> itemObject = new ArrayList<>();
            itemArray.forEach(i -> itemObject.add(i.getAsJsonObject()));
            
            ArrayList<ItemStack> items = new ArrayList<>();
            for(JsonObject i : itemObject){
            	if(!i.has("material"))
            		continue;
            	byte data = 0;
            	int amount = 0;
            	String name = "";
            	if(i.has("amount"))
            		amount = i.get("amount").getAsInt();
            	if(i.has("data"))
            		data = i.get("data").getAsByte();
            	if(i.has("name"))
            		name = i.get("name").getAsString();
            	if("".equals(name))
            		items.add(new ItemStack(this.getMaterial(i.get("material").getAsString()), amount, data));
            	else
            		items.add(Items.createItem(this.getMaterial(i.get("material").getAsString()), amount, data, ChatColor.translateAlternateColorCodes('&', name)));
            }
            
            gameChests.add(new GameChest(items, chance));
        }

        return new ModuleCollection<>(new ChestModule(gameChests));
    }
    
    private Material getMaterial(String s){
    	s = s.replaceAll(" ", "_");
    	s = s.toUpperCase();
    	try{
    		return Material.valueOf(s);
    	}catch(Exception ex){
    		System.out.println("Material: " + s + " not found!");
    	}
    	return Material.AIR;
    }

}
