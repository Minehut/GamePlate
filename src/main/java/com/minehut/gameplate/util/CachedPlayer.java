package com.minehut.gameplate.util;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by luke on 12/19/16.
 */
public class CachedPlayer {
    private String name;
    private UUID uuid;

    public CachedPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public CachedPlayer(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
