package com.minehut.gameplate.module.modules.lives;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Numbers;
import org.jdom2.Element;

import java.util.HashMap;

/**
 * Created by luke on 12/19/16.
 */
public class LivesModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        HashMap<TeamModule, Integer> lives = new HashMap<>();

        for (Element livesElement : match.getDocument().getRootElement().getChildren("respawns")) {
            for (Element element : livesElement.getChildren()) {
                if (element.getName().toLowerCase().equals("respawn")) {
                    TeamModule teamModule = TeamManager.getTeamById(element.getAttributeValue("team"));
                    int amount = Numbers.parseInt(element.getAttributeValue("lives"));
                    lives.put(teamModule, amount);
                }
            }
        }

        if (lives.isEmpty()) {
            return null;
        } else {
            return new ModuleCollection<>(new LivesModule(lives));
        }
    }
}
