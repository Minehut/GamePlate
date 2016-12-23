package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnModule extends Module {

    private HashMap<TeamModule, List<SpawnNode>> spawns = new HashMap<>();

    public void addTeamSpawn(TeamModule teamModule, SpawnNode spawnNode) {
        List<SpawnNode> existingSpawns;
        if (spawns.containsKey(teamModule)) {
            existingSpawns = spawns.get(teamModule);
        } else {
            existingSpawns = new ArrayList<>();
        }

        existingSpawns.add(spawnNode);
        spawns.put(teamModule, existingSpawns);

    }

    @EventHandler
    public void onGameSpawn(GameSpawnEvent event) {
        event.setSpawn(getRandomSpawn(event.getTeam()));
    }

    public Location getRandomSpawn(Player player) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(player);
        return getRandomSpawn(teamModule);
    }

    public Location getRandomSpawn(TeamModule teamModule) {
        return this.spawns.get(teamModule).get(0).getRegion().getRandomLocation(); //todo: randomize.
    }
}
