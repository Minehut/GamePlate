package com.minehut.gameplate.commands;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
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
        sender.sendMessage(new LocalizedChatMessage(ChatConstant.UI_ROTATION_HEAD).getMessage(ChatUtil.getLocale(sender)));
        GamePlate.getInstance().getGameHandler().getRepositoryManager().getRotation().forEach(map -> {
            sender.sendMessage((map.equals(GamePlate.getInstance().getGameHandler().getMatch().getCurrentMap().getMap()) ? ChatColor.YELLOW : ChatColor.GRAY) + " - " + map.getName());
        });
        sender.sendMessage(" ");
    }

}
