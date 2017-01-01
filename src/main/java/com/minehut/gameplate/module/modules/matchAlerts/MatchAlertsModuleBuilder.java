package com.minehut.gameplate.module.modules.matchAlerts;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import org.bukkit.Bukkit;

/**
 * Created by luke on 12/28/16.
 */
public class MatchAlertsModuleBuilder extends ModuleBuilder {
    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        Bukkit.broadcastMessage("MatchAlertsModuleBuilder load()");
        return new ModuleCollection<>(new MatchAlertsModule());
    }
}
