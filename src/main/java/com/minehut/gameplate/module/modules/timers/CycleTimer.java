package com.minehut.gameplate.module.modules.timers;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.ChatMessage;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.chat.UnlocalizedChatMessage;
import com.minehut.gameplate.event.MatchEndEvent;
import com.minehut.gameplate.event.MatchStartEvent;
import com.minehut.gameplate.match.MatchState;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CycleTimer extends Countdown {

    private boolean hasPlayed = false;

    @Override
    public BossBar createBossBar(Player player) {
        BossBar bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(false);
        return bossBar;
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.hasPlayed = true;
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        startCountdown(Config.cycleDefault);
    }

    @Override
    public ChatMessage getBossbarMessage(Player player) {
        return new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.GOLD + GameHandler.getGameHandler().getRepositoryManager().getRotation().getNext().getName() + ChatColor.YELLOW), new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.RED.toString() + getTime() + ChatColor.YELLOW)));
    }

    @Override
    public ChatMessage getBossbarEndMessage(Player player) {
        return getBossbarMessage(player);
    }

    @Override
    public void onCountdownStart() {
        if(getTime() >= 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(getBossbarEndMessage(player).getMessage(player.spigot().getLocale()));
            }
        };
        GameHandler.getGameHandler().getMatch().setMatchState(MatchState.CYCLING);
    }

    @Override
    public boolean canStart() {
        return !GameHandler.getGameHandler().getMatch().isRunning();
    }

    @Override
    public void onCountdownCancel() {
        GameHandler.getGameHandler().getMatch().setMatchState(hasPlayed ? MatchState.ENDED : MatchState.WAITING);
    }

    @Override
    public void onCountdownEnd() {
        GameHandler.getGameHandler().cycleAndMakeMatch();
    }

}