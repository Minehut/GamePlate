package com.minehut.gameplate.module.modules.filter;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.comparators.TeamComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.comparators.constant.TrueComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.comparators.logic.AllowComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.comparators.logic.DenyComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.comparators.logic.OnlyComparator;
import com.minehut.gameplate.module.modules.filter.filterExecutor.executors.BlockBreakFilterExecutor;
import com.minehut.gameplate.module.modules.filter.filterExecutor.executors.BlockPlaceFilterExecutor;
import com.minehut.gameplate.module.modules.filter.filterExecutor.executors.EnterFilterExecutor;
import com.minehut.gameplate.module.modules.regions.RegionModuleBuilder;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.jdom2.Element;

import java.util.Arrays;

/**
 * Created by luke on 12/27/16.
 */
public class FilterModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection results = new ModuleCollection();

        for (Element filtersElement : match.getDocument().getRootElement().getChildren("filters")) {
            for (Element filter : filtersElement.getChildren()) {
                if (filter.getName().toLowerCase().equals("filter")) {
                    if (filter.getAttributeValue("enter") != null) {
                        results.add(new EnterFilterExecutor(RegionModuleBuilder.parseChildRegions(filter), getComparator(filter.getAttributeValue("enter")), filter.getAttributeValue("message")));
                    } else if (filter.getAttributeValue("modify") != null) {
                        results.add(new BlockBreakFilterExecutor(RegionModuleBuilder.parseChildRegions(filter), getComparator(filter.getAttributeValue("modify")), filter.getAttributeValue("message")));
                        results.add(new BlockPlaceFilterExecutor(RegionModuleBuilder.parseChildRegions(filter), getComparator(filter.getAttributeValue("modify")), filter.getAttributeValue("message")));
                    } else if (filter.getAttributeValue("break") != null) {
                        results.add(new BlockBreakFilterExecutor(RegionModuleBuilder.parseChildRegions(filter), getComparator(filter.getAttributeValue("modify")), filter.getAttributeValue("message")));
                    } else if (filter.getAttributeValue("place") != null) {
                        results.add(new BlockPlaceFilterExecutor(RegionModuleBuilder.parseChildRegions(filter), getComparator(filter.getAttributeValue("modify")), filter.getAttributeValue("message")));
                    }

                }
            }
        }

        return results;
    }

    private static FilterComparator getComparator(String value) {
        String split[] = value.toLowerCase().split("-");

        FilterComparator filterComparator = null;

        if (split.length >= 2) {
            if (split[1].equals("all")) {
                filterComparator = new TrueComparator();
            } else {
                for (TeamModule teamModule : TeamManager.getTeamModules()) {
                    if (split[1].equals(teamModule.getId())) {
                        filterComparator = new TeamComparator(teamModule);
                    }
                }
            }

        }

        FilterComparator parentComparator = null;

        switch (split[0]) {
            case "only":
                parentComparator = new OnlyComparator(Arrays.asList(filterComparator));
                break;
            case "deny":
                parentComparator = new DenyComparator(filterComparator);
                break;
            case "allow":
                parentComparator = new AllowComparator(filterComparator);
                break;
        }

        return parentComparator;
    }
}
