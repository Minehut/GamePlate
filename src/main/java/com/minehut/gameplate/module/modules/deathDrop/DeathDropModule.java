package com.minehut.gameplate.module.modules.deathDrop;

import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.TaskedModule;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luke on 12/31/16.
 */
public class DeathDropModule extends TaskedModule {
    private HashMap<Item, Long> bones = new HashMap<>();

    public DeathDropModule() {

    }

    @EventHandler
    public void onGameDeath(GameDeathEvent event) {
        for (ItemStack itemStack : event.getPlayer().getInventory()) {
            if (itemStack != null) {
                event.getPlayer().getLocation().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack);
            }
        }


        for(int i = 0; i < 3; i++) {
            ItemStack bone = new ItemStack(Material.BONE);
            Item item = event.getPlayer().getLocation().getWorld().dropItemNaturally(event.getPlayer().getLocation(), bone);
            bones.put(item, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (bones.containsKey(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        if (bones.containsKey(event.getEntity())) {
            this.bones.remove(event.getEntity());
        }
    }

    @Override
    public void run() {
        List<Item> toRemove = new ArrayList<>();
        for (Item item : bones.keySet()) {
            if (bones.get(item) + 2000 < System.currentTimeMillis()) {
                item.remove();
                toRemove.add(item);
            }
        }
        for (Item item : toRemove) {
            bones.remove(item);
        }
    }
}
