package com.minehut.gameplate.module.modules.events.types;

import com.minehut.gameplate.module.modules.events.EventsModule;
import com.minehut.gameplate.util.Effects;
import com.minehut.gameplate.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jdom2.Element;

/**
 * Created by MatrixTunnel on 1/5/2017.
 */
public class ClickEvent extends EventsModule {

    private final Element element;

    public ClickEvent(Element element) {
        this.element = element;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Bukkit.broadcastMessage("Event fired!");
        Action action = Action.valueOf(element.getAttributeValue("action").toUpperCase().replace(" ", "_"));

        if (element.getAttribute("action") != null && event.getAction() == action) {

            for (Element test : element.getChildren()) {
                Bukkit.broadcastMessage(action.toString());

                if (test.getName().equalsIgnoreCase("time")) {
                    EventsModule.parseTime(test);
                } else if (test.getName().equalsIgnoreCase("item")) {
                    event.getPlayer().getInventory().addItem(Items.parseItemstack(test));
                } else if (test.getName().equalsIgnoreCase("potion")) {
                    event.getPlayer().addPotionEffect(Effects.parsePotionEffect(test));
                } else if (test.getName().equalsIgnoreCase("message")) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('*', test.getChild("message").getValue()));
                }

            }

        }

    }

}
