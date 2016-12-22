package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("spawns")) {

        }

        return null;
    }
}
