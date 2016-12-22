package com.minehut.gameplate.event;

import com.minehut.gameplate.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Match match;

    public MatchStartEvent(Match match) {
        this.match = match;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Match getMatch() {
        return match;
    }
}
