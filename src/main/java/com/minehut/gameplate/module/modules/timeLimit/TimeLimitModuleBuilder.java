package com.minehut.gameplate.module.modules.timeLimit;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.util.Parser;
import org.jdom2.Element;

public class TimeLimitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        for (Element timeLimitElement : match.getDocument().getRootElement().getChildren("timeLimit")) {
            int time = Parser.timeStringToSeconds(timeLimitElement.getTextNormalize());
            return new ModuleCollection<>(new TimeLimitModule(time));
        }

        return null;
    }

}
