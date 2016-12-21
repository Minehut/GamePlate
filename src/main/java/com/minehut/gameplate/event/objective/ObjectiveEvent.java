package com.minehut.gameplate.event.objective;

import com.minehut.gameplate.module.GameObjectiveModule;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ObjectiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected GameObjectiveModule objective;
    protected Player player;

    public ObjectiveEvent(GameObjectiveModule objective, Player player) {
        this.objective = objective;
        this.player = player;
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

    public GameObjectiveModule getObjective() {
        return objective;
    }
}
