package com.minehut.gameplate.module.modules;

import org.bukkit.Material;

public class ItemInfo {

        public final Material material;
        public final byte data;

        public ItemInfo(Material material, byte data) {
            this.material = material;
            this.data = data;
        }

        public boolean dataEqual(byte data) {
            return this.data == data || this.data == -1;
        }

}