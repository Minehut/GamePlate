package com.minehut.gameplate.module.modules.tracker;


import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

public class TrackerBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new TntTracker(), new DamageTracker(), new SpleefTracker());
    }
}
