package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.util.ChatUtil;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lucas on 12/30/2016.
 */
public class RotationCommands {

    @Command(desc = "View the rotation", aliases = {"rotation", "rot"})
    @CommandPermissions("gameplate.rotation")
    public static void rotation(CommandContext cmd, CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_ROTATION_HEAD).getMessage(ChatUtil.getLocale(sender)));

        for(int i = 0; i < GamePlate.getInstance().getGameHandler().getRepositoryManager().getRotation().size(); i++) {
            LoadedMap loadedMap = GameHandler.getGameHandler().getRepositoryManager().getRotation().get(i);
            ChatColor color;
            if (GameHandler.getGameHandler().getMatch().getCurrentMap().getMap() == loadedMap) {
                color = ChatColor.GOLD;
            } else {
                color = ChatColor.DARK_AQUA;
            }
            sender.sendMessage(color.toString() + (i + 1) + ". " + loadedMap.getName());
        }

        sender.sendMessage(" ");
    }

}
