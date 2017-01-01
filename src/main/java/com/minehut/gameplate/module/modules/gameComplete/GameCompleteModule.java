package com.minehut.gameplate.module.modules.gameComplete;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.objective.ObjectiveCompleteEvent;
import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class GameCompleteModule extends Module {

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            for (TeamModule team : TeamManager.getTeamModules()) {
                if(team.isObserver()) continue;

                if (testWin(team)) {
                    GameHandler.getGameHandler().getMatch().end(team);
                    return;
                }
            }
        }
    }

    /*
     * Returns true if the team wins.
     */
    private boolean testWin(TeamModule team) {

        int completed = 0;
        int needed = team.getObjectives().size();

        for (ObjectiveModule objective : team.getObjectives()) {
            if (objective.isCompletedBy(team)) {
                completed++;
            }
        }

        return completed >= needed;
    }
}
