package com.minehut.gameplate.module.modules.objectives.score;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.objectives.score.types.DeathScoreTypeModule;
import com.minehut.gameplate.module.modules.objectives.score.types.GoalScoreTypeModule;
import com.minehut.gameplate.module.modules.objectives.score.types.KillScoreTypeModule;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Numbers;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by luke on 12/28/16.
 */
public class ScoreObjectiveModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        for (Element scoreElement : match.getDocument().getRootElement().getChildren("score")) {
            int limit = Integer.MAX_VALUE;
            if (scoreElement.getChild("limit") != null) {
                limit = Numbers.parseInt(scoreElement.getChild("limit").getTextNormalize());
            }
            ScoreObjectiveModule scoreObjectiveModule = new ScoreObjectiveModule(limit);
            results.add(scoreObjectiveModule);

            if (scoreElement.getChild("kill") != null) {
                int amount = Numbers.parseInt(scoreElement.getChild("kill").getTextNormalize());
                results.add(new KillScoreTypeModule(scoreObjectiveModule, amount));
            }
            if (scoreElement.getChild("death") != null) {
                int amount = Numbers.parseInt(scoreElement.getChild("death").getTextNormalize());
                results.add(new DeathScoreTypeModule(scoreObjectiveModule, amount));
            }
            for (Element element : scoreElement.getChildren("goal")) {
                int amount = Numbers.parseInt(element.getAttributeValue("amount"));
                TeamModule teamModule = null;
                if (element.getAttributeValue("team") != null) {
                    teamModule = TeamManager.getTeamById(element.getAttributeValue("team"));
                }
                String message = element.getAttributeValue("message");
                List<RegionModule> regions = RegionModuleBuilder.parseChildRegions(element);
                results.add(new GoalScoreTypeModule(scoreObjectiveModule, amount, teamModule, message, regions));
            }
        }
        return results;
    }
}
