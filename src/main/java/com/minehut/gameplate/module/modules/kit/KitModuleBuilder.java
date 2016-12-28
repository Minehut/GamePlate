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
import org.jdom2.DataConversionException;
import org.jdom2.Element;

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

        for (Element kitsElement : match.getDocument().getRootElement().getChildren("kits")) {
            Map<Kit, List<String>> parents = new HashMap<>();
            Map<TeamModule, Kit> kits = new HashMap<>();
            for (Element kitElement : kitsElement.getChildren("kit")) {
                Kit kit = parseKit(kitElement);
                if (kitElement.getChild("parent") != null) {
                    List<String> p = new ArrayList<>();
                    for (Element parentElement : kitElement.getChildren("parent")) {
                        p.add(parentElement.getValue());
                    }
                    parents.put(kit, p);
                }
                TeamModule team = null;
                if (kitElement.getAttribute("team") != null) {
                    String t = kitElement.getAttributeValue("team");
                    team = TeamManager.getTeamById(t);
                    if (team == null) {
                        team = TeamManager.getTeamByName(t);
                    }
                }
                if (team == null) continue;
                kits.put(team, kit);
            }
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

    public static Kit parseKit(Element element) {
        String name = element.getAttributeValue("name");
        List<KitItem> kitItems = new ArrayList<>();
        for (Element itemElement : element.getChildren("item")) {
            String type = itemElement.getAttributeValue("type");
            if (type == null)
                type = "item";
            String mat = itemElement.getAttributeValue("material");
            Material material = Material.getMaterial(mat);
            if (material == null) continue;
            int amount = 1;
            if (itemElement.getAttribute("amount") != null) {
                try {
                    amount = itemElement.getAttribute("amount").getIntValue();
                } catch (DataConversionException ex) {
                    continue;
                }
            }
            int slot = 0;
            switch (type.toLowerCase()) {
                case "item":
                    try {
                        slot = itemElement.getAttribute("slot").getIntValue();
                    } catch (DataConversionException | NullPointerException ex) {
                        continue;
                    }
                    break;
                case "armor":
                    if (material.name().contains("_BOOTS")) {
                        slot = 100;
                    } else if (material.name().contains("_LEGGINGS")) {
                        slot = 101;
                    } else if (material.name().contains("_CHESTPLATE")) {
                        slot = 102;
                    } else if (material.name().contains("_HELMET")) {
                        slot = 103;
                    }
                    break;
                case "offHand":
                    slot = -106;
                    break;
                default: continue;
            }
            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            if (itemElement.getAttribute("displayName") != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemElement.getAttributeValue("displayName")));
            }
            if (itemElement.getChild("lore") != null) {
                List<String> lore = new ArrayList<>();
                for (Element loreElement : itemElement.getChildren("lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', loreElement.getValue()));
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            List<Element> enchants = new ArrayList<>();
            enchants.addAll(itemElement.getChildren("enchant"));
            enchants.addAll(itemElement.getChildren("enchantment"));
            for (Element enchantElement : enchants) {
                Enchantment enchantment = Enchantment.getByName(enchantElement.getAttributeValue("id"));
                if (enchantment == null) continue;
                int level = 1;
                if (enchantElement.getAttribute("level") != null) {
                    try {
                        level = enchantElement.getAttribute("level").getIntValue();
                    } catch (DataConversionException ex) {
                        continue;
                    }
                }
                item.addEnchantment(enchantment, level);
            }
            kitItems.add(new KitItem(slot, item));
        }
        return new Kit(name, kitItems);
    }

}
