package com.minehut.gameplate.module.modules.itemDrop;

import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.TaskedModule;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Lucas on 1/4/2017.
 */
public abstract class ItemDropModule extends TaskedModule {

    private Location location;
    private ItemStack item;

    public ItemDropModule(Location location, ItemStack item) {
        this.location = location;
        this.item = item;
        respawn();
    }

    public Item respawn() {
        Item itemSpawn = location.getWorld().dropItemNaturally(location, item);
        itemSpawn.setPickupDelay(20 * 3);
        return itemSpawn;
    }

}
