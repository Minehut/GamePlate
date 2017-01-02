package com.minehut.gameplate.module.modules.regions;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.regions.types.BlockRegion;
import com.minehut.gameplate.module.modules.regions.types.CuboidRegion;
import com.minehut.gameplate.module.modules.regions.types.CylinderRegion;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by luke on 12/22/16.
 */
@BuilderData(load = ModuleLoadTime.EARLIEST)
public class RegionModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        RegionModule globalRegion = new CuboidRegion("global",
                new Vector(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE),
                new Vector(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
        results.add(globalRegion);

        for (Element element1 : match.getDocument().getRootElement().getChildren()) {
            parseRegion(element1);
            for (Element element2 : element1.getChildren()) {
                parseRegion(element2);
                for (Element element3 : element2.getChildren()) {
                    parseRegion(element3);
                    for (Element element4 : element3.getChildren()) {
                        parseRegion(element4);
                    }
                }
            }
        }

        return results;
    }

    public static List<RegionModule> parseChildRegions(Element element) {
        List<RegionModule> regions = new ArrayList<>();

        for (Element child : element.getChildren()) {
            RegionModule regionModule = parseRegion(child);
            if (regionModule != null) {
                regions.add(regionModule);
            }
        }
        return regions;
    }

    public static RegionModule parseRegion(Element element) {
        RegionModule regionModule = null;
        boolean reference = false;

        switch (element.getName().toLowerCase()) {
            case "block":
                regionModule = parseBlockRegion(element);
                break;
            case "cuboid":
                regionModule = parseCuboidRegion(element);
                break;
            case "cylinder":
                regionModule = parseCylinderRegion(element);
                break;
            case "region":
                regionModule = getGlobalRegion(element.getAttributeValue("id"));
                reference = true;
                break;

        }

        if (regionModule != null && regionModule.getId() != null && !reference) {
            if (getGlobalRegion(regionModule.getId()) == null) {
                GameHandler.getGameHandler().getMatch().getModules().add(regionModule);
                Bukkit.getLogger().log(Level.INFO, "Added global region '" + regionModule.getId() + "'");
            }
        }

        return regionModule;
    }

    public static RegionModule getGlobalRegion(String id) {
        for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
            if (regionModule.getId() != null && regionModule.getId().equalsIgnoreCase(id)) {
                return regionModule;
            }
        }
        return null;
    }

    private static RegionModule parseCuboidRegion(Element element) {
        Vector pos1 = Numbers.parseVector(element.getAttributeValue("min"));
        Vector pos2 = Numbers.parseVector(element.getAttributeValue("max"));

        String id = null;
        if (element.getAttributeValue("id") != null) {
            id = element.getAttributeValue("id");
        }

        return new CuboidRegion(id, pos1, pos2);
    }

    private static RegionModule parseCylinderRegion(Element element) {
        Vector base = Numbers.parseVector(element.getAttributeValue("base"));
        double radius = Numbers.parseDouble(element.getAttributeValue("radius"));
        double height = Numbers.parseDouble(element.getAttributeValue("height"));

        String id = null;
        if (element.getAttributeValue("id") != null) {
            id = element.getAttributeValue("id");
        }

        return new CylinderRegion(id, base, radius, height);
    }

    private static RegionModule parseBlockRegion(Element element) {
        Vector location = Numbers.parseVector(element.getAttributeValue("location"));

        String id = null;
        if (element.getAttributeValue("id") != null) {
            id = element.getAttributeValue("id");
        }

        return new BlockRegion(id, location);
    }
}
