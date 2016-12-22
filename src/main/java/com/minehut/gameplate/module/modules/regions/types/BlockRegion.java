package com.minehut.gameplate.module.modules.regions.types;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * Created by luke on 12/22/16.
 */
public class BlockRegion extends RegionModule {
    private final Vector vector;

    public BlockRegion(String id, Vector vector) {
        super(id);
        this.vector = vector;

        super.setBlocks(Arrays.asList(new Location(GameHandler.getGameHandler().getCurrentMap().getWorld(), vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).getBlock()));
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.getBlockX() == getVector().getBlockX() &&
                vector.getBlockY() == getVector().getBlockY() &&
                vector.getBlockZ() == getVector().getBlockZ();
    }

    public Vector getVector() {
        return vector;
    }
}
