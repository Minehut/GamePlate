package com.minehut.gameplate.module.modules.objectives.score.types;

import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.objectives.score.ScoreObjectiveModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.event.EventHandler;

/**
 * Created by luke on 12/28/16.
 */
public class KillScoreTypeModule extends Module {
    private ScoreObjectiveModule scoreObjectiveModule;
    private int amount;

    public KillScoreTypeModule(ScoreObjectiveModule scoreObjectiveModule, int amount) {
        this.scoreObjectiveModule = scoreObjectiveModule;
        this.amount = amount;
    }

    @EventHandler
    public void onGameDeath(GameDeathEvent event) {
        if (event.getKiller() != null) {
            scoreObjectiveModule.addPoints(TeamManager.getTeamByPlayer(event.getKiller()), amount);
        }
    }
}
