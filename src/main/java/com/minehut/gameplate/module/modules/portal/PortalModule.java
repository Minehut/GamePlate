package com.minehut.gameplate.module.modules.portal;

import com.minehut.gameplate.event.PortalTeleportEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by lucascosolo on 12/24/16.
 */
public class PortalModule extends Module {

    private RegionModule region;
    private double x, y, z;

    public PortalModule(RegionModule region, double x, double y, double z) {
        this.region = region;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (region.contains(event.getTo().toVector())) {
            PortalTeleportEvent teleportEvent = new PortalTeleportEvent(event.getPlayer(), this);
            Bukkit.getServer().getPluginManager().callEvent(teleportEvent);
            if (!teleportEvent.isCancelled()) {
                event.getPlayer().teleport(teleportEvent.getTo());
            }
        }
    }

    public RegionModule getRegion() { return region; }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
