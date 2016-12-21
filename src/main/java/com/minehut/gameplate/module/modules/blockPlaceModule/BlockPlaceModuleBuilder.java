package com.minehut.gameplate.module.modules.blockPlaceModule;

import com.google.gson.JsonArray;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/20/2016.
 */
public class BlockPlaceModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("blockedPlace")) {
            JsonArray array = match.getJson().get("blockedPlace").getAsJsonArray();
            List<String> blocked = new ArrayList<>();
            array.forEach(s -> blocked.add(s.getAsString()));
            return new ModuleCollection<>(new BlockPlaceModule(blocked));
        }

        return null;
    }
}
