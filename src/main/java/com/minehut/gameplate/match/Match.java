package com.minehut.gameplate.match;

import com.google.gson.JsonObject;
import com.minehut.gameplate.map.CurrentMap;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleCollection;

import java.util.UUID;

/**
 * Created by luke on 12/19/16.
 */
public class Match {
    private UUID uuid;
    private CurrentMap currentMap;
    private ModuleCollection<Module> modules;

    public Match(UUID uuid, CurrentMap currentMap) {
        this.uuid = uuid;
        this.currentMap = currentMap;
    }

    //todo
    public void registerModules() {

    }

    public void unregisterModules() {
        modules.unregisterAll();
    }

    public ModuleCollection<Module> getModules() {
        return modules;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CurrentMap getCurrentMap() {
        return currentMap;
    }

    public JsonObject getJson() {
        return this.currentMap.getMap().getJsonObject();
    }
}
