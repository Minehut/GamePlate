package com.minehut.gameplate.module.modules.blockPlaceModule;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.ItemInfo;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/20/2016.
 */
public class BlockPlaceModule extends Module {

    private List<ItemInfo> blocked = new ArrayList<>();

    /**
     * Makes a BlockPlaceModule from a list of items
     * @param blocked List of items in format "material_name" or "material_name:data"
     */
    public BlockPlaceModule(List<String> blocked) {
        for (String string : blocked) {
            Material material;
            byte data = -1;
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
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item == null) return;
        for (ItemInfo info : blocked) {
            if (info.material.equals(item.getType()) && info.dataEqual(item.getData().getData())) {
                event.setCancelled(true);
                ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.ERROR_BLOCKED_PLACE, ChatColor.AQUA.toString() + info.material.name() + ChatColor.GRAY.toString());
            }
        }
    }

}
