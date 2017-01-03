package com.minehut.gameplate.module.modules.objectives.destroyable;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.objectives.capturable.CapturableObjective;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.util.ColorUtil;
import com.minehut.gameplate.util.Numbers;
import com.minehut.gameplate.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by luke on 1/1/17.
 */
public class DestroyableObjectiveBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        for (Element capturablesElement : match.getDocument().getRootElement().getChildren("destroyables")) {
            for (Element element : capturablesElement.getChildren()) {
                if (element.getName().equalsIgnoreCase("destroyable")) {
                    String id = element.getAttributeValue("id");
                    String name = element.getAttributeValue("name");

                    Material material;
                    byte data = 0;
                    if (element.getAttributeValue("wool") != null) {
                        material = Material.WOOL;
                        data = ColorUtil.parseDyeColor(element.getAttributeValue("wool")).getWoolData();
                    } else {
                        material = Material.valueOf(Strings.getTechnicalName(element.getAttributeValue("material")));
                        if (element.getAttributeValue("data") != null) {
                            data = (byte) Numbers.parseInt(element.getAttributeValue("data"));
                        }
                    }

                    int health = Numbers.parseInt(element.getAttributeValue("health"));

                    boolean damageSound = true;
                    if (element.getAttribute("damageSound") != null) {
                        damageSound = Boolean.valueOf(element.getAttributeValue("damageSound"));
                    }

                    boolean completeSound = true;
                    if (element.getAttribute("completeSound") != null) {
                        completeSound = Boolean.valueOf(element.getAttributeValue("completeSound"));
                    }

                    List<RegionModule> regions = RegionModuleBuilder.parseChildRegions(element);

                    results.add(new DestroyableObjective(id, name, false, material, data, regions, health, damageSound, completeSound));
                }
            }
        }

        return results;
    }
}
