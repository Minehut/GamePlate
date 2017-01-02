package com.minehut.gameplate.module.modules.regions.types;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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
    }

    @Override
    public boolean contains(Vector vector) {
        return (Math.hypot(Math.abs(vector.getX() - getBaseX()), Math.abs(vector.getZ() - getBaseZ())) <= getRadius()) && Numbers.checkInterval(vector.getY(), getBaseY(), getBaseY() + getHeight());
    }

    @Override
    public Location getRandomLocation() {
        double a = Numbers.getRandom(0, radius);
        double b = Numbers.getRandom(0, 360);
        double c = Numbers.getRandom(0, height);

        return new Location(GameHandler.getGameHandler().getCurrentMap().getWorld(), getBaseX() + a * Math.cos(b), getBaseY() + c, getBaseZ() + a * Math.sin(b));
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
