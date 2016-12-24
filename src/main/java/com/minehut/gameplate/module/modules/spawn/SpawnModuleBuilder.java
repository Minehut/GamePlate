package com.minehut.gameplate.module.modules.spawn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        List<SpawnNode> spawns = new ArrayList<>();
        if (match.getJson().has("spawns")) {
            for (JsonElement e : match.getJson().getAsJsonArray("spawns")) {
                JsonObject jsonObject = e.getAsJsonObject();

                TeamModule teamModule = null;
                if (jsonObject.has("team")) {
                    teamModule = TeamManager.getTeamById(jsonObject.get("team").getAsString());
                }

                RegionModule regionModule = RegionModuleBuilder.parseRegion(jsonObject, "region");
                if (regionModule == null) {
                    Bukkit.getLogger().log(Level.SEVERE, "Spawn Region was null!");
                } else {
                    Bukkit.getLogger().log(Level.SEVERE, "Spawn Region was not null, blocks = " + regionModule.getBlocks().size());
                }

                float yaw = 0;
                if (jsonObject.has("yaw")) {
                    yaw = (float) Numbers.parseDouble(jsonObject.get("yaw").getAsString());
                }

                float pitch = 0;
                if (jsonObject.has("pitch")) {
                    pitch = (float) Numbers.parseDouble(jsonObject.get("pitch").getAsString());
                }

                if (teamModule != null) {
                    Bukkit.getLogger().log(Level.SEVERE, "Adding spawn for " + teamModule.getName());
                }

                SpawnNode spawnNode = new SpawnNode(regionModule, teamModule, yaw, pitch);

                if (teamModule != null) {
                    teamModule.addSpawn(spawnNode);
                }

                spawns.add(spawnNode);
            }
        }

        return new ModuleCollection<>(new SpawnModule(spawns));
    }
}
