package com.minehut.gameplate.module;

import com.minehut.gameplate.GamePlate;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Created by Lucas on 12/18/2016.
 */
public abstract class Module implements Listener {

    public void enable() {
        GamePlate.getInstance().register(this);
    }

    public void disable() {
        HandlerList.unregisterAll(this);
    }

}
