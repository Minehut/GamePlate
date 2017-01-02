package com.minehut.gameplate.module.modules.objectives.score.types;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.objectives.score.ScoreObjectiveModule;
import com.minehut.gameplate.module.modules.observers.ObserverModule;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.respawn.RespawnModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/28/16.
 */
public class GoalScoreTypeModule extends Module {
    private ScoreObjectiveModule scoreObjectiveModule;
    private int amount;
    private TeamModule teamModule;
    private String message;
    private List<RegionModule> regionModules = new ArrayList<>();

    public GoalScoreTypeModule(ScoreObjectiveModule scoreObjectiveModule, int amount, TeamModule teamModule, String message, List<RegionModule> regionModules) {
        this.scoreObjectiveModule = scoreObjectiveModule;
        this.amount = amount;
        this.teamModule = teamModule;
        this.message = message;
        this.regionModules = regionModules;

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(ObserverModule.isObserver(event.getPlayer())) return;

        if (contains(event.getTo())) {

            TeamModule playerTeam = TeamManager.getTeamByPlayer(event.getPlayer());
            if (teamModule != null) {
                if (teamModule != playerTeam) {
                    return;
                }
            }

            if (message != null) {
                Bukkit.broadcastMessage(message);
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(new LocalizedChatMessage(ChatConstant.GAME_GOAL_SCORE, playerTeam.getColor() + event.getPlayer().getName() + ChatColor.DARK_AQUA, ChatColor.AQUA.toString() + amount + ChatColor.DARK_AQUA, playerTeam.getColor() + playerTeam.getName() + ChatColor.DARK_AQUA).getMessage(player.spigot().getLocale()));
                }
            }
            scoreObjectiveModule.addPoints(playerTeam, amount);

            GameHandler.getGameHandler().getMatch().getModules().getModule(RespawnModule.class).killPlayer(event.getPlayer(), null, null);
        }
    }

    public boolean contains(Location location) {
        for (RegionModule regionModule : this.regionModules) {
            if (regionModule.contains(location.toVector())) {
                return true;
            }
        }
        return false;
    }
}
