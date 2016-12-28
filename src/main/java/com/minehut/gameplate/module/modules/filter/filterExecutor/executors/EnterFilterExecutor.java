package com.minehut.gameplate.module.modules.filter.filterExecutor.executors;

import com.minehut.gameplate.module.modules.filter.filterComparator.FilterComparator;
import com.minehut.gameplate.module.modules.filter.filterComparator.FilterResponse;
import com.minehut.gameplate.module.modules.filter.filterExecutor.FilterExecutor;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

/**
 * Created by luke on 12/27/16.
 */
public class EnterFilterExecutor extends FilterExecutor {
    protected String message;

    public EnterFilterExecutor(List<RegionModule> regions, FilterComparator comparator, String message) {
        super(regions, comparator);
        this.message = message;
    }

    @EventHandler
    public void onEnter(PlayerMoveEvent event) {
        for (RegionModule regionModule : super.regions) {
            if (regionModule.contains(event.getTo().toVector())) {
                if (comparator.evaluate(event.getPlayer()) == FilterResponse.DENY) {
                    event.setCancelled(true);
                    ChatUtil.sendWarningMessage(event.getPlayer(), message);
                }
            }
        }
    }
}
