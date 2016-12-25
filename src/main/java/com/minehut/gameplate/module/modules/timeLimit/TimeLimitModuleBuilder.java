package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.util.Parser;

public class TimeLimitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        if (match.getJson().has("timeLimit")) {
            int time = Parser.timeStringToSeconds(match.getJson().get("timeLimit").getAsString());
            return new ModuleCollection<>(new TimeLimitModule(time));
        }

        return null;
    }

}
