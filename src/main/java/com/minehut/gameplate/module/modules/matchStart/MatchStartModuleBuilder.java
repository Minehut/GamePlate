package com.minehut.gameplate.module.modules.matchStart;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

/**
 * Created by luke on 12/28/16.
 */
public class MatchStartModuleBuilder extends ModuleBuilder {
    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new MatchStartModule());
    }
}
