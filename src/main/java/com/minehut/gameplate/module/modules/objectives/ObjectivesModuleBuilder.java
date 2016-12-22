package com.minehut.gameplate.module.modules.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;

/**
 * Created by luke on 12/21/16.
 */
@BuilderData(load = ModuleLoadTime.LATER)
public class ObjectivesModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        if (match.getJson().has("objectives")) {
            for (JsonElement e : match.getJson().getAsJsonArray("objectives")) {
                JsonObject objectiveJson = e.getAsJsonObject();

                TeamModule teamModule;
                if (!objectiveJson.has("team")) {
                    continue;
                }
                String teamId = objectiveJson.get("team").getAsString();
                if (teamId.equalsIgnoreCase("all")) {
                    teamModule = null;
                } else {
                    teamModule = TeamManager.getTeamById(teamId);
                }

                for (GameObjectiveModule objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjectiveModule.class)) {
                    if (objective.getId().equals(objectiveJson.get("objective").getAsString())) {
                        if (teamModule != null) {
                            teamModule.addObjective(objective);
                        } else { //team = all
                            for (TeamModule team : TeamManager.getTeamModules()) {
                                if(team.isObserver()) continue;

                                team.addObjective(objective);
                            }
                        }
                    }
                }
            }
        }


        return null;
    }
}
