package com.minehut.gameplate.module.modules.teamManager;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.ChatColor;

/**
 * Created by luke on 12/19/16.
 */

@BuilderData(load = ModuleLoadTime.EARLIER)
public class TeamManagerBuilder extends ModuleBuilder {
    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        ModuleCollection results = new ModuleCollection();

        TeamManager.TeamType teamType = TeamManager.TeamType.TRADITIONAL;

        if (match.getJson().get("teams").isJsonPrimitive()) {
            teamType = TeamManager.TeamType.valueOf(match.getJson().get("teams").getAsString().toUpperCase().replace(" ", "_"));
        } else {
            //todo: manual team parsing
        }

        results.add(new TeamManager(teamType));
        results.add(new TeamModule("observers", "Observers", true, ChatColor.AQUA, Integer.MAX_VALUE, Integer.MAX_VALUE, TeamModule.JoinAllowance.ALL));

        return results;
    }
}
