package com.minehut.gameplate.module.modules.teamManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.util.Parser;
import org.bukkit.ChatColor;

/**
 * Created by luke on 12/19/16.
 */

@BuilderData(load = ModuleLoadTime.EARLIER)
public class TeamManagerBuilder extends ModuleBuilder {
    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        ModuleCollection results = new ModuleCollection();

        TeamManager.TeamType teamType = TeamManager.TeamType.STATIC;

        if (match.getJson().get("teams").isJsonPrimitive()) {
            teamType = TeamManager.TeamType.valueOf(match.getJson().get("teams").getAsString().toUpperCase().replace(" ", "_"));
        } else {
            for (JsonElement e : match.getJson().getAsJsonArray("teams")) {
                JsonObject jsonObject = e.getAsJsonObject();

                String id = jsonObject.get("id").getAsString();
                String name = jsonObject.get("name").getAsString();
                ChatColor color = Parser.parseColor(jsonObject.get("color").getAsString());
                int maxPlayers = jsonObject.get("max").getAsInt();

                int maxOverfill;
                if (jsonObject.has("maxOverfill")) {
                    maxOverfill = jsonObject.get("maxOverfill").getAsInt();
                } else {
                    maxOverfill = maxPlayers + 10;
                }

                TeamModule teamModule = new TeamModule(id, name, false, color, maxPlayers, maxOverfill, TeamModule.JoinAllowance.ALL);
                results.add(teamModule);
            }
        }

        results.add(new TeamManager(teamType));
        results.add(new TeamModule("observers", "Observers", true, ChatColor.AQUA, Integer.MAX_VALUE, Integer.MAX_VALUE, TeamModule.JoinAllowance.ALL));

        return results;
    }
}
