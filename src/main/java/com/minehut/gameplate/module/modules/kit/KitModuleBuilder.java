package com.minehut.gameplate.module.modules.kit;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Numbers;
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
@BuilderData(load = ModuleLoadTime.EARLIER)
public class KitModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<KitModule> results = new ModuleCollection();

        for (Element kitsElement : match.getDocument().getRootElement().getChildren("kits")) {
            for (Element kitElement : kitsElement.getChildren()) {

                KitModule kit = parseKit(kitElement);

                if (kitElement.getAttributeValue("parents") != null) {
                    String split[] = kitElement.getAttributeValue("parents").replace(" ", "").split(",");
                    for (String parent : split) {
                        for (KitModule parentKit : results) {
                            if (parentKit.getId().equals(parent)) {
                                kit.addParentKit(parentKit);
                            }
                        }
                    }
                }

                results.add(kit);
            }
        }

        return results;
    }

    public static KitModule parseKit(Element element) {
        String id = element.getAttributeValue("id");

        List<KitItem> kitItems = new ArrayList<>();
        for (Element itemElement : element.getChildren()) {
            String type = itemElement.getName();

            String mat = itemElement.getAttributeValue("material");
            Material material = Material.getMaterial(mat.toUpperCase().replace(" ", "_"));

            int amount = 1;
            if (itemElement.getAttribute("amount") != null) {
                try {
                    amount = itemElement.getAttribute("amount").getIntValue();
                } catch (DataConversionException ex) {
                    continue;
                }
            }
            int slot = 0;
            if (itemElement.getAttributeValue("slot") != null) {
                slot = Numbers.parseInt(itemElement.getAttributeValue("slot"));
            } else {
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
                    default:
                        continue;
                }
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
        return new KitModule(id, kitItems);
    }

}
