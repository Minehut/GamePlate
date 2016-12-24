package com.minehut.gameplate.module.modules.kit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.module.modules.team.TeamModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lucas on 12/21/2016.
 */
public class Kit {

    private String name;
    private int slot, price;
    private String description;
    private Material iconMaterial;

    private List<KitItem> items;

    private List<Kit> parentKits;

    public Kit(String name, int slot, int price, String description, Material iconMaterial, List<KitItem> items) {
        this.name = name;
        this.slot = slot;
        this.price = price;
        this.description = description;
        this.iconMaterial = iconMaterial;
        this.items = items;
    }

    public void apply(Player player) {
        parentKits.forEach(parent -> parent.apply(player));
        items.forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Material getIconMaterial() {
        return iconMaterial;
    }

    public void addParentKit(Kit kit) {
        this.parentKits.add(kit);
    }

    public List<Kit> getParentKits() { return parentKits; }

    public ItemStack getIcon(Player player) {
        ItemStack item = new ItemStack(this.iconMaterial);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(new LocalizedChatMessage(ChatConstant.UI_KIT_ICON, this.name).getMessage(player.spigot().getLocale()));
        meta.setLore(Arrays.asList("", ChatColor.YELLOW + description, ""));
        item.setItemMeta(meta);
        return item;
    }

    public static Kit fromJson(JsonObject object) {
        String name = object.get("name").getAsString();
        int kitSlot = -1;
        if (object.has("slot")) {
            kitSlot = object.get("slot").getAsInt();
        }
        int price = 0;
        if (object.has("price")) {
            price = object.get("price").getAsInt();
        }
        String description = "";
        if (object.has("description")) {
            object.get("description").getAsString();
        }
        Material iconMaterial;
        if (object.has("material")) {
            iconMaterial = Material.getMaterial(object.get("material").getAsString());
        } else if (GamePlate.getInstance().getGameHandler().getMatch().getJson().has("defaultKitMaterial")) {
            iconMaterial = Material.getMaterial(GamePlate.getInstance().getGameHandler().getMatch().getJson().get("defaultKitMaterial").getAsString());
        }
        iconMaterial = Material.CLAY;
        List<KitItem> kitItems = new ArrayList<>();
        JsonArray array = object.get("items").getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();
            String type = obj.get("type").getAsString();
            String mat = obj.get("material").getAsString();
            mat = mat.replace(" ", "_").toUpperCase();
            Material material = Material.getMaterial(mat);
            if (material == null) continue;
            int amount = 1;
            if (obj.has("amount")) {
                amount = obj.get("amount").getAsInt();
            }
            int slot = 0;

            /*
             * We might want to do checks in the apply method instead of doing it like this, but
             * I looked up slot ids for armor and found these, and they were easier and seemed cleaner
             */
            switch (type) {
                case "item":
                    slot = obj.get("slot").getAsInt();
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
            if (obj.has("displayName")) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', obj.get("displayName").getAsString()));
            }
            if (obj.has("lore")) {
                List<String> lore = new ArrayList<>();
                for (JsonElement e : obj.get("lore").getAsJsonArray()) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', e.getAsString()));
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            JsonArray enchants = null;
            if (obj.has("enchants")) {
                enchants = obj.get("enchants").getAsJsonArray();
            } else if (obj.has("enchantments")) {
                enchants = obj.get("enchantments").getAsJsonArray();
            }
            if (enchants != null) {
                for (JsonElement e : enchants) {
                    JsonObject ench = e.getAsJsonObject();
                    Enchantment enchantment = Enchantment.getByName(ench.get("id").getAsString());
                    if (enchantment == null) continue;
                    int level = 1;
                    if (ench.has("level")) {
                        level = ench.get("level").getAsInt();
                    }
                    item.addEnchantment(enchantment, level);
                }
            }
            kitItems.add(new KitItem(slot, item));
        }
        return new Kit(name, kitSlot, price, description, iconMaterial, kitItems);
    }

}
