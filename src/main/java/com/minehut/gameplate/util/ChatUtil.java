package com.minehut.gameplate.util;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
}
