package com.minehut.gameplate.module.modules.filter.filterComparator.comparators.logic;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;

import static com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse.ALLOW;

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

        FilterResponse response = child.evaluate(objects);
        return response;
    }
}
