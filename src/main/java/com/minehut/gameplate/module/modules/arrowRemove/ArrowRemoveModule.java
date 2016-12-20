package com.minehut.gameplate.module.modules.arrowRemove;

import com.minehut.gameplate.module.Module;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Created by Lucas on 12/20/2016.
 */
public class ArrowRemoveModule extends Module {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            event.getEntity().remove();
        }
    }

}
