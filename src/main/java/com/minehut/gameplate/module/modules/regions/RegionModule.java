package com.minehut.gameplate.module.modules.regions;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.Module;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/22/16.
 */
public abstract class RegionModule extends Module {
    private String id;

    public RegionModule(String id) {
        this.id = id;
    }

    /*
     * Don't just loop through blocks. Please use math!
     */
    public abstract boolean contains(Vector vector);

    public static RegionModule getRegionById(String id) {
        for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
            if (regionModule.getId().equals(id)) {
                return regionModule;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }


    public abstract Location getRandomLocation();
}
