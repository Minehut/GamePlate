package com.minehut.gameplate.module.modules.axeDamage;

import com.minehut.gameplate.module.Module;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by luke on 11/19/16.
 */
public class AxeDamageModule extends Module {

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if ((event.getDamager() instanceof Player)) {
            Player player = (Player)event.getDamager();
            if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                event.setDamage(event.getDamage() - 6.0D + 3.0D);
            } else if (player.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {
                event.setDamage(event.getDamage() - 6.0D + 2.5D);
            } else if (player.getInventory().getItemInMainHand().getType() == Material.STONE_AXE) {
                event.setDamage(event.getDamage() - 6.0D + 2.0D);
            } else if ((player.getInventory().getItemInMainHand().getType() == Material.GOLD_AXE) ||
                    (player.getInventory().getItemInMainHand().getType() == Material.WOOD_AXE)) {
                event.setDamage(event.getDamage() - 4.0D + 1.5D);
            } else if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SPADE) {
                event.setDamage(event.getDamage() - 2.5D + 2.0D);
            } else if (player.getInventory().getItemInMainHand().getType() == Material.STONE_SPADE) {
                event.setDamage(event.getDamage() - 0.75D + 1.25D);
            }
        }
    }
}