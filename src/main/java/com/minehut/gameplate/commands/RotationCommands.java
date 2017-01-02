package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.util.ChatUtil;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Lucas on 12/30/2016.
 */
public class RotationCommands {

    @Command(desc = "View the rotation", aliases = {"rotation", "rot"})
    public static void rotation(CommandContext cmd, CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(ChatUtil.HEADER + new LocalizedChatMessage(ChatConstant.UI_ROTATION_HEAD).getMessage(ChatUtil.getLocale(sender)));

        for(int i = 0; i < GamePlate.getInstance().getGameHandler().getRepositoryManager().getRotation().size(); i++) {
            LoadedMap loadedMap = GameHandler.getGameHandler().getRepositoryManager().getRotation().get(i);
            ChatColor color;
            if (GameHandler.getGameHandler().getMatch().getCurrentMap().getMap() == loadedMap) {
                color = ChatUtil.HIGHLIGHT;
            } else {
                color = ChatUtil.TEXT;
            }
            sender.sendMessage(color.toString() + (i + 1) + ". " + loadedMap.getName());
        }

        sender.sendMessage(" ");
    }

}
