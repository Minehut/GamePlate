package com.minehut.gameplate.module.modules.spawn;

import com.minehut.gameplate.module.modules.regions.RegionModule;

/**
 * Created by luke on 12/22/16.
 */
public class SpawnNode {
    private RegionModule region;

    public SpawnNode(RegionModule region) {
        this.region = region;
    }

    public RegionModule getRegion() {
        return region;
    }
}
