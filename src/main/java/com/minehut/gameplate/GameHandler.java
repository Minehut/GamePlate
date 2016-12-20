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
    private WeakReference<World> matchWorld;
    private Match match;
    private CurrentMap currentMap;
    private File matchFile;
    private boolean globalMute;

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
        if (repositoryManager.getRotation().getNext().equals(currentMap.getMap())) {
            repositoryManager.getRotation().move();
        }
        World oldMatchWorld = matchWorld == null ? null : matchWorld.get();
        currentMap.run();
        if (match != null) match.unregisterModules();
        this.match = new Match(currentMap.getUuid(), currentMap);
        this.match.registerModules();
        GamePlate.getInstance().getLogger().info(this.match.getModules().size() + " modules loaded.");
        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(match));

        currentMap = new CurrentMap(repositoryManager.getRotation().getNext(), UUID.randomUUID());
        Bukkit.unloadWorld(oldMatchWorld, true);
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public World getMatchWorld() {
        return matchWorld.get();
    }

    public void setMatchWorld(World world) {
        matchWorld = new WeakReference<>(world);
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public File getMatchFile() {
        return matchFile;
    }

    public void setMatchFile(File file) {
        matchFile = file;
    }

    public boolean getGlobalMute() {
        return globalMute;
    }

    public void setGlobalMute(boolean globalMute) {
        this.globalMute = globalMute;
    }

    public boolean toggleGlobalMute() {
        globalMute = !globalMute;
        return globalMute;
    }
}
