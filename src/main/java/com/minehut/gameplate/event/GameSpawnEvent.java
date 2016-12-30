package com.minehut.gameplate.event;

import com.minehut.gameplate.module.modules.kit.KitModule;
import com.minehut.gameplate.module.modules.spawn.SpawnNode;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private TeamModule team;
    private SpawnNode spawn;
    private boolean cancelled;

    public GameSpawnEvent(final Player player, TeamModule team, SpawnNode spawn) {
        this.player = player;
        this.team = team;
        this.spawn = spawn;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public TeamModule getTeam() {
        return team;
    }

    public void setTeam(TeamModule team) {
        this.team = team;
    }

    public SpawnNode getSpawn() {
        return spawn;
    }

    public void setSpawn(SpawnNode spawn) {
        this.spawn = spawn;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
