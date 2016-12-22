package com.minehut.gameplate.module.modules.lives;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.respawn.RespawnModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashMap;

/**
 * Created by luke on 12/20/16.
 */
public class LivesModule extends Module {
    private HashMap<TeamModule, Integer> lives = new HashMap<>();

    public LivesModule(HashMap<TeamModule, Integer> lives) {
        this.lives = lives;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(GameDeathEvent event) {
        TeamModule team = TeamManager.getTeamByPlayer(event.getPlayer());

        if (this.lives.containsKey(team)) {
            int lives = this.lives.get(team);
            this.lives.put(team, lives - 1);
            lives--;

            if (lives <= 0) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(RespawnModule.class).setTeamCanRespawn(team, false);
            }
        }
    }

    public boolean isTeamOutOfLives(TeamModule teamModule) {
        if (this.lives.containsKey(teamModule)) {
            return this.lives.get(teamModule) <= 0;
        }
        return false;
    }
}
