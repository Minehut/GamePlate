package com.minehut.gameplate.util;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * Created by luke on 12/19/16.
 */
public class ChatUtil {

    public static void sendMessage(Player player, ChatConstant chatConstant, String... messages) {
        sendMessage(player, ChatColor.DARK_PURPLE,  chatConstant, messages);
    }

    public static void sendMessage(Player player, ChatColor chatColor, ChatConstant chatConstant, String... messages) {
        player.sendMessage(chatColor + new LocalizedChatMessage(chatConstant, messages).getMessage(player.spigot().getLocale()));
    }

    public static void sendWarningMessage(Player player, ChatConstant chatConstant, String... messages) {
        player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.GRAY + new LocalizedChatMessage(chatConstant, messages).getMessage(player.spigot().getLocale()));
    }

    public static String getWarningMessage(String msg) {
        if (msg == null) return null;
        else return ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg;
    }

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).spigot().getLocale() : Locale.getDefault().toString();
    }
}
