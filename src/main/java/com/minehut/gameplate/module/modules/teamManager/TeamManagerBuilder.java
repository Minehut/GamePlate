package com.minehut.gameplate.module.modules.teamManager;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by luke on 12/19/16.
 */

@BuilderData(load = ModuleLoadTime.EARLIER)
public class TeamManagerBuilder extends ModuleBuilder {
    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        ModuleCollection results = new ModuleCollection();

        TeamManager.TeamType teamType = TeamManager.TeamType.TRADITIONAL;
        if (match.getJson().has("teamType")) {
            teamType = TeamManager.TeamType.valueOf(match.getJson().get("teamType").getAsString().toUpperCase().replace(" ", "_"));
        }
        results.add(new TeamManager(teamType));
        results.add(new TeamModule("observers", "Observers", true, ChatColor.AQUA, Integer.MAX_VALUE, Integer.MAX_VALUE, TeamModule.JoinAllowance.ALL));

        return results;
    }
}
