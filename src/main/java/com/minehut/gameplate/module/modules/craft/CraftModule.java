package com.minehut.gameplate.module.modules.craft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/20/2016.
 */
public class CraftModule extends Module {

    private List<ItemInfo> blocked = new ArrayList<>();

    public CraftModule(JsonArray blocked) {
        for (JsonElement element : blocked) {
            Material material;
            byte data = -1;
            String string = element.getAsString();
            if (string.contains(":")) {
                String[] split = string.split(":");
                material = Material.getMaterial(split[0]);
                try {
                    data = Byte.parseByte(split[1]);
                } catch (NumberFormatException ex) {
                    continue;
                }
            } else {
                material = Material.getMaterial(string);
            }
            if (material == null) continue;
            this.blocked.add(new ItemInfo(material, data));
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if (result == null) return;
        for (ItemInfo info : blocked) {
            if (info.material.equals(result.getType()) && info.dataEqual(result.getData().getData())) {
                event.setCancelled(true);
                ChatUtil.sendWarningMessage((Player) event.getWhoClicked(), ChatConstant.ERROR_BLOCKED_CRAFT, ChatColor.AQUA.toString() + info.material.name() + ChatColor.GRAY.toString());
            }
        }
    }

    class ItemInfo {

        final Material material;
        final byte data;

        ItemInfo(Material material, byte data) {
            this.material = material;
            this.data = data;
        }

        boolean dataEqual(byte data) {
            return this.data == data || this.data == -1;
        }

    }

}
