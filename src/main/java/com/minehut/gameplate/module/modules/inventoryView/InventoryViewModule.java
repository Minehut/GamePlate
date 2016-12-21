package com.minehut.gameplate.module.modules.inventoryView;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.PlayerChangeTeamEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.modules.observers.ObserverModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Items;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Created by luke on 12/20/16.
 */
public class InventoryViewModule extends Module {
    private Map<UUID, List<UUID>> viewing = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (ObserverModule.testObserver(event.getPlayer())) {
            if (event.getRightClicked() instanceof Player && !event.getPlayer().isSneaking()){
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    openInventory(event.getPlayer(), (Player) event.getRightClicked(), false);
                }
            }
        }
    }

    public void openInventory(Player viewer, Player view, boolean message){
        if (ObserverModule.testObserver(viewer) && !ObserverModule.testObserver(view)) {
            viewer.openInventory(getFakeInventory(view, viewer.spigot().getLocale()));
            if (!viewing.containsKey(view.getUniqueId())) {
                viewing.put(view.getUniqueId(), new ArrayList<UUID>());
            }
            viewing.get(view.getUniqueId()).add(viewer.getUniqueId());
        } else if (message){
            ChatUtil.sendMessage(viewer, ChatColor.RED, ChatConstant.ERROR_INVENTORY_NOT_VIEWABLE);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        for (UUID uuid : viewing.keySet()) {
            if (viewing.get(uuid).contains(event.getPlayer().getUniqueId())) {
                List<UUID> viewingList = viewing.get(uuid);
                viewingList.remove(event.getPlayer().getUniqueId());
                viewing.put(uuid, viewingList);
            }
        }
    }

    public void closeInventories (UUID uuid) {
        if (viewing.containsKey(uuid)) {
            for (int i = 0; i < viewing.get(uuid).size(); i++) {
                if (Bukkit.getPlayer(viewing.get(uuid).get(0)) != null) {
                    Bukkit.getPlayer(viewing.get(uuid).get(0)).closeInventory();
                }
            }
        }
    }

    public void refreshView(final UUID view) {
        if (Bukkit.getPlayer(view) != null && viewing.containsKey(view)) {
            List<UUID> viewers = viewing.get(view);
            for (int i = 0; i < viewers.size(); i++) {
                Player player = Bukkit.getPlayer(viewers.get(i));
                if (player != null && player.getOpenInventory().getTitle().contains(Bukkit.getPlayer(view).getName())) {
                    Inventory fake = getFakeInventory(Bukkit.getPlayer(view), player.spigot().getLocale());
                    for (int i2 = 0; i2 < 45; i2 ++) {
                        try {
                            player.getOpenInventory().setItem(i2, fake.getItem(i2));
                        } catch (NullPointerException e) {
                        }
                    }
                    if (!player.getOpenInventory().getTitle().equals(Bukkit.getPlayer(view).getDisplayName())){
                        player.openInventory(fake);
                        viewing.get(view).add(viewers.get(i));
                    }
                }
            }
        }
    }

    public Inventory getFakeInventory(Player player, String locale) {
        Inventory inventory = Bukkit.createInventory(null, 45, player.getDisplayName().length() > 32 ? TeamManager.getTeamByPlayer(player).getColor() + player.getName() : player.getDisplayName());
        inventory.setItem(0, player.getInventory().getHelmet());
        inventory.setItem(1, player.getInventory().getChestplate());
        inventory.setItem(2, player.getInventory().getLeggings());
        inventory.setItem(3, player.getInventory().getBoots());
        inventory.setItem(4, player.getInventory().getItemInOffHand());

        ItemStack potion;
        if (player.getActivePotionEffects().size() > 0){
            ArrayList<String> effects = new ArrayList<>();
            for (PotionEffect effect : player.getActivePotionEffects()) {
                String effectName = WordUtils.capitalizeFully(effect.getType().getName().toLowerCase().replaceAll("_", " "));
                effects.add(ChatColor.YELLOW + effectName + " " + (effect.getAmplifier() + 1));
            }
            potion = Items.createItem(Material.POTION, 1, (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_POTION_EFFECTS).getMessage(locale), effects);
        } else {
            potion = Items.createItem(Material.GLASS_BOTTLE, 1, (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_POTION_EFFECTS).getMessage(locale), new ArrayList<>(Collections.singletonList(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_NO_POTION_EFFECTS).getMessage(locale))));
        }
        inventory.setItem(6, potion);
        ItemStack food = Items.createItem(Material.COOKED_BEEF, player.getFoodLevel(), (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_HUNGER_LEVEL).getMessage(locale));
        inventory.setItem(7, food);
        ItemStack health = Items.createItem(Material.REDSTONE, (int) Math.ceil(player.getHealth()), (short) 0, ChatColor.AQUA + "" + ChatColor.ITALIC + new LocalizedChatMessage(ChatConstant.UI_HEALTH_LEVEL).getMessage(locale));
        inventory.setItem(8, health);
        for (int i = 36; i <= 44; i++) {
            inventory.setItem(i, player.getInventory().getItem(i - 36));
        }
        for (int i = 9; i <= 35; i++) {
            inventory.setItem(i, player.getInventory().getItem(i));
        }
        return inventory;
    }

    @EventHandler
    public void onViewingPlayerPickupItem(PlayerPickupItemEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onViewingPlayerDropItem(PlayerDropItemEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onViewingFoodLevelChange(FoodLevelChangeEvent event) {
        refreshView(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onViewingEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            refreshView(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onViewingEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            refreshView(event.getEntity().getUniqueId());
        }
    }

    private void updateNextTick(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GamePlate.getInstance(), new Runnable() {
            @Override
            public void run() {
                refreshView(player.getUniqueId());
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onViewingPlayerRespawn(PlayerRespawnEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            refreshView(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        refreshView(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        closeInventories(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryClick(CraftItemEvent event) {
        updateNextTick((Player) event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryMoveItem(InventoryClickEvent event) {
        updateNextTick((Player) event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryClick(InventoryCreativeEvent event) {
        updateNextTick((Player) event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onViewingInventoryMoveItem(InventoryDragEvent event) {
        updateNextTick((Player) event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeamEvent(PlayerChangeTeamEvent event) {
        if (event.getNewTeam() != null && event.getNewTeam().isObserver()){
            closeInventories(event.getPlayer().getUniqueId());
        }
    }


    /*
     * Open fake inventories for chests on the map.
     * This stops observers from modifying the items in any way.
     */
    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if (ObserverModule.testObserver(event.getPlayer())) {
            event.setCancelled(true);
            if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (event.getPlayer().getInventory().getItemInMainHand() != null && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WRITTEN_BOOK))){
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.ALLOW);
            }
            if (event.getClickedBlock() != null && !event.getPlayer().isSneaking() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                    Inventory chest = Bukkit.createInventory(null, ((Chest) event.getClickedBlock().getState()).getInventory().getSize());
                    for (int i = 0; i < ((Chest) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        chest.setItem(i, ((Chest) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(chest);
                }
                if (event.getClickedBlock().getType().equals(Material.FURNACE) || event.getClickedBlock().getType().equals(Material.BURNING_FURNACE)) {
                    Inventory furnace = Bukkit.createInventory(null, InventoryType.FURNACE);
                    for (int i = 0; i < ((Furnace) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        furnace.setItem(i, ((Furnace) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(furnace);
                }
                if (event.getClickedBlock().getType().equals(Material.DISPENSER)) {
                    Inventory dispenser = Bukkit.createInventory(null, InventoryType.DISPENSER);
                    for (int i = 0; i < ((Dispenser) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        dispenser.setItem(i, ((Dispenser) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(dispenser);
                }
                if (event.getClickedBlock().getType().equals(Material.DROPPER)) {
                    Inventory dropper = Bukkit.createInventory(null, InventoryType.DROPPER);
                    for (int i = 0; i < ((Dropper) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        dropper.setItem(i, ((Dropper) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(dropper);
                }
                if (event.getClickedBlock().getType().equals(Material.HOPPER)) {
                    Inventory hopper = Bukkit.createInventory(null, InventoryType.HOPPER);
                    for (int i = 0; i < ((Hopper) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        hopper.setItem(i, ((Hopper) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(hopper);
                }
                if (event.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
                    Inventory brewingStand = Bukkit.createInventory(null, InventoryType.BREWING);
                    for (int i = 0; i < ((BrewingStand) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        brewingStand.setItem(i, ((BrewingStand) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(brewingStand);
                }
                if (event.getClickedBlock().getType().equals(Material.BEACON)) {
                    Inventory beacon = Bukkit.createInventory(null, InventoryType.BEACON);
                    for (int i = 0; i < ((Beacon) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        beacon.setItem(i, ((Beacon) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(beacon);
                }
            }
        }
    }
}
