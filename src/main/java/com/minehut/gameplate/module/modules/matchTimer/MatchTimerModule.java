package com.minehut.gameplate.module.modules.matchTimer;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import org.bukkit.event.EventHandler;

import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;

public class MatchTimerModule extends Module {

	private long startTime, endTime;

	public MatchTimerModule() {
		this.startTime = System.currentTimeMillis(); //make sure a time gets set incase match never starts.
	}

	@EventHandler
	public void onStart(MatchStartEvent event){
		this.startTime = System.currentTimeMillis();
	}

	public static double getTimeInSeconds() {
		Match match = GameHandler.getGameHandler().getMatch();
		if (match.isRunning()) {
			return ((double) System.currentTimeMillis() - (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimerModule.class)).getTime()) / 1000.0;
		}
		if (match.hasEnded()) {
			return match.getModules().getModule(MatchTimerModule.class).getEndTime();
		}
		return 0;
	}

	public long getTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}
}
