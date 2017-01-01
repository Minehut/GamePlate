package com.minehut.gameplate.module.modules.scoreboard;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import org.bukkit.Bukkit;

/**
 * Created by luke on 12/31/16.
 */
public class ScoreboardModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new ScoreboardModuleCreator());
    }
}