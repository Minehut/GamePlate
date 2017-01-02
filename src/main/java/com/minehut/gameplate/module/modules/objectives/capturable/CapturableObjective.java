package com.minehut.gameplate.module.modules.objectives.capturable;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.ColorUtil;
import com.minehut.gameplate.util.Fireworks;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by luke on 1/1/17.
 */
public class CapturableObjective extends ObjectiveModule {
    private Material material;
    private byte data;
    private List<RegionModule> regions;

    private List<UUID> touches = new ArrayList<>();

    public CapturableObjective(String id, String name, Boolean showOnScoreboard, Material material, byte data, List<RegionModule> regions) {
        super(id, name, showOnScoreboard);
        this.material = material;
        this.data = data;
        this.regions = regions;
    }

    @EventHandler
    public void onBlockPickup(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() == material && event.getItem().getItemStack().getData().getData() == data) {
            if(this.touches.contains(event.getPlayer().getUniqueId())) return;

            TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
            if (teamModule == null) return;
            if (teamModule.getObjectives().contains(this)) {
                this.touches.add(event.getPlayer().getUniqueId());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(teamModule.getColor() + new LocalizedChatMessage(ChatConstant.GAME_CAPTURABLE_TOUCHED, event.getPlayer().getName() + ChatColor.DARK_AQUA, ChatColor.AQUA + super.getName() + ChatColor.DARK_AQUA, teamModule.getColor() + teamModule.getName() + ChatColor.DARK_AQUA).getMessage(player.spigot().getLocale()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == this.material && event.getBlockPlaced().getData() == this.data) {
            if (contains(event.getBlockPlaced().getLocation())) {
                TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
                if (teamModule == null) return;
                if (!teamModule.getObjectives().contains(this)) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.GAME_NOT_YOUR_OBJECTIVE);
                    return;
                }

                if (super.isCompletedBy(teamModule)) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.GAME_OBJECTIVE_ALREADY_COMPLETE);
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(teamModule.getColor() + new LocalizedChatMessage(ChatConstant.GAME_CAPTURABLE_COMPLETED, event.getPlayer().getName() + ChatColor.DARK_AQUA, ChatColor.AQUA + super.getName() + ChatColor.DARK_AQUA, teamModule.getColor() + teamModule.getName() + ChatColor.DARK_AQUA).getMessage(player.spigot().getLocale()));

                    if (teamModule.containsPlayer(player)) {
                        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 2f);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8f, 0.8f);
                    }
                }

                Fireworks.spawnFirework(event.getBlockPlaced().getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(ColorUtil.convertChatColorToColor(teamModule.getColor())).build(), 1);

                event.setCancelled(false);
                super.addCompletedBy(teamModule);
            }
        }
    }

    public boolean contains(Location location) {
        for (RegionModule region : regions) {
            if (region.contains(location.toVector())) {
                return true;
            }
        }
        return false;
    }
}
