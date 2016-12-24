package com.minehut.gameplate.event;

import com.minehut.gameplate.module.modules.portal.PortalModule;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by lucascosolo on 12/24/16.
 */
public class PortalTeleportEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final PortalModule portal;

    private final RegionModule region;
    private final Location to;

    private boolean cancelled;

    public PortalTeleportEvent(Player player, PortalModule portal) {
        this.player = player;
        this.portal = portal;
        this.region = portal.getRegion();
        this.to = new Location(player.getWorld(), portal.getX(), portal.getY(), portal.getZ());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public PortalModule getPortal() { return portal; }

    public RegionModule getRegion() { return region; }

    public Location getTo() { return to; }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
