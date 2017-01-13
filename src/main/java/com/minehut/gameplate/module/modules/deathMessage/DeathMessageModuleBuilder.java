package com.minehut.gameplate.module.modules.deathMessage;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

/**
 * Created by luke on 1/13/17.
 */
public class DeathMessageModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new DeathMessageModule());
    }
}
