package com.minehut.gameplate;

import com.minehut.gameplate.chat.LocaleHandler;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.module.Module;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lucas on 12/18/2016.
 */
public class GamePlate extends JavaPlugin {

    private static GamePlate instance;

    private LocaleHandler localeHandler;
    private GameHandler gameHandler;

    @Override
    public void onEnable() {
        instance = this;

        try {
            this.localeHandler = new LocaleHandler(this, Arrays.asList("lang/en.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.gameHandler = new GameHandler();
        } catch (RotationLoadException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static GamePlate getInstance() { return instance; }

    public void register(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public LocaleHandler getLocaleHandler() {
        return localeHandler;
    }
}
