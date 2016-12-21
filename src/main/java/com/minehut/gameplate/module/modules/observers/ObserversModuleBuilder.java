package com.minehut.gameplate.module.modules.observers;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.gameComplete.GameCompleteModule;

/**
 * Created by luke on 12/19/16.
 */
public class ObserversModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new ObserverModule());
    }
}
