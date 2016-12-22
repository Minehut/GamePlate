package com.minehut.gameplate.module.modules.kit;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitModule {

    private Map<UUID, Inventory> kitMenus = new HashMap<>();

    private List<Kit> kits;

    public KitModule(List<Kit> kits) {
        this.kits = kits;
    }

    

}
