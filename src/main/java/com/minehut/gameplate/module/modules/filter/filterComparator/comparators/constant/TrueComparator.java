package com.minehut.gameplate.module.modules.filter.filterComparator.comparators.constant;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;

/**
 * Created by luke on 12/27/16.
 */
public class TrueComparator extends FilterComparator {

    @Override
    public FilterResponse evaluate(Object... objects) {
        return FilterResponse.ALLOW;
    }
}
