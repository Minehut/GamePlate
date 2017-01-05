package com.minehut.gameplate.module.modules.teamManager;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.match.MatchState;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.spawn.SpawnModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Created by luke on 12/19/16.
 */
public class TeamManager extends Module {
    public enum TeamType {
        SOLO,
        STATIC
    }
    public TeamType teamType;

    public TeamManager(TeamType teamType) {
        this.teamType = teamType;
    }

    public void createAndJoinTeamForPlayer(Player player) {
        TeamModule teamModule = new TeamModule(player.getName(), player.getName(), false, ChatColor.YELLOW, 1, 1, TeamModule.JoinAllowance.NEVER);

        teamModule.enable();
        GameHandler.getGameHandler().getMatch().getModules().add(teamModule);

        GameHandler.getGameHandler().getMatch().getModules().getModule(SpawnModule.class).assignRandomSpawnpoint(teamModule);

        teamModule.addPlayer(player, false);
    }

    /*
     * A player is attempting to join a team using /join or the team picker GUI.
     * returns TRUE if join was successful.
     */
    public boolean attemptJoinTeam(Player player, TeamModule teamModule) {
        TeamModule oldTeam = getTeamByPlayer(player);

        //Check if players are allowed to join before the game starts.
        if (teamModule.getJoinAllowance() == TeamModule.JoinAllowance.PRE_GAME && !GameHandler.getGameHandler().getMatch().isStarting()) {
            player.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_MATCH_ALREADY_STARTED, teamModule.getColor() + teamModule.getName() + ChatColor.RED).getMessage(player.spigot().getLocale())));
            return false;
        }

        //Check if players are allowed to join mid game.
        if (teamModule.getJoinAllowance() == TeamModule.JoinAllowance.MID_GAME) {
            if (GameHandler.getGameHandler().getMatch().isStarting()) {
                player.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_MATCH_NOT_STARTED, teamModule.getColor() + teamModule.getName() + ChatColor.RED).getMessage(player.spigot().getLocale())));
                return false;
            }

            if (GameHandler.getGameHandler().getMatch().isState(MatchState.CYCLING) || GameHandler.getGameHandler().getMatch().isState(MatchState.ENDED)) {
                player.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_MATCH_CYCLING, teamModule.getColor() + teamModule.getName() + ChatColor.RED).getMessage(player.spigot().getLocale())));
                return false;
            }
        }

        //Check if players are allowed to join at all.
        if (teamModule.getJoinAllowance() == TeamModule.JoinAllowance.NEVER) {
            player.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_TEAM_NO_JOIN, teamModule.getColor() + teamModule.getName() + ChatColor.RED).getMessage(player.spigot().getLocale())));
            return false;
        }

        //Check if the team is full.
        if (!teamModule.isObserver() && teamModule.getMembers().size() >= teamModule.getMaxPlayers()) {
            if (player.hasPermission("gameplate.joinFull")) {
                if (teamModule.getMembers().size() >= teamModule.getMaxOverflow()) {
                    player.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_TEAM_OVERFLOWED, teamModule.getColor() + teamModule.getName() + ChatColor.RED).getMessage(player.spigot().getLocale())));
                    return false;
                }
            } else {
                player.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_TEAM_FULL, teamModule.getColor() + teamModule.getName() + ChatColor.RED).getMessage(player.spigot().getLocale())));
                return false;
            }
        }

        teamModule.addPlayer(player, true);
        if (oldTeam != null) {
            oldTeam.removePlayer(player);
        }

        return true;
    }

    public static TeamModule getTeamByName(String name) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            if (teamModule.getName().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                return teamModule;
            }
        }
        return null;
    }

    public static TeamModule getTeamByPlayer(Player player) {
        for (TeamModule teamModule : getTeamModules()) {
            if (teamModule.containsPlayer(player)) {
                return teamModule;
            }
        }
        return null;
    }

    public static TeamModule getTeamById(String id) {
        for (TeamModule teamModule : getTeamModules()) {
            if (teamModule.getId().equals(id)) {
                return teamModule;
            }
        }
        return null;
    }

    public static TeamModule getTeamWithFewestPlayers() {
        TeamModule smallest = null;
        for (TeamModule teamModule : getTeamModules()) {
            if(teamModule.isObserver()) continue;

            if(smallest == null) {
                smallest = teamModule;
            } else {
                if (((double) teamModule.getPlayers().size() / teamModule.getMaxPlayers()) < ((double) smallest.getPlayers().size() / smallest.getMaxPlayers())) {
                    smallest = teamModule;
                }
            }
        }
        return smallest;
    }

    public static ModuleCollection<TeamModule> getTeamModules() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class);
    }

    public static TeamModule getObservers() {
        for (TeamModule teamModule : getTeamModules()) {
            if (teamModule.isObserver()) {
                return teamModule;
            }
        }
        Bukkit.getLogger().log(Level.SEVERE, "Unable to find observers team!");
        return null;
    }

    public TeamType getTeamType() {
        return teamType;
    }
}
