package com.minehut.gameplate.module.modules.objectives.score;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.scoreboard.ScoreboardModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

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

    @EventHandler(priority = EventPriority.LOW)
    public void onCycle(CycleCompleteEvent event) {
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            if(teamModule.isObserver()) continue;
            this.scores.put(teamModule, 0);
        }
    }

    public void addPoints(TeamModule teamModule, int amount) {
        int score = getScore(teamModule) + amount;

        this.scores.put(teamModule, score);

        if (score >= limitScore) {
            super.addCompletedBy(teamModule);
        }

        Bukkit.broadcastMessage(teamModule.getColor() + teamModule.getName() + ": " + ChatColor.WHITE + scores.get(teamModule) +
            ChatColor.GRAY + "/" + limitScore);

        for (ScoreboardModule scoreboardModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboardModule.refresh(this);
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

    public HashMap<TeamModule, Integer> getScores() {
        return scores;
    }

    public int getLimitScore() {
        return limitScore;
    }
}
