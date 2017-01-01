package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

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
    public void onCycleComplete(CycleCompleteEvent event) {
        TeamModule observers = TeamManager.getObservers();
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            for (Player player : teamModule.getPlayers()) {
                GameSpawnEvent spawnEvent = new GameSpawnEvent(player, teamModule, getObserversSpawn());
                Bukkit.getPluginManager().callEvent(spawnEvent);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());

        SpawnNode spawnNode;
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            spawnNode = teamModule.getRandomSpawn();
        } else {
            spawnNode = getObserversSpawn();
        }

        GameSpawnEvent spawnEvent = new GameSpawnEvent(event.getPlayer(), teamModule, spawnNode);
        Bukkit.getPluginManager().callEvent(spawnEvent);
    }

    @EventHandler
    public void onGameSpawn(GameSpawnEvent event) {
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
        event.setSpawn(event.getTeam().getRandomSpawn());

        event.getPlayer().getInventory().clear();

        if (event.getSpawn().getKit() != null) {
            event.getSpawn().getKit().apply(event.getPlayer());
        }

        event.getPlayer().updateInventory();
        Players.resetPlayer(event.getPlayer());
        event.getPlayer().setFlying(false);
        event.getPlayer().setAllowFlight(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameSpawnFinal(GameSpawnEvent event) {
        event.getPlayer().teleport(event.getSpawn().toLocation());
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
            GameSpawnEvent gameSpawnEvent = new GameSpawnEvent(event.getPlayer(), teamModule, teamModule.getRandomSpawn());
            Bukkit.getPluginManager().callEvent(gameSpawnEvent);
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            if (!teamModule.isObserver()) {
                for (Player player : teamModule.getPlayers()) {
                    GameSpawnEvent gameSpawnEvent = new GameSpawnEvent(player, teamModule, teamModule.getRandomSpawn());
                    Bukkit.getPluginManager().callEvent(gameSpawnEvent);
                }
            }
        }
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

    public SpawnNode getObserversSpawn() {
        for (SpawnNode spawnNode : this.spawns) {
            if (spawnNode.getTeamModule().isObserver()) {
                return spawnNode;
            }
        }
        return null;
    }
}
