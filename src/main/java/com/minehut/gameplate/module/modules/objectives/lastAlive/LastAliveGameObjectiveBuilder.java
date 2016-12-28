package com.minehut.gameplate.module.modules.objectives.lastAlive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import org.jdom2.Element;

/**
 * Created by luke on 12/21/16.
 */
@BuilderData(load = ModuleLoadTime.EARLIER)
public class LastAliveGameObjectiveBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        for (Element objectivesElement : match.getDocument().getRootElement().getChildren("objectives")) {
            for (Element element : objectivesElement.getChildren()) {
                if (element.getName().toLowerCase().equals("lastAlive")) {
                    return new ModuleCollection<>(new LastAliveGameObjectiveModule());
                }
            }
        }

        return null;
    }
}
