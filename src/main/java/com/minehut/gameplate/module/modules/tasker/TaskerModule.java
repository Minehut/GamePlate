package com.minehut.gameplate.module.modules.tasker;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.module.TaskedModule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class TaskerModule extends TaskedModule {

    private final UUID matchUuid;

    public TaskerModule() {
        this.matchUuid = GameHandler.getGameHandler().getMatch().getUuid();
    }

    @Override
    public void run() {
        if (matchUuid.equals(GameHandler.getGameHandler().getMatch().getUuid())) { //stops this from running once a new match starts.
            for (TaskedModule task : GameHandler.getGameHandler().getMatch().getModules().getModules(TaskedModule.class)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), task, 1);
            }
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        run();
    }
}
