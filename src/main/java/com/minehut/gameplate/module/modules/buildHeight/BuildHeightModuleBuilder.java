package com.minehut.gameplate.module.modules.buildHeight;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 12/19/16.
 */
public class BuildHeightModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        List<Element> buildHeightElements = new ArrayList<>();
        buildHeightElements.addAll(match.getDocument().getRootElement().getChildren("buildHeight"));
        buildHeightElements.addAll(match.getDocument().getRootElement().getChildren("maxHeight"));

        for (Element element : buildHeightElements) {
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
