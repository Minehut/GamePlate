package com.minehut.gameplate.module.modules.blockPlaceModule;

import com.google.gson.JsonArray;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/20/2016.
 */
public class BlockPlaceModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        for (Element element : match.getDocument().getRootElement().getChildren("blockedPlace")) {
            List<String> blocked = new ArrayList<>();
            element.getChildren().forEach(e -> blocked.add(e.toString()));
            return new ModuleCollection<>(new BlockPlaceModule(blocked));
        }

        return null;
    }
}
