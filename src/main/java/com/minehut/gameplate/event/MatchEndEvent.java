package com.minehut.gameplate.event;

import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final TeamModule team;

    public MatchEndEvent(TeamModule team) {
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public TeamModule getTeam() {
        return team;
    }
}
