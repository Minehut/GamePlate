package com.minehut.gameplate.module.modules.portal;

import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

/**
 * Created by lucascosolo on 12/24/16.
 */
public class PortalModule extends Module {

    private List<RegionModule> regions;
    private RegionModule destination;
    private float yaw, pitch;
    private boolean sound;

    public PortalModule(List<RegionModule> regions, RegionModule destination, float yaw, float pitch, boolean sound) {
        this.regions = regions;
        this.destination = destination;
        this.yaw = yaw;
        this.pitch = pitch;
        this.sound = sound;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (contains(event.getTo())) {
            Location location = destination.getRandomLocation();
            location.setYaw(yaw);
            location.setPitch(pitch);

            event.getPlayer().teleport(location);
            if (sound) {
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2F, 1);
            }
        }
    }

    public boolean contains(Location location) {
        for (RegionModule regionModule : this.regions) {
            if (regionModule.contains(location.toVector())) {
                return true;
            }
        }
        return false;
    }
}
