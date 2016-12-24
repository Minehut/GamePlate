package com.minehut.gameplate.module.modules.kit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lucas on 12/21/2016.
 */
public class Kit {

    private String name;
    private List<KitItem> items;

    private List<Kit> parentKits;

    public Kit(String name, List<KitItem> items) {
        this.name = name;
        this.items = items;
    }

    public void apply(Player player) {
        parentKits.forEach(parent -> parent.apply(player));
        items.forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));
    }

    public String getName() {
        return name;
    }

    public void addParentKit(Kit kit) {
        this.parentKits.add(kit);
    }

    public List<Kit> getParentKits() { return parentKits; }

}
