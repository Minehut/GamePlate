package com.minehut.gameplate.match;

import com.google.gson.JsonObject;
import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.map.CurrentMap;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.ModuleLoadTime;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.Bukkit;
import org.jdom2.Document;

import java.util.UUID;

/**
 * Created by luke on 12/19/16.
 */
public class Match {
    private UUID uuid;
    private CurrentMap currentMap;
    private ModuleCollection<Module> modules;
    private MatchState matchState;

    public Match(UUID uuid, CurrentMap currentMap) {
        this.uuid = uuid;
        this.modules = new ModuleCollection<>();
        this.currentMap = currentMap;
        this.matchState = MatchState.WAITING;
    }

    public void registerModules() {
        for (ModuleLoadTime time : ModuleLoadTime.getOrdered()) {
            for (Module module : GameHandler.getGameHandler().getModuleFactory().build(this, time)) {
                modules.add(module);
                module.enable();
            }
        }
    }

    public void end(TeamModule teamModule) {
        setMatchState(MatchState.ENDED);
        MatchEndEvent event = new MatchEndEvent(teamModule);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
        if(matchState.equals(MatchState.PLAYING))
        	Bukkit.getPluginManager().callEvent(new MatchStartEvent());
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

    public boolean isWaiting() {
        return matchState == MatchState.WAITING;
    }

    public boolean isStarting() {
        return matchState == MatchState.STARTING;
    }

    public boolean isRunning() {
        return matchState == MatchState.PLAYING;
    }

    public boolean hasEnded() {
        return matchState == MatchState.ENDED || matchState == MatchState.CYCLING;
    }

    public boolean isState(MatchState state) {
        return this.matchState == state;
    }

    public Document getDocument() {
        return this.currentMap.getMap().getDocument();
    }

    public MatchState getMatchState() {
        return matchState;
    }
}
