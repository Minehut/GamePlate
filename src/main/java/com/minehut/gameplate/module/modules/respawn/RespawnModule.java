package com.minehut.gameplate.module.modules.respawn;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.module.modules.visibility.Visibility;
import com.minehut.gameplate.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private static int DEFAULT_RESPAWN_TIME = 20; //ticks

    private Map<UUID, Long> deadPlayers = new HashMap<>(); //Long = System time when player died. Used for respawn timers.
    private HashMap<TeamModule, Double> respawnTimers = new HashMap<>();

    private ArrayList<TeamModule> denyRespawns = new ArrayList<>();

    public RespawnModule() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (UUID uuid : deadPlayers.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (canPlayerRespawn(player)) {
                        respawnPlayer(player);
                    }
                }
            }
        }, 0L, 0L);
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

        return System.currentTimeMillis() - this.deadPlayers.get(player.getUniqueId()) + timer;
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

        return System.currentTimeMillis() - this.deadPlayers.get(player.getUniqueId()) > timer;
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

    public void killPlayer(Player player, Player killer, EntityDamageEvent.DamageCause cause) {
        this.deadPlayers.put(player.getUniqueId(), System.currentTimeMillis());

        GameDeathEvent cardinalDeathEvent = new GameDeathEvent(player, killer, cause);
        Bukkit.getServer().getPluginManager().callEvent(cardinalDeathEvent);

        Players.resetPlayer(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 10)); //todo: something better

        GameHandler.getGameHandler().getMatch().getModules().getModule(Visibility.class).showOrHide(player);

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
