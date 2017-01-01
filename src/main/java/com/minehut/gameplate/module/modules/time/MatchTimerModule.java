package com.minehut.gameplate.module.modules.time;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.match.Match;
import org.bukkit.event.EventHandler;

import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;

public class MatchTimerModule extends Module {

	private long startTime, endTime;

	public MatchTimerModule() {
		this.startTime = System.currentTimeMillis(); //make sure a time gets set incase match never starts.
		this.endTime = 0;
	}

	@EventHandler
	public void onStart(MatchStartEvent event){
		this.startTime = System.currentTimeMillis();
	}

	@EventHandler
	public void onMatchEnd(MatchEndEvent event) {
		this.endTime = (System.currentTimeMillis() - (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimerModule.class)).getTime()) / (long) 1000.0;
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
