package com.minehut.gameplate.module.modules.kit.types;

import com.minehut.gameplate.module.modules.kit.KitItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

/**
 * Created by luke on 1/1/17.
 */
public class KitPotionItem extends KitItem {
    private PotionEffect potionEffect;
    private int slot;
    private int amount;

    public KitPotionItem(PotionEffect potionEffect, int slot, int amount) {
        this.potionEffect = potionEffect;
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        if (slot == -1) {
            potionEffect.apply(player);
        } else {
            //todo
        }
    }
}
