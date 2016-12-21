package com.minehut.gameplate.module.modules.teamManager;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by luke on 12/19/16.
 */
public class TeamManager extends Module {
    public enum TeamType {
        SOLO,
        TRADITIONAL
    }
    public TeamType teamType;

    public TeamManager(TeamType teamType) {
        this.teamType = teamType;
    }

    /*
     * A player is attempting to join a team using /join or the team picker GUI.
     * returns TRUE if join was successful.
     */
    public boolean attemptJoinTeam(Player player, TeamModule teamModule) {
        TeamModule oldTeam = getTeamByPlayer(player);

        //Check if the team is full.
        if (teamModule.getMembers().size() >= teamModule.getMaxPlayers()) {
            if (player.hasPermission("gameplate.joinFull")) {
                if (teamModule.getMembers().size() >= teamModule.getMaxOverflow()) {
                    ChatUtil.sendMessage(player, ChatConstant.ERROR_TEAM_OVERFLOWED, teamModule.getColor() + teamModule.getName() + ChatColor.DARK_PURPLE);
                    return false;
                }
            } else {
                ChatUtil.sendMessage(player, ChatConstant.ERROR_TEAM_FULL, teamModule.getColor() + teamModule.getName() + ChatColor.DARK_PURPLE);
                return false;
            }
        }

        teamModule.addPlayer(player, true);
        if (oldTeam != null) {
            oldTeam.removePlayer(player);
        }

        return true;
    }

    public static TeamModule getTeamByPlayer(Player player) {
        for (TeamModule teamModule : getTeamModules()) {
            if (teamModule.containsPlayer(player)) {
                return teamModule;
            }
        }
        return null;
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
        return null;
    }

    public TeamType getTeamType() {
        return teamType;
    }
}
