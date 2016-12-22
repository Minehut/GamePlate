package com.minehut.gameplate.module.modules.lives;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

/**
 * Created by luke on 12/20/16.
 */
public class LivesModule extends Module {
    private HashMap<TeamModule, Integer> lives = new HashMap<>();

    public LivesModule(HashMap<TeamModule, Integer> lives) {
        this.lives = lives;
    }

    @EventHandler
    public void onDeath(GameDeathEvent event) {
        
    }
}
