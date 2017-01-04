package com.minehut.gameplate.module.modules.scoreboard;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.TeamCreateEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.event.EventHandler;

/**
 * Created by luke on 12/31/16.
 */
public class ScoreboardModuleCreator extends Module {
    private String title;

    /*
     * This class was created to allow teams to be created
     * after the match has started. This will be the case for
     * FFA gamemodes, where a new team is created each time a player joins.
     */

    public ScoreboardModuleCreator(String title) {
        this.title = title;

        //we have to call it here because the team modules will
        //be initialized before this module is initialized.
        int i = 1;
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            i++;
            createTeam(teamModule);
        }
    }

    private void createTeam(TeamModule teamModule) {
        ScoreboardModule scoreboardModule = new ScoreboardModule(teamModule, title);
        scoreboardModule.enable();

        GameHandler.getGameHandler().getMatch().getModules().add(scoreboardModule);
    }

    @EventHandler
    public void onTeamCreation(TeamCreateEvent event) {
        createTeam(event.getTeamModule());
    }
}
