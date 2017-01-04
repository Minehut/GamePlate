package com.minehut.gameplate.module.modules.scoreboard;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;

/**
 * Created by luke on 12/31/16.
 */
@BuilderData(load = ModuleLoadTime.NORMAL)
public class ScoreboardModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new ScoreboardModuleCreator());
    }
}
