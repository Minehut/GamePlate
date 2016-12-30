package com.minehut.gameplate.module.modules.matchStart;

import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

/**
 * Created by luke on 12/28/16.
 */
public class MatchStartModule extends Module {

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        Bukkit.broadcastMessage("The match has started!");
    }
}
