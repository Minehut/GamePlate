package com.minehut.gameplate.module.modules.gameComplete;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.objective.ObjectiveCompleteEvent;
import com.minehut.gameplate.module.GameObjectiveModule;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.event.EventHandler;

public class GameCompleteModule extends Module {

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        event.getObjective().addCompletedBy(event.getTeamModule());

        for (TeamModule team : TeamManager.getTeamModules()) {
            if (testWin(team)) {
                GameHandler.getGameHandler().getMatch().end(team);
            }
        }
    }

    /*
     * Returns true if the team wins.
     */
    private boolean testWin(TeamModule team) {

        int completed = 0;
        int needed = team.getObjectives().size();

        for (GameObjectiveModule objective : team.getObjectives()) {
            if (objective.isCompletedBy(team)) {
                completed++;
            }
        }

        return completed >= needed;
    }
}
