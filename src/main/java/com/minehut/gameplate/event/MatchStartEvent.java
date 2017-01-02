package com.minehut.gameplate.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public MatchStartEvent() {

    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
