package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.TaskedModule;
import com.minehut.gameplate.module.modules.time.MatchTimerModule;

/**
 * Created by luke on 12/31/16.
 */
public class TimeLimitModule extends TaskedModule {
    private int timeLimit;

    public TimeLimitModule(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getLimit() {
        return this.timeLimit;
    }

    public static int getTimeLimit() {
        for (TimeLimitModule timeLimitModule : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimitModule.class)) {
            return timeLimitModule.getLimit();
        }
        return 0;
    }


    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (MatchTimerModule.getTimeInSeconds() >= timeLimit) {
                GameHandler.getGameHandler().getMatch().end(null);
            }
        }
    }
}
