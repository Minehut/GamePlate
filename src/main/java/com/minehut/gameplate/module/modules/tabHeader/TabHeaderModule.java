package com.minehut.gameplate.module.modules.tabHeader;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.ChatMessage;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.map.Contributor;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.TaskedModule;
import com.minehut.gameplate.module.modules.time.MatchTimerModule;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Strings;
import com.minehut.gameplate.util.titleAPI.TitleAPI;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luke on 1/3/17.
 */
public class TabHeaderModule extends TaskedModule {
    private int lastTime = 0;
    private boolean hasEnded = false;
    private List<String> authors = new ArrayList<>();

    public TabHeaderModule() {
        for (Contributor author : GameHandler.getGameHandler().getCurrentMap().getMap().getAuthors()) {
            authors.add(author.getName());
        }
    }

    public void updateAll() {
        ChatColor timeColor = ChatColor.RED;
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            timeColor = ChatColor.GREEN;
        } else if (GameHandler.getGameHandler().getMatch().hasEnded()) {
            timeColor = ChatColor.GOLD;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            TitleAPI.sendTabTitle(player,
                    ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_TAB_HEADER,
                    ChatColor.AQUA + GameHandler.getGameHandler().getCurrentMap().getMap().getName() + ChatColor.DARK_AQUA).getMessage(player.spigot().getLocale()),

                    ChatColor.WHITE + ChatColor.BOLD.toString() + new LocalizedChatMessage(ChatConstant.UI_TAB_FOOTER, "MINEHUT.COM" + ChatColor.GRAY, timeColor + Strings.formatTime(MatchTimerModule.getTimeInSeconds())).getMessage(player.spigot().getLocale()));
        }
    }

    @EventHandler
    public void onCycle(CycleCompleteEvent event) {
        updateAll();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updateAll();
    }

    @Override
    public void run() {
        if ((int) MatchTimerModule.getTimeInSeconds() != lastTime) {
            lastTime = (int) MatchTimerModule.getTimeInSeconds();
            updateAll();
        } else if (!hasEnded && GameHandler.getGameHandler().getMatch().hasEnded()) {
            hasEnded = true;
            updateAll();
        }
    }

}
