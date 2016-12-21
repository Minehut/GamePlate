package com.minehut.gameplate.module.modules.craft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.buildHeight.BuildHeightModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/20/2016.
 */
public class CraftModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("blockedCrafts")) {
            JsonArray array = match.getJson().get("blockedCrafts").getAsJsonArray();
            List<String> blocked = new ArrayList<>();
            array.forEach(s -> blocked.add(s.getAsString()));
            return new ModuleCollection<>(new CraftModule(blocked));
        }

        return null;
    }

}
