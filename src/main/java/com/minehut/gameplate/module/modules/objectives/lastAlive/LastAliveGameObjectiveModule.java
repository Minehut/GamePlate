package com.minehut.gameplate.module.modules.objectives.lastAlive;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.event.objective.ObjectiveCompleteEvent;
import com.minehut.gameplate.module.GameObjectiveModule;
import com.minehut.gameplate.module.modules.lives.LivesModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;

/**
 * Created by luke on 12/21/16.
 */
public class LastAliveGameObjectiveModule extends GameObjectiveModule {

    public LastAliveGameObjectiveModule() {
        super("last-alive", "Last Alive", false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGameDeath(GameDeathEvent event) {
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(LivesModule.class).isTeamOutOfLives(event.getTeamModule())) {

            ArrayList<TeamModule> alive = new ArrayList<>();

            for (TeamModule teamModule : TeamManager.getTeamModules()) {
                if(teamModule.isObserver()) continue;

                if (!GameHandler.getGameHandler().getMatch().getModules().getModule(LivesModule.class).isTeamOutOfLives(teamModule)) {
                    alive.add(teamModule);
                }
            }

            if (alive.size() == 1) {
                ObjectiveCompleteEvent objectiveCompleteEvent = new ObjectiveCompleteEvent(this, alive.get(0));
                Bukkit.getPluginManager().callEvent(objectiveCompleteEvent);
            }
        }
    }
}
