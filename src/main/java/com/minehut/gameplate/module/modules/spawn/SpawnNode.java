package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.Location;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnNode {
    private RegionModule region;
    private TeamModule teamModule;
    private float yaw, pitch;

    public SpawnNode(RegionModule region, TeamModule teamModule, float yaw, float pitch) {
        this.region = region;
        this.teamModule = teamModule;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public RegionModule getRegion() {
        return region;
    }

    public TeamModule getTeamModule() {
        return teamModule;
    }

    public Location toLocation() {
        Location location = region.getRandomLocation();
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }

    public void setTeamModule(TeamModule teamModule) {
        this.teamModule = teamModule;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
