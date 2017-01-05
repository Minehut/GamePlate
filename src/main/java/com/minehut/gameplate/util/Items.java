package com.minehut.gameplate.util;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Wool;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class Items {

    public static ItemStack createItem(Material material, int amount, short data, String name) {
        return createItem(material, amount, data, name, null);
    }

    public static ItemStack createItem(Material material, int amount, short data, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBook(Material material, int amount, String name, String author) {
        ItemStack item = createItem(material, amount, (short) 0, name);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(author);
        meta.setPages(Collections.singletonList(""));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, int amount, String name, List<String> lore, Color color) {
        ItemStack item = createItem(material, amount, (short) 0, name, lore);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack toMaxDurability(ItemStack item) {
        ItemStack item2 = item.clone();
        item2.setDurability((short)0);
        return item2;
    }

    public static ItemStack parseItemstack(Element element) {
        Material material = Material.getMaterial(element.getAttributeValue("material").toUpperCase().replace(" ", "_"));

        int amount = 1;
        if (element.getAttributeValue("amount") != null) {
            amount = Numbers.parseInt(element.getAttributeValue("amount"));
        }

        ItemStack item;
        if (element.getAttributeValue("color") != null && material == Material.WOOL) {
            item = new ItemStack(material, amount, ColorUtil.parseDyeColor(element.getAttributeValue("color")).getWoolData());
        } else {
            item = new ItemStack(material, amount);
        }

        ItemMeta meta = item.getItemMeta();

        if (element.getAttribute("name") != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('*', element.getAttributeValue("name")));
        }
        if (element.getChild("lore") != null) {
            List<String> lore = new ArrayList<>();
            for (Element loreElement : element.getChildren("lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('*', loreElement.getValue()));
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        List<Element> enchants = new ArrayList<>();
        enchants.addAll(element.getChildren("enchantment"));
        for (Element enchantElement : enchants) {
            Enchantment enchantment = Enchantment.getByName(Strings.getTechnicalName(enchantElement.getAttributeValue("id")));
            int level = 1;
            if (enchantElement.getAttributeValue("level") != null) {
                level = Numbers.parseInt(enchantElement.getAttributeValue("level"));
            }
            item.addEnchantment(enchantment, level);
        }

        if (element.getAttributeValue("color") != null) {
            if (item.getType().toString().contains("LEATHER")) {
                LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();
                armorMeta.setColor(ColorUtil.convertHexToRGB(element.getAttributeValue("color")));
                item.setItemMeta(armorMeta);
            }

//            else if (item.getType().toString().contains("WOOL")) {
//                DyeColor color = ColorUtil.parseDyeColor(element.getAttributeValue("color").toUpperCase());
//                Bukkit.getLogger().log(Level.INFO, "Creating colored wool: " + color.toString());
//                Wool wool = (Wool) item.getData();
//                wool.setColor(color);
//                item.setData(wool);
//            }
        }

        return item;
    }

}
