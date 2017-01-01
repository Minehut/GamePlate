package com.minehut.gameplate.module.modules.kit;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.*;
import com.minehut.gameplate.module.modules.kit.types.KitInventoryItem;
import com.minehut.gameplate.module.modules.kit.types.KitPotionItem;
import com.minehut.gameplate.util.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

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

    private static ItemStack parseItemstack(Element element) {
        Material material = Material.valueOf(element.getAttributeValue("material").toUpperCase().replace(" ", "_"));

        int amount = 1;
        if (element.getAttributeValue("amount") != null) {
            amount = Numbers.parseInt(element.getAttributeValue("amount"));
        }

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (element.getAttribute("name") != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', element.getAttributeValue("name")));
        }
        if (element.getChild("lore") != null) {
            List<String> lore = new ArrayList<>();
            for (Element loreElement : element.getChildren("lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', loreElement.getValue()));
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        List<Element> enchants = new ArrayList<>();
        enchants.addAll(element.getChildren("enchantment"));
        for (Element enchantElement : enchants) {
            Enchantment enchantment = Enchantment.getByName(enchantElement.getAttributeValue("id"));
            if (enchantment == null) continue;
            int level = 1;
            if (enchantElement.getAttributeValue("level") != null) {
                level = Numbers.parseInt(enchantElement.getAttributeValue("level"));
            }
            item.addEnchantment(enchantment, level);
        }

        return item;
    }

    public static PotionEffect parsePotionEffect(Element element) {
        PotionEffectType effectType = PotionEffectType.getByName(element.getAttributeValue("id").toUpperCase().replace(" ", "_"));
        int level = 0;
        if (element.getAttribute("level") != null) {
            level = Numbers.parseInt(element.getAttributeValue("level"));
        }
        int amplifier = 0;
        if (element.getAttributeValue("amplifier") != null) {
            amplifier = Numbers.parseInt(element.getAttributeValue("amplifier"));
        }
        return new PotionEffect(effectType, level, amplifier);
    }

    public static KitModule parseKit(Element kitElement) {
        String id = kitElement.getAttributeValue("id");
        List<KitItem> kitItems = new ArrayList<>();

        for (Element element : kitElement.getChildren()) {
            if (element.getName().equalsIgnoreCase("item")) {

                ItemStack itemStack = parseItemstack(element);
                int slot = Numbers.parseInt(element.getAttributeValue("slot"));

                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("helmet")) {
                ItemStack itemStack = parseItemstack(element);
                int slot = 103;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("chestplate")) {
                ItemStack itemStack = parseItemstack(element);
                int slot = 102;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("leggings")) {
                ItemStack itemStack = parseItemstack(element);
                int slot = 101;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("boots")) {
                ItemStack itemStack = parseItemstack(element);
                int slot = 100;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("offhand")) {
                ItemStack itemStack = parseItemstack(element);
                int slot = -2;
                kitItems.add(new KitInventoryItem(slot, itemStack));
            }
            else if (element.getName().equalsIgnoreCase("potion")) {
                PotionEffect potionEffect = parsePotionEffect(element);

                int slot = -1;
                if (element.getAttributeValue("slot") != null) {
                    slot = Numbers.parseInt(element.getAttributeValue("slot"));
                }

                int amount = 1;
                if (element.getAttributeValue("amount") != null) {
                    amount = Numbers.parseInt(element.getAttributeValue("amount"));
                }

                kitItems.add(new KitPotionItem(potionEffect, slot, amount));
            }
        }

        return new KitModule(id, kitItems);
    }

}
