package com.minehut.gameplate.util;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.ChatMessage;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.chat.UnlocalizedChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

/**
 * Created by luke on 12/19/16.
 */
public class ChatUtil {

    public static ChatColor HEADER = ChatColor.YELLOW;
    public static ChatColor TEXT = ChatColor.DARK_AQUA;
    public static ChatColor HIGHLIGHT = ChatColor.AQUA;

    public static String DIVIDER = "======================================";

    public static void sendMessage(Player player, ChatConstant chatConstant, String... messages) {
        sendMessage(player, ChatUtil.HEADER,  chatConstant, messages);
    }

    public static void sendMessage(Player player, ChatColor chatColor, ChatConstant chatConstant, String... messages) {
        player.sendMessage(chatColor + new LocalizedChatMessage(chatConstant, messages).getMessage(player.spigot().getLocale()));
    }

    public static void sendWarningMessage(Player player, ChatConstant chatConstant, String... messages) {
        player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.GRAY + new LocalizedChatMessage(chatConstant, messages).getMessage(player.spigot().getLocale()));
    }

    public static void sendWarningMessage(Player player, String message) {
        player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.GRAY + message);
    }

    public static String getWarningMessage(String msg) {
        if (msg == null) return null;
        else return ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg;
    }

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).spigot().getLocale() : Locale.getDefault().toString();
    }

    public static void broadcastMessage(ChatMessage chatMessage) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(chatMessage.getMessage(player.spigot().getLocale()));
        }
        Bukkit.getLogger().log(Level.ALL, chatMessage.getMessage(Locale.getDefault().toString()));
    }

    public static void broadcastMessage(ChatConstant chatConstant, String... messages) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, chatConstant, messages);
        }
        Bukkit.getLogger().log(Level.ALL, new LocalizedChatMessage(chatConstant, messages).getMessage(Locale.getDefault().toString()));
    }

    public static ChatMessage toChatMessage(List<String> names, ChatColor nameColor, ChatColor extraColor) {
        int size = names.size();
        if (size == 1) {
            return new UnlocalizedChatMessage(nameColor + names.get(0));
        } else if (size > 1) {
            String first = "";
            for (String name : names) {
                int index = names.indexOf(name);
                if (index < size - 2) {
                    first += nameColor + name + extraColor + ", ";
                } else if (index == size - 2) {
                    first += nameColor + name + extraColor;
                } else if (index == size - 1) {
                    return new LocalizedChatMessage(ChatConstant.MISC_AND, first, nameColor + name + extraColor);
                }
            }
        }
        return new UnlocalizedChatMessage("");
    }
}
