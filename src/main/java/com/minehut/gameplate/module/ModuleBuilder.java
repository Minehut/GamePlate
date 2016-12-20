package com.minehut.gameplate.module;

import com.minehut.gameplate.match.Match;

public abstract class ModuleBuilder {

    public ModuleBuilder() {
    }

    public abstract ModuleCollection<? extends Module> load(Match match);

}
