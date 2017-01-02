package com.minehut.gameplate.module.modules.mobSpawn;

import com.minehut.gameplate.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Created by Lucas on 1/2/2017.
 */
public class MobSpawnModule extends Module {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

}
