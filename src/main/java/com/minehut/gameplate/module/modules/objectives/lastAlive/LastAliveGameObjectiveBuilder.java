package com.minehut.gameplate.module.modules.objectives.lastAlive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;

/**
 * Created by luke on 12/21/16.
 */
@BuilderData(load = ModuleLoadTime.EARLIER)
public class LastAliveGameObjectiveBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        if (match.getJson().has("objectives")) {
            for (JsonElement e : match.getJson().getAsJsonArray("objectives")) {
                JsonObject objectiveJson = e.getAsJsonObject();

                if (objectiveJson.has("objective")) {
                    if (objectiveJson.get("objective").getAsString().equalsIgnoreCase("last-alive")) {
                        return new ModuleCollection<>(new LastAliveGameObjectiveModule());
                    }
                }
            }
        }

        return null;
    }
}
