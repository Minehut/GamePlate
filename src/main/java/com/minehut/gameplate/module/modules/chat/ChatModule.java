package com.minehut.gameplate.module.modules.chat;

import com.minehut.gameplate.event.api.GamePlateAllowedChatEvent;
import com.minehut.gameplate.event.api.GamePlatePrefixEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by luke on 12/28/16.
 */
public class ChatModule extends Module {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());

        GamePlateAllowedChatEvent allowedChatEvent = new GamePlateAllowedChatEvent(event.getPlayer());
        Bukkit.getPluginManager().callEvent(allowedChatEvent);

        if (allowedChatEvent.isAllowedChat()) {
            GamePlatePrefixEvent prefixEvent = new GamePlatePrefixEvent(event.getPlayer());
            Bukkit.getPluginManager().callEvent(prefixEvent);

            String prefix = prefixEvent.getPrefix();
            if (prefix.equals("")) {
                prefix = ChatColor.GRAY.toString();
            }

            if (teamModule == null) return;

            String formatted = teamModule.getColor() + "[" + teamModule.getName().substring(0, 1) + "] " + prefix + event.getPlayer().getName() + ChatColor.WHITE + ": " + event.getMessage();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(formatted);
            }
            System.out.println(formatted);
        }
    }

    public static void sendToTeam(TeamModule teamModule, String msg) {
        for (Player player : teamModule.getPlayers()) {
            player.sendMessage(msg);
        }
    }

}
