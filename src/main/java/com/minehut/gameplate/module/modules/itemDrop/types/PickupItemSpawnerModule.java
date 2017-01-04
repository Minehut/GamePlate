package com.minehut.gameplate.module.modules.itemDrop.types;

import com.minehut.gameplate.module.modules.itemDrop.ItemSpawnerModule;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucas on 1/4/2017.
 */
public class PickupItemSpawnerModule extends ItemSpawnerModule {

    private Item item;

    /**
     * @param item The ItemStack to drop
     */
    public PickupItemSpawnerModule(Location location, ItemStack item) {
        super(location, item);
    }

    @Override
    public void run() {
        if (item == null || item.isDead()) {
            item = respawn();
        }
    }

}
