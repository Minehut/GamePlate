package com.minehut.gameplate.module.modules.kit;

import com.minehut.gameplate.module.Module;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitModule extends Module {

    private String id;
    private List<KitItem> items;

    private List<KitModule> parentKits = new ArrayList<>();

    public KitModule(String id, List<KitItem> items) {
        this.id = id;
        this.items = items;
    }

    public void apply(Player player) {
        for (KitModule parent : parentKits) {
            parent.apply(player);
        }

        items.forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));
    }

    public String getId() {
        return id;
    }

    public void addParentKit(KitModule kit) {
        this.parentKits.add(kit);
    }

    public List<KitModule> getParentKits() { return parentKits; }

}
