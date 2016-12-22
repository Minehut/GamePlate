package com.minehut.gameplate.module.modules.regions.types;

import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/22/16.
 */
public class CylinderRegion extends RegionModule {
    private final Vector base;
    private final double radius, height;

    public CylinderRegion(String id, Vector base, double radius, double height) {
        super(id);
        this.base = base;
        this.radius = radius;
        this.height = height;

        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getMin(), getMax());
        for (Block block : bound.getBlocks()) {
            if (contains(new Vector(block.getX(), block.getY(), block.getZ()))) {
                results.add(block);
            }
        }
        super.setBlocks(results);
    }

    @Override
    public boolean contains(Vector vector) {
        return (Math.hypot(Math.abs(vector.getX() - getBaseX()), Math.abs(vector.getZ() - getBaseZ())) <= getRadius()) && Numbers.checkInterval(vector.getY(), getBaseY(), getBaseY() + getHeight());
    }

    public double getBaseX() {
        return base.getX();
    }

    public double getBaseY() {
        return base.getY();
    }

    public double getBaseZ() {
        return base.getZ();
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    public Vector getMin() {
        return new Vector(getBaseX() - radius, getBaseY(), getBaseZ() - radius);
    }

    public Vector getMax() {
        return new Vector(getBaseX() + radius, getBaseY() + height, getBaseZ() + radius);
    }

}
