package com.minehut.gameplate.module.modules.objectives.capturable;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.util.ColorUtil;
import com.minehut.gameplate.util.Numbers;
import com.minehut.gameplate.util.Parser;
import com.minehut.gameplate.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by luke on 1/1/17.
 */
public class CapturableObjectiveBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        for (Element capturablesElement : match.getDocument().getRootElement().getChildren("capturables")) {
            for (Element element : capturablesElement.getChildren()) {
                if (element.getName().equalsIgnoreCase("capturable")) {
                    String id = element.getAttributeValue("id");
                    String name = element.getAttributeValue("name");


                    Material material;
                    byte data = 1;
                    if (element.getAttributeValue("wool") != null) {
                        material = Material.WOOL;
                        data = ColorUtil.parseDyeColor(element.getAttributeValue("wool")).getWoolData();
                    } else {
                        material = Material.valueOf(Strings.getTechnicalName(element.getAttributeValue("material")));
                        if (element.getAttributeValue("data") != null) {
                            data = (byte) Numbers.parseInt(element.getAttributeValue("data"));
                        }
                    }

                    List<RegionModule> regions = RegionModuleBuilder.parseChildRegions(element);

                    results.add(new CapturableObjective(id, name, false, material, data, regions));
                }
            }
        }

        return results;
    }
}
