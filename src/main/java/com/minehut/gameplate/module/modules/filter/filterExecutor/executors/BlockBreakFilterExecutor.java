package com.minehut.gameplate.module.modules.filter.filterExecutor.executors;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;
import com.minehut.gameplate.module.modules.filter.filterExecutor.FilterExecutor;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

/**
 * Created by luke on 12/27/16.
 */
public class BlockBreakFilterExecutor extends FilterExecutor {
    private String message;

    public BlockBreakFilterExecutor(List<RegionModule> regions, FilterComparator comparator, String message) {
        super(regions, comparator);
        this.message = message;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (RegionModule regionModule : super.regions) {
            if (regionModule.contains(event.getBlock().getLocation().toVector())) {
                if (comparator.evaluate(event.getPlayer()) == FilterResponse.DENY) {
                    event.setCancelled(true);
                    if (message != null) {
                        ChatUtil.sendWarningMessage(event.getPlayer(), message);
                    }
                }
            }
        }
    }
}
