package com.minehut.gameplate;

import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.map.CurrentMap;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.map.repository.RepositoryManager;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.ModuleFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.logging.Level;

public class GameHandler {

    private static GameHandler handler;
    private final ModuleFactory moduleFactory;
    private RepositoryManager repositoryManager;
    private Match match;
    private CurrentMap currentMap;

    public GameHandler() throws RotationLoadException {
        handler = this;
        this.moduleFactory = new ModuleFactory();
    }

    public void load() throws RotationLoadException {
        repositoryManager = new RepositoryManager();
        repositoryManager.setupRotation();

        currentMap = new CurrentMap(repositoryManager.getRotation().getNext(), UUID.randomUUID());
        Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                cycleAndMakeMatch();
            }
        });
    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    public void cycleAndMakeMatch() {
        final World oldWorld = currentMap != null ? currentMap.getWorld() : null;

        if (currentMap != null) {
            repositoryManager.getRotation().move();
        }

        if (match != null) match.unregisterModules();

        LoadedMap next = repositoryManager.getRotation().getNext();
        Bukkit.getLogger().log(Level.INFO, "Loading map " + next.getName());
        currentMap = new CurrentMap(next, UUID.randomUUID());
        repositoryManager.getRotation().setNextSelfAssignedMap(null);

        currentMap.run();

        this.match = new Match(currentMap.getUuid(), currentMap);
        this.match.registerModules();
        GamePlate.getInstance().getLogger().info(this.match.getModules().size() + " modules loaded.");
        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(match));

        if (oldWorld != null) {
            Bukkit.unloadWorld(oldWorld, false);
        }
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public ModuleFactory getModuleFactory() {
        return moduleFactory;
    }

    public CurrentMap getCurrentMap() {
        return currentMap;
    }
}
