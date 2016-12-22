package com.minehut.gameplate.module.modules.kit;

import com.google.gson.JsonArray;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("kits")) {
            JsonArray kitArray = match.getJson().get("kits").getAsJsonArray();
            List<Kit> kits = new ArrayList<>();
            kitArray.forEach(element -> {

            });
        }

        return null;
    }
}
