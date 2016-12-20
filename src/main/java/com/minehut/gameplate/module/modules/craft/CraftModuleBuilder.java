package com.minehut.gameplate.module.modules.craft;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.buildHeight.BuildHeightModule;

/**
 * Created by Lucas on 12/20/2016.
 */
public class CraftModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("blockedCrafts")) {
            return new ModuleCollection<>(new CraftModule(match.getJson().get("blockedCrafts").getAsJsonArray()));
        }

        return null;
    }

}
