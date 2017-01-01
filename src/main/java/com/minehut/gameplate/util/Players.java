package com.minehut.gameplate.util;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

/**
 * Created by luke on 12/20/16.
 */
public class Players {
    public static void resetPlayer(Player player) {
        resetPlayer(player, true);
    }

    public static void resetPlayer(Player player, boolean heal) {
        if (heal) player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
        for (PotionEffect effect : player.getActivePotionEffects()) {
            try {
                player.removePotionEffect(effect.getType());
            } catch (NullPointerException ignored) {
            }
        }
        player.setTotalExperience(0);
        player.setExp(0);
        player.setLevel(0);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);

        player.setFlying(false);
        player.setAllowFlight(false);

        for (Attribute attribute : Attribute.values()) {
            if (player.getAttribute(attribute) == null) continue;
            for (AttributeModifier modifier : player.getAttribute(attribute).getModifiers()) {
                player.getAttribute(attribute).removeModifier(modifier);
            }
        }
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 4.001D, AttributeModifier.Operation.ADD_SCALAR));
    }
}
