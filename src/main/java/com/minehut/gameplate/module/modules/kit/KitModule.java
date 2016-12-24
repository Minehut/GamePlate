package com.minehut.gameplate.module.modules.kit;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.module.Module;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Lucas on 12/21/2016.
 */
public class KitModule extends Module {

    private static KitModule instance;

    private Map<UUID, Inventory> kitMenus = new HashMap<>();

    private List<Kit> kits;

    public KitModule(List<Kit> kits) {
        this.kits = kits;
    }

    public Kit getKit(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    @Override
    public void enable() {
        super.enable();
        instance = this;
    }

    @Override
    public void disable() {
        super.disable();
        // Cleanup static variable
        instance = null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        kitMenus.put(event.getPlayer().getUniqueId(), makeNewInventory(event.getPlayer()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        kitMenus.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getWhoClicked() instanceof Player) {
            Inventory inventory = kitMenus.get(event.getWhoClicked().getUniqueId());
            if (event.getClickedInventory().equals(inventory)) {

            }
        }
    }

    @Command(aliases = {"kits", "kit"}, desc = "Open the kit selector")
    public static void kitsCommand(CommandContext cmd, CommandSender sender) {
        if (instance == null) return;
        if (sender instanceof Player) {
            ((Player)sender).openInventory(instance.getKitMenus().get(((Player) sender).getUniqueId()));
        }
    }

    private Inventory makeNewInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, new LocalizedChatMessage(ChatConstant.UI_KIT_MENU_TITLE, player.getDisplayName()).getMessage(player.spigot().getLocale()));
        int slot = 1;
        for (Kit kit : kits) {
            if (slot != -1) {
                slot = kit.getSlot();
            }
            inventory.setItem(slot, kit.getIcon(player));
            slot += 2;
        }
        return inventory;
    }

    private Map<UUID, Inventory> getKitMenus() { return kitMenus; }

}
