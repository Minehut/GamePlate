package com.minehut.gameplate.module.modules.kit.types;

import com.minehut.gameplate.module.modules.kit.KitItem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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
