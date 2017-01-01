package com.minehut.gameplate;

import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.map.CurrentMap;
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

        if (repositoryManager.getRotation().getNext().equals(currentMap.getMap())) {
            repositoryManager.getRotation().move();
        }

        if (match != null) match.unregisterModules();

        if (repositoryManager.getRotation().getForcedNextMap() != null) {
            currentMap = new CurrentMap(repositoryManager.getRotation().getForcedNextMap(), UUID.randomUUID());
            repositoryManager.getRotation().setForcedNextMap(null);
        } else {
            currentMap = new CurrentMap(repositoryManager.getRotation().getNext(), UUID.randomUUID());
        }

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
