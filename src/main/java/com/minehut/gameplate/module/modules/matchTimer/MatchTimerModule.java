package com.minehut.gameplate.module.modules.matchTimer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;

public class MatchTimerModule extends Module {

	private int elapsed = 0, runnable;
	
	@EventHandler
	public void onStart(MatchStartEvent event){
		startTimer();
	}
	
	@Override
	public void disable(){
		if(Bukkit.getServer().getScheduler().isCurrentlyRunning(runnable))
			Bukkit.getServer().getScheduler().cancelTask(runnable);
	}
	
	private void startTimer(){
		runnable = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GamePlate.getInstance(), () -> {
			this.elapsed++;
		}, 1, 1);
	}
	
	public int getTimeElapsed(){
		return elapsed;
	}
	
}
