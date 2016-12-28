package com.minehut.gameplate.module.modules.buildHeight;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import org.jdom2.Element;

/**
 * Created by luke on 12/19/16.
 */
public class BuildHeightModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        for (Element element : match.getDocument().getRootElement().getChildren()) {
            int height;
            try {
                height = Integer.parseInt(element.getValue());
            } catch (NumberFormatException ex) {
                break;
            }
            return new ModuleCollection<>(new BuildHeightModule(height));
        }

        return null;
    }
}
