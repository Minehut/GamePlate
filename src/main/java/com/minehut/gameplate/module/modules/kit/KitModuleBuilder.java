package com.minehut.gameplate.module.modules.kit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        return null;
    }

//    @Override
//    public ModuleCollection<? extends Module> load(Match match) {
//
//        if (match.getJson().has("kits")) {
//            Map<Kit, List<String>> parents = new HashMap<>();
//            JsonArray kitArray = match.getJson().get("kits").getAsJsonArray();
//            Map<TeamModule, Kit> kits = new HashMap<>();
//            kitArray.forEach(element -> {
//                JsonObject object = element.getAsJsonObject();
//                Kit kit = parseKit(object);
//                if (object.has("parents")) {
//                    List<String> p = new ArrayList<>();
//                    object.get("parents").getAsJsonArray().forEach(parent -> {
//                        p.add(parent.getAsString());
//                    });
//                    parents.put(kit, p);
//                }
//                TeamModule team = TeamManager.getTeamById(object.get("id").getAsString());
//                if (team != null) {
//                    kits.put(team, kit);
//                }
//            });
//            KitModule module = new KitModule(kits);
//            parents.forEach((kit, ps) -> ps.forEach(p -> {
//                Kit parent = module.getKit(p);
//                if (parent != null) {
//                    kit.addParentKit(parent);
//                }
//            }));
//            return new ModuleCollection<>(module);
//        }
//
//        return null;
//    }
//
//    public static Kit parseKit(JsonObject object) {
//        String name = object.get("name").getAsString();
//        List<KitItem> kitItems = new ArrayList<>();
//        JsonArray array = object.get("items").getAsJsonArray();
//        for (JsonElement element : array) {
//            JsonObject obj = element.getAsJsonObject();
//            String type = obj.get("type").getAsString();
//            String mat = obj.get("material").getAsString();
//            mat = mat.replace(" ", "_").toUpperCase();
//            Material material = Material.getMaterial(mat);
//            if (material == null) continue;
//            int amount = 1;
//            if (obj.has("amount")) {
//                amount = obj.get("amount").getAsInt();
//            }
//            int slot = 0;
//
//            /*
//             * We might want to do checks in the apply method instead of doing it like this, but
//             * I looked up slot ids for armor and found these, and they were easier and seemed cleaner
//             */
//            switch (type) {
//                case "item":
//                    slot = obj.get("slot").getAsInt();
//                    break;
//                case "armor":
//                    if (material.name().contains("_BOOTS")) {
//                        slot = 100;
//                    } else if (material.name().contains("_LEGGINGS")) {
//                        slot = 101;
//                    } else if (material.name().contains("_CHESTPLATE")) {
//                        slot = 102;
//                    } else if (material.name().contains("_HELMET")) {
//                        slot = 103;
//                    }
//                    break;
//                case "offHand":
//                    slot = -106;
//                    break;
//                default: continue;
//            }
//            ItemStack item = new ItemStack(material, amount);
//            ItemMeta meta = item.getItemMeta();
//            if (obj.has("displayName")) {
//                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', obj.get("displayName").getAsString()));
//            }
//            if (obj.has("lore")) {
//                List<String> lore = new ArrayList<>();
//                for (JsonElement e : obj.get("lore").getAsJsonArray()) {
//                    lore.add(ChatColor.translateAlternateColorCodes('&', e.getAsString()));
//                }
//                meta.setLore(lore);
//            }
//            item.setItemMeta(meta);
//            JsonArray enchants = null;
//            if (obj.has("enchants")) {
//                enchants = obj.get("enchants").getAsJsonArray();
//            } else if (obj.has("enchantments")) {
//                enchants = obj.get("enchantments").getAsJsonArray();
//            }
//            if (enchants != null) {
//                for (JsonElement e : enchants) {
//                    JsonObject ench = e.getAsJsonObject();
//                    Enchantment enchantment = Enchantment.getByName(ench.get("id").getAsString());
//                    if (enchantment == null) continue;
//                    int level = 1;
//                    if (ench.has("level")) {
//                        level = ench.get("level").getAsInt();
//                    }
//                    item.addEnchantment(enchantment, level);
//                }
//            }
//            kitItems.add(new KitItem(slot, item));
//        }
//        return new Kit(name, kitItems);
//    }

}
