package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
public class TimeLimitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
    	if (!match.getJson().has("timeLimit"))
        	return null;
        int time = match.getJson().get("timeLimit").getAsInt();
        return new ModuleCollection<>(new TimeLimitModule(time*20));
    }

}
