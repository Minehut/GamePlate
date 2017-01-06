package com.minehut.gameplate.module.modules.events.types;

import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.modules.events.EventsModule;
import com.minehut.gameplate.util.Effects;
import com.minehut.gameplate.util.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jdom2.Element;

/**
 * Created by MatrixTunnel on 1/5/2017.
 */
public class KillEvent extends EventsModule {

    private final Element element;

    public KillEvent(Element element) {
        this.element = element;
    } //TODO Add cause attribute to tell what killed the player

    @EventHandler
    public void onPlayerDeath(GameDeathEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {

            for (Element test : element.getChildren()) {
                if (test.getName().equalsIgnoreCase("time")) {
                    EventsModule.parseTime(test);
                } else if (test.getName().equalsIgnoreCase("item")) {
                    event.getKiller().getPlayer().getInventory().addItem(Items.parseItemstack(test));
                } else if (test.getName().equalsIgnoreCase("potion")) {
                    event.getKiller().getPlayer().addPotionEffect(Effects.parsePotionEffect(test));
                }
            }
        }
    }

}
