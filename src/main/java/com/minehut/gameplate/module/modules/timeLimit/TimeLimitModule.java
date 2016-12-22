package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.matchTimer.MatchTimerModule;

public class TimeLimitModule extends Module {

	private int limit = 0;
	
	public TimeLimitModule(int time){
		this.limit = time;
	}
	
	public int getTimeLeft(){
		if(GameHandler.getGameHandler().getMatch().getModules().contains(MatchTimerModule.class))
			return limit - GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimerModule.class).getTimeElapsed();
		return Integer.MAX_VALUE;
	}
	
	public int getTimeLimit(){
		return this.limit;
	}
	
}
