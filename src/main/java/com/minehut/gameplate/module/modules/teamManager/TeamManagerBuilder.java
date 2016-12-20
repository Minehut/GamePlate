package com.minehut.gameplate.module.modules.teamManager;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;

/**
 * Created by luke on 12/19/16.
 */

@BuilderData(load = ModuleLoadTime.EARLIER)
public class TeamManagerBuilder extends ModuleBuilder {
    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        TeamManager.TeamType teamType = TeamManager.TeamType.TRADITIONAL;
        if (match.getJson().has("teamType")) {
            teamType = TeamManager.TeamType.valueOf(match.getJson().get("teamType").getAsString().toUpperCase().replace(" ", "_"));
        }

        return new ModuleCollection<>(new TeamManager(teamType));
    }
}
