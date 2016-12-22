package com.minehut.gameplate.module.modules.kit;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitItem {

    private int slot;
    private ItemStack item;

    public KitItem(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
