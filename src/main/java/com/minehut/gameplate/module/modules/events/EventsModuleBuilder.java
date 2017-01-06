package com.minehut.gameplate.module.modules.events;

import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.ModuleBuilder;
import com.minehut.gameplate.module.ModuleCollection;
import com.minehut.gameplate.module.modules.events.types.ClickEvent;
import com.minehut.gameplate.module.modules.events.types.KillEvent;
import org.bukkit.Bukkit;
import org.jdom2.Element;

/**
 * Created by MatrixTunnel on 1/4/2017.
 */
public class EventsModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        ModuleCollection<EventsModule> events = new ModuleCollection<>();

        for (Element filtersElement : match.getDocument().getRootElement().getChildren("events")) {
            for (Element filter : filtersElement.getChildren()) {
                Bukkit.broadcastMessage("loop");

                if (filter.getName().toLowerCase().equals("kill")) {
                    Bukkit.broadcastMessage("kill");
                    events.add(new KillEvent(filter));
                } else if (filter.getName().toLowerCase().equals("click")) {
                    Bukkit.broadcastMessage("click");
                    events.add(new ClickEvent(filter));
                }
            }

            return events;
        }

        return null;
    }
}
