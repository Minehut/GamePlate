package com.minehut.gameplate.module.modules.motd;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.util.Config;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * Created by luke on 1/4/17.
 */
public class MotdModule extends Module {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        String name = GameHandler.getGameHandler().getMatch().getCurrentMap().getMap().getName();
        ChatColor color = ChatColor.GRAY;
        switch (GameHandler.getGameHandler().getMatch().getMatchState()) {
            case ENDED:
                color = ChatColor.AQUA;
                break;
            case PLAYING:
                color = ChatColor.GOLD;
                break;
            case STARTING:
                color = ChatColor.GREEN;
                break;
        }
        event.setMotd(color + "\u00BB " + ChatColor.AQUA + name + color + " \u00AB" +
                (!Config.motd.equals("") ? "\n" + ChatColor.translateAlternateColorCodes('`', Config.motd) : ""));

    }
}
