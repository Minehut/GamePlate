package com.minehut.gameplate.module.modules.matchTimer;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

public class MatchTimerModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
    	return new ModuleCollection<>(new MatchTimerModule());
    }

}
