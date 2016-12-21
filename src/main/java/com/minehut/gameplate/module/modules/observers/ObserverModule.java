package com.minehut.gameplate.module.modules.observers;

import com.google.common.base.*;
import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Items;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class ObserverModule extends Module {

    @EventHandler
    public void onCardinalSpawn(GameSpawnEvent event) {
        if (event.getTeam().isObserver()) {
            //todo: apply observer kit
        }
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockBreakEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getRightClicked() instanceof ItemFrame) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && (testDead((Player) event.getWhoClicked()) || (testObserver((Player) event.getWhoClicked()) && !event.getInventory().getType().equals(InventoryType.PLAYER)))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (testObserverOrDead((Player) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player && testObserverOrDead((Player) event.getAttacker())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && testObserverOrDead((Player) event.getEntered())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player && testObserverOrDead((Player) event.getExited())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getTo().getY() <= -64) {
                //todo: teleport to observers spawn.
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (testObserver(event.getPlayer())) {
            if (event.getTo().getY() <= -64) {
                //todo: teleport to observers spawn.
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (testObserverOrDead((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            if (testObserverOrDead((Player) event.getRemover())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (testObserverOrDead(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombustEvent(EntityCombustByBlockEvent event) {
        if (event.getEntity() instanceof Player && testObserver((Player)event.getEntity())){
            event.getEntity().setFireTicks(0);
        }
    }

    public static boolean testObserverOrDead(Player player) {
        return testObserver(player) || testDead(player);
    }

    public static boolean testObserver(Player player) {
        TeamModule team = TeamManager.getTeamByPlayer(player);
        return (team != null && team.isObserver()) || !GameHandler.getGameHandler().getMatch().isRunning();
    }

    public static boolean testDead(Player player) {
        //todo: death module
        return false;
    }

}
