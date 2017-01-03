package com.minehut.gameplate.module.modules.matchAlerts;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.ColorUtil;
import com.minehut.gameplate.util.Fireworks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Created by luke on 12/28/16.
 */
public class MatchAlertsModule extends Module {

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            for (Player player : teamModule.getPlayers()) {
                player.sendMessage(ChatUtil.HEADER + ChatUtil.DIVIDER);
                player.sendMessage(ChatUtil.HEADER + "= " + ChatUtil.HIGHLIGHT + "The match has started!");
                player.sendMessage(ChatUtil.HEADER + "=");
                if (!teamModule.isObserver()) {
                    player.sendMessage(ChatUtil.HEADER + "= " + ChatUtil.TEXT + "You are on the " + teamModule.getColor() + teamModule.getName());
                } else {
                    player.sendMessage(ChatUtil.HEADER + "= " + ChatUtil.TEXT + "Join the game with " + ChatUtil.HIGHLIGHT + "/join");
                }
                player.sendMessage(ChatUtil.HEADER + ChatUtil.DIVIDER);
            }
        }

        if (GameHandler.getGameHandler().getCurrentMap().getMap().getObjective() != null) {
            final UUID matchUuid = GameHandler.getGameHandler().getMatch().getUuid();
            Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), () -> {
                if (GameHandler.getGameHandler().getMatch().getUuid().equals(matchUuid)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatUtil.HEADER + "[" + new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PREFIX).getMessage(player.spigot().getLocale()) + "] "
                                + ChatUtil.TEXT + GameHandler.getGameHandler().getCurrentMap().getMap().getObjective());
                    }
                }
            }, 2 * 20);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMatchEnd(MatchEndEvent event) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            for (Player player : teamModule.getPlayers()) {

                player.sendMessage(ChatColor.GREEN + ChatUtil.DIVIDER);
                player.sendMessage(ChatColor.GREEN + "=");
                if (event.getTeam() != null) {
                    player.sendMessage(ChatColor.GREEN + "= " + event.getTeam().getColor() + event.getTeam().getName() + ChatUtil.TEXT + " won the match!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "= " + ChatUtil.TEXT + "The match resulted in a tie.");
                }

                player.sendMessage(ChatColor.GREEN + "=");
                player.sendMessage(ChatColor.GREEN + ChatUtil.DIVIDER);

                ChatColor color = ChatUtil.HIGHLIGHT;
                if (event.getTeam() != null) {
                    color = event.getTeam().getColor();
                }

                Fireworks.spawnFirework(player.getLocation(), FireworkEffect.builder().withColor(ColorUtil.convertChatColorToColor(color)).with(FireworkEffect.Type.BALL).build(), 1);

                player.setAllowFlight(true);
                player.setFlying(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        if (GameHandler.getGameHandler().getMatch().hasEnded()) {
            event.getPlayer().setAllowFlight(true);
            event.getPlayer().setFlying(true);
        }
    }
}
