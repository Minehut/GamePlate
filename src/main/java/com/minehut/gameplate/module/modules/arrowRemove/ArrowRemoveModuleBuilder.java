package com.minehut.gameplate.module.modules.arrowRemove;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.buildHeight.BuildHeightModule;

/**
 * Created by Lucas on 12/20/2016.
 */
public class ArrowRemoveModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("removeArrows")) {
            boolean removeArrows = match.getJson().get("removeArrows").getAsString().equals("true");
            if (removeArrows)
                return new ModuleCollection<>(new ArrowRemoveModule());
        }

        return null;
    }

}
