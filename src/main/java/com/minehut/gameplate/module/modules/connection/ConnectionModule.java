package com.minehut.gameplate.module.modules.connection;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by luke on 12/22/16.
 */
public class ConnectionModule extends Module {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        TeamModule observers = TeamManager.getObservers();
        for (Player player : Bukkit.getOnlinePlayers()) {
            observers.addPlayer(player, true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        GameHandler.getGameHandler().getMatch().getModules().getModule(TeamManager.class).attemptJoinTeam(event.getPlayer(), TeamManager.getObservers());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
        if (teamModule != null) {
            teamModule.removePlayer(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent event) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
        if (teamModule != null) {
            teamModule.removePlayer(event.getPlayer());
        }
    }
}
