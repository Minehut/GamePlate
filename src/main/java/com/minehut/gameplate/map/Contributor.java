package com.minehut.gameplate.map;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by luke on 12/19/16.
 */
public class Contributor {
    private String name = "";
    private UUID uuid;
    private String contribution;

    public Contributor(UUID uuid, String contribution) {
        this.uuid = uuid; //nullable
        this.contribution = contribution;
    }

    public boolean isContributor(Player player) {
        if (uuid != null) {
            return player.getUniqueId().equals(this.uuid);
        } else {
            return player.getName().equals(this.name);
        }
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getContribution() {
        return contribution;
    }

    public void setName(String name) {
        this.name = name;
    }
}
