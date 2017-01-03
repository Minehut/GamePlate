package com.minehut.gameplate.module.modules.portal;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Bukkit;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by lucascosolo on 12/24/16.
 */
public class PortalModuleBuilder extends ModuleBuilder {


    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        for (Element portalsElement : match.getDocument().getRootElement().getChildren("portals")) {
            for (Element element : portalsElement.getChildren("portal")) {
                List<RegionModule> regions = RegionModuleBuilder.parseChildRegions(element);
                RegionModule destination = RegionModule.getRegionById(element.getAttributeValue("destination"));

                float yaw = 0;
                if (element.getAttributeValue("yaw") != null) {
                    yaw = (float) Numbers.parseDouble(element.getAttributeValue("yaw"));
                }

                float pitch = 0;
                if (element.getAttributeValue("pitch") != null) {
                    pitch = (float) Numbers.parseDouble(element.getAttributeValue("pitch"));
                }

                boolean sound = true;
                if (element.getAttributeValue("sound") != null) {
                    sound = Boolean.valueOf(element.getAttributeValue("sound"));
                }

                results.add(new PortalModule(regions, destination, yaw, pitch, sound));
            }
        }

        return results;

    }

}
