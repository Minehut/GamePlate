package com.minehut.gameplate.event;

import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private TeamModule teamModule;

    public TeamCreateEvent(TeamModule teamModule) {
        this.teamModule = teamModule;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public TeamModule getTeamModule() {
        return teamModule;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
