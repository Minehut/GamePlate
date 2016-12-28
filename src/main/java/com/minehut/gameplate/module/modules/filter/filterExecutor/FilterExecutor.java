package com.minehut.gameplate.module.modules.filter.filterExecutor;

import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.regions.RegionModule;

import java.util.List;

/**
 * Created by luke on 12/27/16.
 */
public class FilterExecutor extends Module {
    protected List<RegionModule> regions;
    protected FilterComparator comparator;

    public FilterExecutor(List<RegionModule> regions, FilterComparator comparator) {
        this.regions = regions;
        this.comparator = comparator;
    }
}
