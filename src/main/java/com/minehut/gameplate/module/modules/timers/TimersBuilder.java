package com.minehut.gameplate.module.modules.timers;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

public class TimersBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<Countdown> load(Match match) {
        return new ModuleCollection<>(new StartTimer(), new CycleTimer());
    }

}
