package com.minehut.gameplate.module.modules.visibility;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.observers.ObserverModule;
import com.minehut.gameplate.module.modules.respawn.RespawnModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

public class Visibility extends Module {

    protected Visibility() {

    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                player.showPlayer(otherPlayer);
            }
        }
    }

    public void showOrHideOthers(Player viewer) {
        for (Player toSee : Bukkit.getOnlinePlayers()) {
            resetVisibility(viewer, toSee, TeamManager.getTeamByPlayer(toSee));
        }
    }

    public void showOrHide(Player toSee) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            resetVisibility(viewer, toSee, TeamManager.getTeamByPlayer(toSee));
        }
    }

    private void resetVisibility(Player viewer, Player toSee, TeamModule newTeam) {
        if (viewer.equals(toSee)) return;
        try {
            boolean showObs = true; //todo: setting system with support for hiding observers
            if (GameHandler.getGameHandler().getMatch().isRunning()) {
                if (RespawnModule.isPlayerDead(toSee)) {
                    setVisibility(viewer, toSee, false);
                } else if (ObserverModule.isObserver(viewer)) {
                    setVisibility(viewer, toSee, !(newTeam != null && newTeam.isObserver() && !showObs));
                } else {
                    setVisibility(viewer, toSee, !(newTeam != null && newTeam.isObserver()));
                }
            } else {
                setVisibility(viewer, toSee, showObs);
            }
        } catch (NullPointerException e) {
            viewer.showPlayer(toSee);
        }
    }

    private void setVisibility(final Player viewer, final Player toSee, boolean shouldSee) {
        if (shouldSee) {
            viewer.showPlayer(toSee);
        } else {
            viewer.hidePlayer(toSee);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(event.getPlayer());
            event.getPlayer().hidePlayer(player);
        }
        showOrHide(event.getPlayer());
        showOrHideOthers(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            showOrHide(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(GameSpawnEvent event) {
        final Player player = event.getPlayer();
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            setVisibility(viewer, player, false);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                showOrHide(player);
            }
        }, 5L);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            showOrHide(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        Player switched = event.getPlayer();
        showOrHide(switched);
        showOrHideOthers(switched);
    }
}
