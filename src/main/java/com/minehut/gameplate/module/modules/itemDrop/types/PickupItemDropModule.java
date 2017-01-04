package com.minehut.gameplate.module.modules.itemDrop.types;

import com.minehut.gameplate.module.modules.itemDrop.ItemDropModule;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucas on 1/4/2017.
 */
public class PickupItemDropModule extends ItemDropModule {

    private Item item;

    /**
     * @param item The ItemStack to drop
     */
    public PickupItemDropModule(Location location, ItemStack item) {
        super(location, item);
    }

    @Override
    public void run() {
        if (item == null || item.isDead()) {
            item = respawn();
        }
    }

}
