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
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

/**
 * Created by luke on 12/28/16.
 */
public class MatchAlertsModule extends Module {

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            for (Player player : teamModule.getPlayers()) {
                player.sendMessage(ChatColor.DARK_PURPLE + ChatUtil.divider);
                player.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.AQUA + "The match has started!");
                player.sendMessage(ChatColor.DARK_PURPLE + "#");
                if (!teamModule.isObserver()) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "You are on the " + teamModule.getColor() + teamModule.getName());
                } else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "Join the game with " + ChatColor.AQUA + "/join");
                }
                player.sendMessage(ChatColor.DARK_PURPLE + ChatUtil.divider);
            }
        }

        if (GameHandler.getGameHandler().getCurrentMap().getMap().getObjective() != null) {
            final UUID matchUuid = GameHandler.getGameHandler().getMatch().getUuid();
            Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (GameHandler.getGameHandler().getMatch().getUuid().equals(matchUuid)) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "[" + new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PREFIX).getMessage(player.spigot().getLocale()) + "] "
                                    + ChatColor.DARK_AQUA + GameHandler.getGameHandler().getCurrentMap().getMap().getObjective());
                        }
                    }
                }
            }, 2 * 20);
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            for (Player player : teamModule.getPlayers()) {
                player.sendMessage(ChatColor.GREEN + ChatUtil.divider);
                player.sendMessage(ChatColor.GREEN + "#");
                if (event.getTeam() != null) {
                    player.sendMessage(ChatColor.GREEN + "# " + event.getTeam().getColor() + event.getTeam().getName() + ChatColor.DARK_AQUA + " won the match!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "# " + ChatColor.DARK_AQUA + "The match resulted in a tie.");
                }

                player.sendMessage(ChatColor.GREEN + "#");
                player.sendMessage(ChatColor.GREEN + ChatUtil.divider);

                Fireworks.spawnFirework(player.getLocation(), FireworkEffect.builder().withColor(ColorUtil.convertChatColorToColor(event.getTeam().getColor())).with(FireworkEffect.Type.BALL).build(), 1);
            }
        }
    }
}
