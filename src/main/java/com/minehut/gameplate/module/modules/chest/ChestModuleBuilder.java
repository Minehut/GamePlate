package com.minehut.gameplate.module.modules.chest;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

public class ChestModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

    	ArrayList<GameChest> gameChests = new ArrayList<>();

        for (Element chests : match.getDocument().getRootElement().getChildren("chests")) {
			for (Element chest : chests.getChildren("chest")) {
				int chance = 100;
				for (Element c : chest.getChildren("chance")) {
					chance = Integer.parseInt(c.toString());
				}
				List<ItemStack> itemList = new ArrayList<>();
				for (Element items : chest.getChildren("items")) {
					for (Element item : items.getChildren("item")) {
						Material material = getMaterial(item.getAttributeValue("material"));
						if (material == null) continue;
						int amount = 1;
						if (item.getAttribute("amount") != null) {
							try {
								amount = item.getAttribute("amount").getIntValue();
							} catch (DataConversionException ex) {
								continue;
							}
						}
						byte data = getData(item.getAttributeValue("material"));
						if (data == -1) {
							if (item.getAttribute("data") != null) {
								try {
									data = (byte)item.getAttribute("data").getIntValue();
								} catch (DataConversionException ex) {
									continue;
								}
							}
							if (data == -1) {
								data = 0;
							}
						}
						ItemStack itemStack = new ItemStack(material, amount, (short)0, data);
						ItemMeta itemMeta = itemStack.getItemMeta();
						if (item.getChild("displayName") != null) {
							itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.getAttributeValue("displayName")));
						}
						itemStack.setItemMeta(itemMeta);
						itemList.add(itemStack);
					}
				}
				gameChests.add(new GameChest(itemList, chance));
			}
			return new ModuleCollection<>(new ChestModule(gameChests));
		}
		return null;
    }
    
    private Material getMaterial(String s){
    	if (s.contains(":")) {
    		s = s.split(":")[0];
		}
    	s = s.replaceAll(" ", "_");
    	s = s.toUpperCase();
    	try{
    		return Material.getMaterial(s);
    	}catch(Exception ex){
    		System.out.println("Material: " + s + " not found!");
    	}
    	return Material.AIR;
    }

    private byte getData(String s) {
    	if (!s.contains(":")) return -1;
    	try {
			return Byte.parseByte(s.split(":")[1]);
		} catch (NumberFormatException ex) {
			return -1;
		}
	}

}
