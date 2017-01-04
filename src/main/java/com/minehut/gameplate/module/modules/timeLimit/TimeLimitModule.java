package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.TaskedModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.time.MatchTimerModule;

/**
 * Created by luke on 12/31/16.
 */
public class TimeLimitModule extends TaskedModule {
    private int timeLimit;
    private TeamModule teamModule;

    public TimeLimitModule(int timeLimit, TeamModule teamModule) {
        this.timeLimit = timeLimit;
        this.teamModule = teamModule;
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
                GameHandler.getGameHandler().getMatch().end(teamModule);
            }
        }
    }
}
