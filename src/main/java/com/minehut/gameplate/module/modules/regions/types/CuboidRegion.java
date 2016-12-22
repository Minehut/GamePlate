package com.minehut.gameplate.module.modules.regions.types;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/22/16.
 */
public class CuboidRegion extends RegionModule {
    private final Vector min, max;

    public CuboidRegion(String id, Vector pos1, Vector pos2) {
        super(id);
        this.min = Vector.getMinimum(pos1, pos2);
        this.max = Vector.getMaximum(pos1, pos2);

        List<Block> results = new ArrayList<>();
        for (int x = (int) getXMin(); x < getXMax(); x++) {
            for (int z = (int) getZMin(); z < getZMax(); z++) {
                for (int y = (int) getYMin(); y < getYMax(); y++) {
                    results.add((new Location(GameHandler.getGameHandler().getCurrentMap().getWorld(), x, y, z).getBlock()));
                }
            }
        }
        super.setBlocks(results);
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.isInAABB(this.min, this.max);
    }

    public double getXMin() {
        return min.getX();
    }

    public double getYMin() {
        return min.getY();
    }

    public double getZMin() {
        return min.getZ();
    }

    public double getXMax() {
        return max.getX();
    }

    public double getYMax() {
        return max.getY();
    }

    public double getZMax() {
        return max.getZ();
    }

}
