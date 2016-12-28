package com.minehut.gameplate.module.modules.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.jdom2.Element;

/**
 * Created by luke on 12/21/16.
 */
@BuilderData(load = ModuleLoadTime.LATER)
public class ObjectivesModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        for (Element objectivesElement : match.getDocument().getRootElement().getChildren("objectives")) {
            for (Element element : objectivesElement.getChildren()) {
                boolean all = false;
                TeamModule teamModule = null;

                if (element.getAttributeValue("team").equals("all")) {
                    all = true;
                } else {
                    TeamManager.getTeamById(element.getAttributeValue("team"));
                }

                for (GameObjectiveModule objectiveModule : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjectiveModule.class)) {
                    if (objectiveModule.getId().equals(objectivesElement.getAttributeValue("objective"))) {
                        if (all) {
                            for (TeamModule allTeam : TeamManager.getTeamModules()) {
                                if (allTeam.isObserver()) {
                                    continue;
                                }
                                teamModule.addObjective(objectiveModule);
                            }
                        } else {
                            teamModule.addObjective(objectiveModule);
                        }
                    }
                }


            }
        }

        return null;
    }
}
