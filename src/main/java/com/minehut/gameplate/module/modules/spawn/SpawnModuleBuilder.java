package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.kit.KitModule;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Numbers;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        List<SpawnNode> spawns = new ArrayList<>();

        for (Element spawnsElement : match.getDocument().getRootElement().getChildren("spawns")) {
            for (Element element : spawnsElement.getChildren()) {

                TeamModule teamModule = null;
                if (element.getAttributeValue("team") != null) {
                    teamModule = TeamManager.getTeamById(element.getAttributeValue("team"));
                }

                float yaw = 0;
                if (element.getAttributeValue("yaw") != null) {
                    yaw = (float) Numbers.parseDouble(element.getAttributeValue("yaw"));
                }

                float pitch = 0;
                if (element.getAttributeValue("pitch") != null) {
                    pitch = (float) Numbers.parseDouble(element.getAttributeValue("pitch"));
                }

                RegionModule regionModule = RegionModuleBuilder.parseChildRegions(element).get(0);

                KitModule kitModule = null;
                if (element.getAttributeValue("kit") != null) {
                    for (KitModule search : GameHandler.getGameHandler().getMatch().getModules().getModules(KitModule.class)) {
                        if (search.getId().equals(element.getAttributeValue("kit"))) {
                            kitModule = search;
                        }
                    }
                }

                SpawnNode spawnNode = new SpawnNode(regionModule, teamModule, kitModule, yaw, pitch);

                if (teamModule != null) {
                    teamModule.addSpawn(spawnNode);
                }

                spawns.add(spawnNode);
            }
        }

        return new ModuleCollection<>(new SpawnModule(spawns));
    }
}
