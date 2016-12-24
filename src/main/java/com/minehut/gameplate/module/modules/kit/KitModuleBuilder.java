package com.minehut.gameplate.module.modules.kit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {

        if (match.getJson().has("kits")) {
            Map<Kit, List<String>> parents = new HashMap<>();
            JsonArray kitArray = match.getJson().get("kits").getAsJsonArray();
            List<Kit> kits = new ArrayList<>();
            kitArray.forEach(element -> {
                JsonObject object = element.getAsJsonObject();
                Kit kit = Kit.fromJson(object);
                if (object.has("parents")) {
                    List<String> p = new ArrayList<>();
                    object.get("parents").getAsJsonArray().forEach(parent -> {
                        p.add(parent.getAsString());
                    });
                    parents.put(kit, p);
                }
                kits.add(kit);
            });
            KitModule module = new KitModule(kits);
            parents.forEach((kit, ps) -> ps.forEach(p -> {
                Kit parent = module.getKit(p);
                if (parent != null) {
                    kit.addParentKit(parent);
                }
            }));
            return new ModuleCollection<>(module);
        }

        return null;
    }
}
