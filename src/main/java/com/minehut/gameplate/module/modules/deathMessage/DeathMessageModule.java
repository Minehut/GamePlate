package com.minehut.gameplate.module.modules.deathMessage;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Created by luke on 1/13/17.
 */
public class DeathMessageModule extends Module {

    @EventHandler
    public void onDeath(GameDeathEvent event) {
        if (event.getKiller() != null) {
            event.getKiller().sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.KILL_MESSAGE, TeamManager.getTeamByPlayer(event.getPlayer()).getColor() + event.getPlayer().getName()).getMessage(event.getKiller().spigot().getLocale()));
            event.getKiller().playSound(event.getKiller().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5F);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == event.getPlayer()) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
            } else if (event.getKiller() != null && player == event.getKiller()) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1.35F);
            } else {
                player.playSound(event.getPlayer().getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1, 1.35F);
            }
        }
    }
}
