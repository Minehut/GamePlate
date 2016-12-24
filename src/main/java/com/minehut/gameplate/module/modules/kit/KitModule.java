package com.minehut.gameplate.module.modules.kit;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitModule extends Module {

    private Map<TeamModule, Kit> teamKits;

    private Kit emptyKit;

    public KitModule(Map<TeamModule, Kit> teamKits) {
        this.teamKits = teamKits;
        this.emptyKit = new Kit("empty", Collections.emptyList());
    }

    public Kit getKit(TeamModule team) {
        if (teamKits.containsKey(team)) {
            return teamKits.get(team);
        } else {
            return emptyKit;
        }
    }

    public Kit getKit(String name) {
        for (Kit kit : teamKits.values()) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onGameSpawn(GameSpawnEvent event) {
        event.setKit(getKit(event.getTeam()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLastGameSpawn(GameSpawnEvent event) {
        event.getKit().apply(event.getPlayer());
    }

}
