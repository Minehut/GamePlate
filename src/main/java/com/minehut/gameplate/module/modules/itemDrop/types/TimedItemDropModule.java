package com.minehut.gameplate.module.modules.itemDrop.types;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.module.modules.itemDrop.ItemDropModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucas on 1/4/2017.
 */
public class TimedItemDropModule extends ItemDropModule {

    private long lastUpdate = 0;

    private int delay;

    /**
     * @param item The ItemStack to drop
     * @param delay Delay in seconds between drops
     */
    public TimedItemDropModule(Location location, ItemStack item, int delay) {
        super(location, item);
        this.delay = delay;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - lastUpdate > delay * 1000) {
            lastUpdate = System.currentTimeMillis();
            respawn();
        }
    }

}
