package com.minehut.gameplate.module.modules.connection;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
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
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

/**
 * Created by luke on 12/22/16.
 */
public class ConnectionModule extends Module {
    private static Map<UUID, PermissionAttachment> attachmentMap = new HashMap<>();
    List<String> worldEditPermissions = new ArrayList<>();

    public ConnectionModule() {
        worldEditPermissions.add("worldedit.navigation.jumpto.command");
        worldEditPermissions.add("worldedit.navigation.thru.command");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        TeamModule observers = TeamManager.getObservers();
        for (Player player : Bukkit.getOnlinePlayers()) {
            observers.addPlayer(player, true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        attachmentMap.put(event.getPlayer().getUniqueId(), event.getPlayer().addAttachment(GamePlate.getInstance()));

        event.getPlayer().resetTitle();

        for (String s : worldEditPermissions) {
            attachmentMap.get(event.getPlayer().getUniqueId()).setPermission(s, true);
        }

        GameHandler.getGameHandler().getMatch().getModules().getModule(TeamManager.class).attemptJoinTeam(event.getPlayer(), TeamManager.getObservers());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
        if (teamModule != null) {
            teamModule.removePlayer(event.getPlayer());
        }

        if (attachmentMap.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer().getUniqueId()));
            attachmentMap.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent event) {
        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
        if (teamModule != null) {
            teamModule.removePlayer(event.getPlayer());
        }

        if (attachmentMap.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().removeAttachment(attachmentMap.get(event.getPlayer().getUniqueId()));
            attachmentMap.remove(event.getPlayer().getUniqueId());
        }
    }
}
