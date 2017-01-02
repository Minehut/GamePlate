package com.minehut.gameplate.module.modules.tracker;

import com.minehut.gameplate.event.GameDeathEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.tracker.event.TrackerDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class DamageTracker extends Module {

    private static HashMap<UUID, TrackerDamageEvent> events = new HashMap<>();

    protected DamageTracker() {
    }

    public static TrackerDamageEvent getEvent(Player player) {
        return events.containsKey(player.getUniqueId()) ? events.get(player.getUniqueId()) : null;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            TrackerDamageEvent damage;
            Description description = null;
            if (event.getEntity().getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.LADDER)) {
                description = Description.OFF_A_LADDER;
            } else if (event.getEntity().getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.VINE)) {
                description = Description.OFF_A_VINE;
            } else if (event.getEntity().getLocation().getBlock().getType().equals(Material.WATER) || event.getEntity().getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
                description = Description.OUT_OF_THE_WATER;
            } else if (event.getEntity().getLocation().getBlock().getType().equals(Material.LAVA) || event.getEntity().getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)) {
                description = Description.OUT_OF_THE_LAVA;
            }
            if (event.getDamager() instanceof Projectile) {
                OfflinePlayer source = null;
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    source = (Player) ((Projectile) event.getDamager()).getShooter();
                }
                damage = new TrackerDamageEvent((Player) event.getEntity(), source, null, Cause.PLAYER, description, Type.SHOT);
            } else if (event.getDamager() instanceof Player) {
                damage = new TrackerDamageEvent((Player) event.getEntity(), (Player) event.getDamager(), ((Player) event.getDamager()).getItemInHand(), Cause.PLAYER, description, Type.KNOCKED);
            } else if (event.getDamager() instanceof TNTPrimed) {
                UUID uuid = TntTracker.getWhoPlaced(event.getDamager());
                if (uuid != null) {
                    damage = new TrackerDamageEvent((Player) event.getEntity(), Bukkit.getOfflinePlayer(uuid), null, Cause.TNT, description, Type.BLOWN);
                } else damage = new TrackerDamageEvent((Player) event.getEntity(), null, null, Cause.TNT, description, Type.BLOWN);
            } else {
                damage = new TrackerDamageEvent((Player) event.getEntity(), null, null, Cause.PLAYER, description, Type.KNOCKED);
            }
            Bukkit.getServer().getPluginManager().callEvent(damage);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTrackerDamage(TrackerDamageEvent event) {
        events.put(event.getPlayer().getUniqueId(), event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCardinalDeath(GameDeathEvent event) {
        events.put(event.getPlayer().getUniqueId(), null);
    }

}
