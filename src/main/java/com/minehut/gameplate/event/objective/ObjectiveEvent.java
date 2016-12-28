package com.minehut.gameplate.event.objective;

import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ObjectiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected ObjectiveModule objective;
    protected TeamModule teamModule;

    public ObjectiveEvent(ObjectiveModule objective, TeamModule teamModule) {
        this.objective = objective;
        this.teamModule = teamModule;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public TeamModule getTeamModule() {
        return teamModule;
    }

    public ObjectiveModule getObjective() {
        return objective;
    }
}
