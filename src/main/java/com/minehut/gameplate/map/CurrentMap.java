package com.minehut.gameplate.map;

import com.minehut.gameplate.map.generate.GenerateMap;
import com.minehut.gameplate.map.generate.NullChunkGenerator;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.UUID;

public class CurrentMap implements Runnable {

    private final UUID uuid;
    private LoadedMap map;
    private World world;

    public CurrentMap(LoadedMap map, UUID uuid) {
        this.map = map;
        this.uuid = uuid;
    }

    public LoadedMap getMap() {
        return map;
    }

    public void setMap(LoadedMap map) {
        this.map = map;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void run() {
        GenerateMap.copyWorldFromRepository(map.getFolder(), uuid);
        this.world = new WorldCreator("matches/" + uuid.toString()).generator(new NullChunkGenerator()).createWorld();
        world.setPVP(true);
    }

    public World getWorld() {
        return world;
    }
}
