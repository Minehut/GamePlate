package com.minehut.gameplate.module.modules.observers;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.event.GameSpawnEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.respawn.RespawnModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.Items;
import com.minehut.gameplate.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import org.bukkit.inventory.ItemStack;

public class ObserverModule extends Module {

    private int hotbarId = -1;

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        this.hotbarId = Bukkit.getScheduler().scheduleSyncRepeatingTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                TeamModule observes = TeamManager.getObservers();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!GameHandler.getGameHandler().getMatch().isRunning() || observes.containsPlayer(player)) {
                        Players.sendActionbar(player, ChatColor.AQUA + new LocalizedChatMessage(ChatConstant.UI_CURRENTLY_SPECTATING).getMessage(player.spigot().getLocale()));
                    } else {
                        Players.sendActionbar(player, "");
                    }
                }
            }
        }, 0, 10);
    }

    /*
     * Returns true if the player is on the observers team OR the player is dead.
     */
    public static boolean isObserver(Player player) {
        TeamModule team = TeamManager.getTeamByPlayer(player);

        if (team != null && team.isObserver() || RespawnModule.isPlayerDead(player)) {
            return true;
        } else if (!GameHandler.getGameHandler().getMatch().isRunning()) { //the game hasn't started yet
            return true;
        } else {
            return false;
        }
    }

    private void giveObserversKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, Items.createItem(Material.COMPASS, 1, (short) 0, ChatColor.BLUE + "Teleport Tool"));
        player.updateInventory();

        player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        player.setFlying(true);

        player.setCollidable(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameSpawn(GameSpawnEvent event) {
        if (event.getTeam().isObserver()) {
            this.giveObserversKit(event.getPlayer());
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (isObserver((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        if (isObserver(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

//        for (Player player : TeamManager.getObservers().getPlayers()) {
//            if (event.getBlockPlaced().getLocation().distance(player.getLocation()) <= 3) {
//                player.teleport(player.getLocation().add(0, 5, 0));
//            }
//        }
    }

    @EventHandler
    public void onBlockChange(BlockBreakEvent event) {
        if (isObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (isObserver(event.getPlayer())) {
            if (event.getRightClicked() instanceof ItemFrame) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && (isObserver((Player) event.getWhoClicked()) || (isObserver((Player) event.getWhoClicked()) && !event.getInventory().getType().equals(InventoryType.PLAYER)))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isObserver(event.getPlayer())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (isObserver((Player) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player && isObserver((Player) event.getAttacker())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && isObserver((Player) event.getEntered())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player && isObserver((Player) event.getExited())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (isObserver(event.getPlayer())) {
            if (event.getTo().getY() <= -64) {
                //todo: teleport to observers spawn.
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (isObserver(event.getPlayer())) {
            if (event.getTo().getY() <= -64) {
                //todo: teleport to observers spawn.
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (isObserver((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (isObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            if (isObserver((Player) event.getRemover())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (isObserver(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombustEvent(EntityCombustByBlockEvent event) {
        if (event.getEntity() instanceof Player && isObserver((Player)event.getEntity())){
            event.getEntity().setFireTicks(0);
        }
    }

}
