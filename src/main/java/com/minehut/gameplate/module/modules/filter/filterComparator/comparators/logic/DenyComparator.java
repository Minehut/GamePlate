package com.minehut.gameplate.module.modules.filter.filterComparator.comparators.logic;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;

/**
 * Created by luke on 12/27/16.
 */
public class DenyComparator extends FilterComparator {
    private FilterComparator child;

    public DenyComparator(FilterComparator child) {
        this.child = child;
    }

    @Override
    public FilterResponse evaluate(Object... objects) {

        FilterResponse response = child.evaluate(objects);

        if (response == FilterResponse.DENY) {
            return FilterResponse.ALLOW;
        } else if (response == FilterResponse.ALLOW) {
            return FilterResponse.DENY;
        }

        //abstain
        return response;
    }
}
