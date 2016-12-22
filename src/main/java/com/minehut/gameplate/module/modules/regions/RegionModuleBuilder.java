package com.minehut.gameplate.module.modules.regions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.regions.types.BlockRegion;
import com.minehut.gameplate.module.modules.regions.types.CuboidRegion;
import com.minehut.gameplate.module.modules.regions.types.CylinderRegion;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.util.logging.Level;

/**
 * Created by luke on 12/22/16.
 */
@BuilderData(load = ModuleLoadTime.EARLIEST)
public class RegionModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        if (match.getJson().has("regions")) {
            for (JsonElement e : match.getJson().getAsJsonArray("regions")) {
                JsonObject jsonObject = e.getAsJsonObject();
                RegionModule regionModule = parseRegion(jsonObject);
                if (regionModule != null) {
                    results.add(regionModule);
                }
            }
        }

        return results;
    }

    /*
     * Parse a region from outside of the "regions" section of the map file.
     */
    public static RegionModule parseRegion(JsonObject containerObject, String key) {
        if (!containerObject.has("key")) {
            Bukkit.getLogger().log(Level.SEVERE, "Error parsing region: key not found '" + key + "'");
            return null;
        }

        if (containerObject.get("key").isJsonPrimitive()) { //referencing a global region
            return RegionModule.getRegionById(containerObject.get("key").getAsString());
        } else {
            parseRegion(containerObject.get("key").getAsJsonObject());
        }
        return null;
    }

    public static RegionModule parseRegion(JsonObject jsonObject) {
        if (jsonObject.has("type")) {
            String type = jsonObject.get("type").getAsString();

            if (type.equalsIgnoreCase("cuboid")) {
                return parseCuboidRegion(jsonObject);
            } else if (type.equalsIgnoreCase("cylinder")) {
                return parseCylinderRegion(jsonObject);
            } else if (type.equalsIgnoreCase("block")) {
                return parseBlockRegion(jsonObject);
            }

        } else {
            return parseCuboidRegion(jsonObject);
        }

        return null;
    }

    private static RegionModule parseCuboidRegion(JsonObject jsonObject) {
        Vector pos1 = Numbers.parseVector(jsonObject.get("pos1").getAsString());
        Vector pos2 = Numbers.parseVector(jsonObject.get("pos2").getAsString());

        String id = null;
        if (jsonObject.has("id")) {
            id = jsonObject.get("id").getAsString();
        }

        return new CuboidRegion(id, pos1, pos2);
    }

    private static RegionModule parseCylinderRegion(JsonObject jsonObject) {
        Vector base = Numbers.parseVector(jsonObject.get("base").getAsString());
        double radius = jsonObject.get("radius").getAsDouble();
        double height = jsonObject.get("height").getAsDouble();

        String id = null;
        if (jsonObject.has("id")) {
            id = jsonObject.get("id").getAsString();
        }

        return new CylinderRegion(id, base, radius, height);
    }

    private static RegionModule parseBlockRegion(JsonObject jsonObject) {
        Vector location = Numbers.parseVector(jsonObject.get("location").getAsString());

        String id = null;
        if (jsonObject.has("id")) {
            id = jsonObject.get("id").getAsString();
        }

        return new BlockRegion(id, location);
    }
}
