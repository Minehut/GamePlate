package com.minehut.gameplate.module.modules.portal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;

/**
 * Created by lucascosolo on 12/24/16.
 */
public class PortalModuleBuilder extends ModuleBuilder {


    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("portals")) {
            ModuleCollection<Module> collection = new ModuleCollection<>();
            match.getJson().get("portals").getAsJsonArray().forEach(element -> {
                JsonObject object = element.getAsJsonObject();
                String coords = object.get("coords").getAsString();
                String[] split = coords.replace(" ", "").split(",");
                double x, y, z;
                try {
                    x = Double.parseDouble(split[0]);
                    y = Double.parseDouble(split[1]);
                    z = Double.parseDouble(split[2]);
                } catch (NumberFormatException ex) {
                    throw new JsonParseException("Error on \"" + coords + "\"! Format should be: \"x, y, z\"");
                }
                collection.add(new PortalModule(RegionModuleBuilder.parseRegion(object, "region"), x, y, z));
            });
            return collection;
        }

        return null;

    }

}
