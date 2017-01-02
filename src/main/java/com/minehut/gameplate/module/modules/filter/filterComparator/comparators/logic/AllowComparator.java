package com.minehut.gameplate.module.modules.filter.filterComparator.comparators.logic;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;

/**
 * Created by luke on 12/27/16.
 */
public class AllowComparator extends FilterComparator {
    private FilterComparator child;

    public AllowComparator(FilterComparator child) {
        this.child = child;
    }

    @Override
    public FilterResponse evaluate(Object... objects) {

        return child.evaluate(objects);
    }
}
