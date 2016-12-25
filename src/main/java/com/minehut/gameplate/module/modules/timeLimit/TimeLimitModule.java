package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.matchTimer.MatchTimerModule;

public class TimeLimitModule extends Module {

	private int limit = 0; //seconds
	
	public TimeLimitModule(int time) {
		this.limit = time;
	}
	
	public int getTimeLeft() {
		return (int) (limit - MatchTimerModule.getTimeInSeconds());
	}
	
	public int getTimeLimit(){
		return this.limit;
	}
	
}
