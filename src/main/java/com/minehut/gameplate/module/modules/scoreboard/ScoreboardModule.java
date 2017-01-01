package com.minehut.gameplate.module.modules.scoreboard;

import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.event.TeamCreateEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.SimpleScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scoreboard.Team;

/**
 * Created by luke on 12/31/16.
 */
public class ScoreboardModule extends Module {
    private TeamModule teamModule;
    private SimpleScoreboard simpleScoreboard;

    /*
     * Each team initializes its own ScoreboardModule.
     */

    public ScoreboardModule(TeamModule teamModule) {
        this.teamModule = teamModule;

        this.simpleScoreboard = new SimpleScoreboard(ChatColor.AQUA + "Objectives");

        for (TeamModule other : TeamManager.getTeamModules()) {
            setupTeam(other);
        }

        for (Player player : teamModule.getPlayers()) {
            simpleScoreboard.send(player);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeamCreate(TeamCreateEvent event) {
        this.setupTeam(event.getTeamModule());
    }

    private void setupTeam(TeamModule teamModule) {
        Team team = this.simpleScoreboard.getScoreboard().registerNewTeam(teamModule.getId());
        team.setDisplayName(teamModule.getName());
        team.setPrefix(teamModule.getColor() + "[" + teamModule.getName().substring(0, 1) + "] ");
        team.setCanSeeFriendlyInvisibles(true);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        for (Player player : teamModule.getPlayers()) {
            team.addPlayer(player);
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        Team team = this.simpleScoreboard.getScoreboard().getTeam(event.getNewTeam().getId());

        if (event.getOldTeam() != null) {
            Team oldTeam = this.simpleScoreboard.getScoreboard().getTeam(event.getOldTeam().getId());
            if (oldTeam != null) {
                oldTeam.removePlayer(event.getPlayer());
            }
        }

        if (team != null) {
           team.addPlayer(event.getPlayer());
        }

        if (event.getNewTeam().getId().equals(this.teamModule.getId())) {
            simpleScoreboard.send(event.getPlayer());
        }
    }

    public TeamModule getTeamModule() {
        return teamModule;
    }

    public SimpleScoreboard getSimpleScoreboard() {
        return simpleScoreboard;
    }
}
