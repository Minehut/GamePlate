package com.minehut.gameplate.module.modules.observers;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.TaskedModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ColorUtil;
import com.minehut.gameplate.util.Items;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

/**
 * Created by luke on 1/3/17.
 */
public class TeamPicker extends TaskedModule {
    private HashMap<UUID, Inventory> inventories = new HashMap<>();
    private long lastUpdate = 0;

    @Override
    public void run() {
        if (System.currentTimeMillis() - lastUpdate > 1000) {
            lastUpdate = System.currentTimeMillis();

            for (UUID uuid : this.inventories.keySet()) {
                updateInventory(uuid);
            }
        }
    }

    public static ItemStack getTeamPickerHotbarItem(Player player) {
        return Items.createItem(Material.LEATHER_HELMET, 1, (short) 0, ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_TEAM_PICKER_ITEM).getMessage(player.spigot().getLocale()));
    }

    public void createAndOpenInventory(Player player) {
        if (this.inventories.containsKey(player.getUniqueId())) {
            player.openInventory(this.inventories.get(player.getUniqueId()));
        } else {
            Inventory inventory = Bukkit.createInventory(null, 9, new LocalizedChatMessage(ChatConstant.UI_TEAM_PICKER_TITLE).getMessage(player.spigot().getLocale()));
            this.inventories.put(player.getUniqueId(), inventory);
            updateInventory(player.getUniqueId());

            player.openInventory(inventory);
        }
    }

    public void updateInventory(UUID uuid) {
        Inventory inventory = inventories.get(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if(inventory == null || player == null) return;

        inventory.clear();
        inventory.setItem(0, Items.createItem(Material.CHAINMAIL_HELMET, 1, (short) 0,
                ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_TEAM_PICKER_AUTOJOIN).getMessage(player.spigot().getLocale()) + ChatColor.GRAY,
                Arrays.asList("", new LocalizedChatMessage(ChatConstant.UI_TEAM_PICKER_AUTOJOIN_DESC).getMessage(player.spigot().getLocale()))));

        int i = 1;
        for (TeamModule teamModule : TeamManager.getTeamModules()) {
            if(teamModule.isObserver()) continue;

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("Only premium users can choose their team.");

            inventory.setItem(i, Items.createLeatherArmor(Material.LEATHER_HELMET, 1,
                    teamModule.getColor() + teamModule.getName() + ChatColor.GRAY + " (" + teamModule.getMembers().size() + "/" + teamModule.getMaxPlayers() + ")",
                    lore, ColorUtil.convertChatColorToColor(teamModule.getColor())));
            i++;
        }

        TeamModule existingTeam = TeamManager.getTeamByPlayer(player);
        if (!existingTeam.isObserver()) {
            inventory.setItem(8, Items.createItem(Material.LEATHER_BOOTS, 1, (short) 0,  new LocalizedChatMessage(ChatConstant.UI_TEAM_PICKER_AUTOJOIN_LEAVE).getMessage(player.spigot().getLocale()),
                    Arrays.asList()));
        }
    }

    @EventHandler
    public void onSelect(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        boolean update = false;

        if (ObserverModule.isObserver(player)) {
            if (event.getClickedInventory().getTitle().equals(this.inventories.get(player.getUniqueId()).getTitle())) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                    player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);

                    if (event.getCurrentItem().getType() == Material.CHAINMAIL_HELMET) {
                        player.closeInventory();
                        player.performCommand("join");
                        update = true;
                    } else if (event.getCurrentItem().getType() == Material.LEATHER_HELMET) {
                        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) event.getCurrentItem().getItemMeta();

                        for (TeamModule teamModule : TeamManager.getTeamModules()) {
                            if(teamModule.isObserver()) continue;

                            if (ColorUtil.convertChatColorToColor(teamModule.getColor()).equals(leatherArmorMeta.getColor())) {
                                player.closeInventory();
                                player.performCommand("join " + teamModule.getName());
                                update = true;
                            }
                        }
                    } else if (event.getCurrentItem().getType() == Material.LEATHER_BOOTS) {
                        player.closeInventory();
                        player.performCommand("join observers");
                        update = true;
                    }
                }
            }
        }

//        if (update) {
//            Bukkit.getScheduler().runTaskLater(GamePlate.getInstance(), new Runnable() {
//                @Override
//                public void run() {
//                    updateInventory(player.getUniqueId());
//                }
//            }, 0L);
//        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.LEATHER_HELMET) {
                if (ObserverModule.isObserver(event.getPlayer())) {
                    this.createAndOpenInventory(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.inventories.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        this.inventories.remove(event.getPlayer().getUniqueId());
    }

}
