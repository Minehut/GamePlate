package com.minehut.gameplate.module.modules.buildHeight;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

/**
 * Created by luke on 12/19/16.
 */
public class BuildHeightModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("buildHeight")) {
            return new ModuleCollection<>(new BuildHeightModule(match.getJson().get("buildHeight").getAsInt()));
        }

        return null;
    }
}
