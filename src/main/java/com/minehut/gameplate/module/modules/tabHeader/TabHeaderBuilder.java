package com.minehut.gameplate.module.modules.tabHeader;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

/**
 * Created by luke on 1/3/17.
 */
public class TabHeaderBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new TabHeaderModule());
    }
}
