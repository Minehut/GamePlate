package com.minehut.gameplate;

import com.minehut.gameplate.modules.Module;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/18/2016.
 */
public class GamePlate extends JavaPlugin {

    private static GamePlate instance;

    private List<Module> modules = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static GamePlate getInstance() { return instance; }

    public void register(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

}
