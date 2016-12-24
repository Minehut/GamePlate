package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnModule extends Module {

    private List<SpawnNode> spawns = new ArrayList<>();

    public SpawnModule(List<SpawnNode> spawns) {
        this.spawns = spawns;
    }

    @EventHandler
    public void onGameSpawn(GameSpawnEvent event) {
        event.setSpawn(event.getTeam().getRandomSpawn().toLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameSpawnFinal(GameSpawnEvent event) {
        event.getPlayer().teleport(event.getSpawn());
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
        GameSpawnEvent gameSpawnEvent = new GameSpawnEvent(event.getPlayer(), teamModule, teamModule.getRandomSpawn().toLocation());
        Bukkit.getPluginManager().callEvent(gameSpawnEvent);
    }

    /*
     * Assigns a random spawnpoint to a team (solo team mode)
     */
    public void assignRandomSpawnpoint(TeamModule teamModule) {
        SpawnNode spawnNode = null;
        for (SpawnNode loop : spawns) {
            if (loop.getTeamModule() == null) {
                spawnNode = loop;
            }
        }
        if(spawnNode == null) return;

        spawnNode.setTeamModule(teamModule);
        teamModule.addSpawn(spawnNode);
    }
}
