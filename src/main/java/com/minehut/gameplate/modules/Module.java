package com.minehut.gameplate.modules;

import com.minehut.gameplate.GamePlate;
import org.bukkit.event.Listener;

/**
 * Created by Lucas on 12/18/2016.
 */
public abstract class Module implements Listener {

    public void initialize() {
        GamePlate.getInstance().register(this);
    }

}
