package com.minehut.gameplate.module.modules.regions.types;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Created by luke on 12/22/16.
 */
public class CuboidRegion extends RegionModule {
    private final Vector min, max;

    public CuboidRegion(String id, Vector pos1, Vector pos2) {
        super(id);
        this.min = Vector.getMinimum(pos1, pos2);
        this.max = Vector.getMaximum(pos1, pos2);
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.isInAABB(this.min, this.max);
    }

    @Override
    public Location getRandomLocation() {
        double x = Numbers.getRandom(min.getX(), max.getX());
        double y = Numbers.getRandom(min.getY(), max.getY());
        double z = Numbers.getRandom(min.getZ(), max.getZ());
        return new Location(GameHandler.getGameHandler().getCurrentMap().getWorld(), x, y, z);
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
