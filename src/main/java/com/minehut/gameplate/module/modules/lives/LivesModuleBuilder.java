package com.minehut.gameplate.module.modules.lives;

import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.observers.ObserverModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luke on 12/19/16.
 */
public class LivesModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        HashMap<TeamModule, Integer> lives = new HashMap<>();

        if (match.getJson().has("lives")) {
            JsonObject livesJson = match.getJson().getAsJsonObject("lives");
            for (TeamModule teamModule : TeamManager.getTeamModules()) {
                if(teamModule.isObserver()) continue;

                if (livesJson.has(teamModule.getId())) {
                    lives.put(teamModule, livesJson.get(teamModule.getId()).getAsInt());
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
