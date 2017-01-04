package com.minehut.gameplate.module.modules.itemDrop;

import com.minehut.gameplate.module.TaskedModule;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Created by Lucas on 1/4/2017.
 */
public abstract class ItemSpawnerModule extends TaskedModule {

    private Location location;
    private ItemStack item;

    public ItemSpawnerModule(Location location, ItemStack item) {
        this.location = location;
        this.item = item;
    }

    public Item respawn() {
        Item itemSpawn = location.getWorld().dropItem(location, item);
        itemSpawn.setVelocity(new Vector(0, 0, 0));
        itemSpawn.setPickupDelay(20 * 2);
        return itemSpawn;
    }

}
