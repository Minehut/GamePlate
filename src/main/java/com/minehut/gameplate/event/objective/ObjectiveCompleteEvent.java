package com.minehut.gameplate.event.objective;

import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ObjectiveCompleteEvent extends ObjectiveEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public ObjectiveCompleteEvent(ObjectiveModule objective, TeamModule teamModule) {
        super(objective, teamModule);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
