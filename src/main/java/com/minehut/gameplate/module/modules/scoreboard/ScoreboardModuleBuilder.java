package com.minehut.gameplate.module.modules.scoreboard;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import org.jdom2.Element;

/**
 * Created by luke on 12/31/16.
 */
@BuilderData(load = ModuleLoadTime.NORMAL)
public class ScoreboardModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        String title = null;
        for (Element scoreboardElement : match.getDocument().getRootElement().getChildren("scoreboard")) {
            title = scoreboardElement.getAttributeValue("title");
        }

        return new ModuleCollection<>(new ScoreboardModuleCreator(title));
    }
}
