package com.minehut.gameplate.module.modules.objectives.destroyable;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.module.modules.objectives.ObjectiveModule;
import com.minehut.gameplate.module.modules.regions.RegionModule;
import com.minehut.gameplate.module.modules.scoreboard.ScoreboardModule;
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
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by luke on 1/1/17.
 */
public class DestroyableObjective extends ObjectiveModule {
    private Material material;
    private byte data;
    private List<RegionModule> regions;
    private int maxHealth;
    private int health;
    private boolean damageSound;
    private boolean completeSound;

    private List<UUID> damagedBy = new ArrayList<>();

    public DestroyableObjective(String id, String name, Boolean showOnScoreboard, Material material, byte data, List<RegionModule> regions, int health, boolean damageSound, boolean completeSound) {
        super(id, name, showOnScoreboard);
        this.material = material;
        this.data = data;
        this.regions = regions;
        this.maxHealth = health;
        this.health = health;
        this.damageSound = damageSound;
        this.completeSound = completeSound;
    }

    @Override
    public String getScoreboardDisplay() {
        if (this.getCompletedBy().size() > 0) {
            return ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + super.getName();
        } else {
            return " " +  super.getName() + " " + (int) Math.floor((((double) this.health) / this.maxHealth) * 100) + "%";
        }
    }

    public boolean isTouchedBy(TeamModule teamModule) {
        for (UUID uuid : this.damagedBy) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                if (teamModule.containsPlayer(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == this.material && event.getBlock().getData() == this.data) {
            if (contains(event.getBlock().getLocation())) {
                TeamModule teamModule = TeamManager.getTeamByPlayer(event.getPlayer());
                if (teamModule == null) return;
                if (!teamModule.getObjectives().contains(this)) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.GAME_NOT_YOUR_OBJECTIVE);
                    event.setCancelled(true);
                    return;
                }

                if (super.isCompletedBy(teamModule)) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), ChatConstant.GAME_OBJECTIVE_ALREADY_COMPLETE);
                    return;
                }

                this.damagedBy.add(event.getPlayer().getUniqueId());

                this.health--;
                if (this.health <= 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(teamModule.getColor() + new LocalizedChatMessage(ChatConstant.GAME_DESTROYABLE_COMPLETE, event.getPlayer().getName() + ChatColor.DARK_AQUA, ChatColor.AQUA + super.getName() + ChatColor.DARK_AQUA, teamModule.getColor() + teamModule.getName() + ChatColor.DARK_AQUA).getMessage(player.spigot().getLocale()));

                        if (this.completeSound) {
                            Fireworks.spawnFirework(event.getBlock().getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(ColorUtil.convertChatColorToColor(teamModule.getColor())).build(), 1);

                            if (teamModule.containsPlayer(player)) {
                                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 2f);
                            } else {
                                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8f, 0.8f);
                            }
                        }
                    }
                    super.addCompletedBy(teamModule);
                } else {
                    for (ScoreboardModule scoreboardModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
                        scoreboardModule.refresh(this);
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(teamModule.getColor() + new LocalizedChatMessage(ChatConstant.GAME_DESTROYABLE_BREAK, event.getPlayer().getName() + ChatColor.DARK_AQUA, ChatColor.AQUA + super.getName() + ChatColor.DARK_AQUA, teamModule.getColor() + teamModule.getName() + ChatColor.DARK_AQUA).getMessage(player.spigot().getLocale()));

                        if (this.damageSound) {
                            Fireworks.spawnFirework(event.getBlock().getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(ColorUtil.convertChatColorToColor(teamModule.getColor())).build(), 1);

                            if (teamModule.containsPlayer(player)) {
                                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 2f);
                            } else {
                                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8f, 0.8f);
                            }
                        }
                    }
                }

                event.setCancelled(false);
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
