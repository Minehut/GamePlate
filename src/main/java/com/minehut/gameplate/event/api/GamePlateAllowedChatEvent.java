package com.minehut.gameplate.event.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlateAllowedChatEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean allowedChat = true;

    public GamePlateAllowedChatEvent(Player player) {
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

    public boolean isAllowedChat() {
        return allowedChat;
    }

    public void setAllowedChat(boolean allowedChat) {
        this.allowedChat = allowedChat;
    }
}
