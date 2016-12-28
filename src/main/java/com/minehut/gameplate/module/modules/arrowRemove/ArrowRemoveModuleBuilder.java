package com.minehut.gameplate.module.modules.arrowRemove;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.buildHeight.BuildHeightModule;
import org.jdom2.Element;

/**
 * Created by Lucas on 12/20/2016.
 */
public class ArrowRemoveModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        for (Element element : match.getDocument().getRootElement().getChildren("removeArrows")) {
            boolean removeArrows = element.getValue().equals("true");
            if (removeArrows)
                return new ModuleCollection<>(new ArrowRemoveModule());
        }

        return null;
    }

}
