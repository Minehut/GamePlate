package com.minehut.gameplate.module.modules.objectives.score;

import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

/**
 * Created by luke on 12/28/16.
 */
public class ScoreObjectiveModule extends ObjectiveModule {
    private HashMap<TeamModule, Integer> scores = new HashMap<>();
    private int limitScore;

    public ScoreObjectiveModule(int limitScore) {
        super("scores", "Points", true);
        this.limitScore = limitScore;
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        if (super.getCompletedBy().isEmpty()) {
            super.addCompletedBy(getWinningTeam());
        }
    }

    public void addPoints(TeamModule teamModule, int amount) {
        int score = getScore(teamModule) + amount;

        this.scores.put(teamModule, score);

        if (score >= limitScore) {
            super.addCompletedBy(teamModule);
        }
    }

    public int getScore(TeamModule teamModule) {
        if(teamModule == null) return -1;

        if (scores.containsKey(teamModule)) {
            return scores.get(teamModule);
        }
        return 0;
    }

    public TeamModule getWinningTeam() {
        TeamModule winningTeam = null;
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            if(teamModule.isObserver()) continue;

            if (getScore(teamModule) > getScore(winningTeam)) {
                winningTeam = teamModule;
            }
        }
        return winningTeam;
    }
}
