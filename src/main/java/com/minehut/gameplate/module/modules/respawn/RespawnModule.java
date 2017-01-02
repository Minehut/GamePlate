package com.minehut.gameplate.module.modules.respawn;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.module.modules.visibility.Visibility;
import com.minehut.gameplate.util.Players;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by luke on 12/21/16.
 */
public class RespawnModule extends Module {
    private static int DEFAULT_RESPAWN_TIME = 60; //ticks

    private Map<UUID, Long> deadPlayers = new HashMap<>(); //Long = System time when player died. Used for respawn timers.
    private HashMap<TeamModule, Double> respawnTimers = new HashMap<>();

    private ArrayList<TeamModule> denyRespawns = new ArrayList<>();

    public RespawnModule() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(GamePlate.getInstance(), () -> {
            for (UUID uuid : deadPlayers.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (canPlayerRespawn(player)) {
                    player.resetTitle();
                    respawnPlayer(player);
                } else {
                    double d = round(getTimeLeft(player), 1);
                    player.sendTitle(ChatColor.RED + ChatColor.BOLD.toString() + new LocalizedChatMessage(ChatConstant.UI_DEAD).getMessage(player.spigot().getLocale()), ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_RESPAWN_TIMER, ChatColor.AQUA.toString() + d).getMessage(player.spigot().getLocale()), 0, Integer.MAX_VALUE, 0);
                }
            }
        }, 1L, 1L);
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public void setTeamCanRespawn(TeamModule team, boolean state) {
        if (state) {
            this.denyRespawns.remove(team);
        } else {
            this.denyRespawns.remove(team);
            this.denyRespawns.add(team);
        }
    }

    public double getTimeLeft(Player player) {
        if(!isPlayerDead(player)) return 0;

        TeamModule team = TeamManager.getTeamByPlayer(player);
        double timer = DEFAULT_RESPAWN_TIME;
        if (this.respawnTimers.containsKey(team)) {
            timer = this.respawnTimers.get(team);
        }

        return ((timer * 50) - (System.currentTimeMillis() - this.deadPlayers.get(player.getUniqueId()))) / 1000;
    }

    public boolean canPlayerRespawn(Player player) {
        if(!isPlayerDead(player)) return true;

        TeamModule team = TeamManager.getTeamByPlayer(player);

        if (this.denyRespawns.contains(team)) {
            return false;
        }

        double timer = DEFAULT_RESPAWN_TIME;
        if (this.respawnTimers.containsKey(team)) {
            timer = this.respawnTimers.get(team);
        }

        return System.currentTimeMillis() - this.deadPlayers.get(player.getUniqueId()) > timer * 50;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        double finalHealth = player.getHealth() - event.getFinalDamage();

        if (finalHealth > 0.01) return;

        event.setCancelled(true);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        event.setDamage(1);
        player.setLastDamageCause(event);

        killPlayer(player, null, event.getCause());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        double finalHealth = player.getHealth() - event.getFinalDamage();

        if (finalHealth > 0.01) return;

        event.setCancelled(true);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        event.setDamage(1);
        player.setLastDamageCause(event);

        if (event.getDamager() instanceof Player) {
            killPlayer(player, (Player)event.getDamager(), event.getCause());
        } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            killPlayer(player, (Player)((Projectile) event.getDamager()).getShooter(), event.getCause());
        } else {
            killPlayer(player, null, event.getCause());
        }
    }

    public void killPlayer(Player dead, Player killer, EntityDamageEvent.DamageCause cause) {
        this.deadPlayers.put(dead.getUniqueId(), System.currentTimeMillis());

        GameDeathEvent cardinalDeathEvent = new GameDeathEvent(dead, killer, cause);
        Bukkit.getServer().getPluginManager().callEvent(cardinalDeathEvent);

        Players.resetPlayer(dead);
        GameHandler.getGameHandler().getMatch().getModules().getModule(Visibility.class).showOrHide(dead);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == dead) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
            } else if (killer != null && player == killer) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1.35F);
            } else {
                player.playSound(dead.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1, 1.35F);
            }
        }

        if (killer != null) {
            killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5F);
        }

        new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0).apply(dead);
        new PotionEffect(PotionEffectType.CONFUSION, 100, 0).apply(dead);
        new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 6).apply(dead);

    }

    private void respawnPlayer(Player player) {
        this.deadPlayers.remove(player.getUniqueId());

        TeamModule teamModule = TeamManager.getTeamByPlayer(player);
        GameSpawnEvent event = new GameSpawnEvent(player, teamModule, teamModule.getRandomSpawn());
        Bukkit.getPluginManager().callEvent(event);

        player.teleport(event.getSpawn().toLocation());

    }

    public static boolean isPlayerDead(Player player) {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(RespawnModule.class).deadPlayers.containsKey(player.getUniqueId());
    }
}
