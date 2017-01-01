package com.minehut.gameplate.module.modules.objectives;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
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

                if(element.getAttributeValue("objective") == null) continue; //regions are defined here too.

                boolean all = false;
                TeamModule teamModule = null;

                if (element.getAttributeValue("team").equals("all")) {
                    all = true;
                } else {
                    teamModule = TeamManager.getTeamById(element.getAttributeValue("team"));
                }

                for (ObjectiveModule objectiveModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ObjectiveModule.class)) {
                    if (objectiveModule.getId().equals(element.getAttributeValue("objective"))) {
                        if (all) {
                            for (TeamModule allTeam : TeamManager.getTeamModules()) {
                                if (allTeam.isObserver()) {
                                    continue;
                                }
                                allTeam.addObjective(objectiveModule);
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
