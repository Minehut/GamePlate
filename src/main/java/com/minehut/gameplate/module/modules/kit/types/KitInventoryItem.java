package com.minehut.gameplate.module.modules.kit.types;

import com.minehut.gameplate.module.modules.kit.KitItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitInventoryItem extends KitItem {

    private int slot;
    private ItemStack item;

    public KitInventoryItem(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void apply(Player player) {

        if (slot == 103) {
            player.getInventory().setHelmet(item);
        } else if (slot == 102) {
            player.getInventory().setChestplate(item);
        } else if (slot == 101) {
            player.getInventory().setLeggings(item);
        } else if (slot == 100) {
            player.getInventory().setBoots(item);
        } else if (slot == -2) {
            player.getInventory().setItemInOffHand(item);
        } else {
            player.getInventory().setItem(slot, item);
        }
    }
}
