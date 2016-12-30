package com.minehut.gameplate.module.modules.timers;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.chat.ChatMessage;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.CycleCompleteEvent;
import com.minehut.gameplate.module.Module;
import com.minehut.gameplate.module.TaskedModule;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Countdown extends TaskedModule implements Cancellable {

    private boolean cancelled = true, canRun = true, destroyOnEnd;
    private int time, originalTime;

    protected List<BossBar> bossBars = new ArrayList<>();

    public Countdown() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCountdownCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getBossBar(player) == null) {
                BossBar bossBar = createBossBar(player);
                bossBar.addPlayer(player);
                bossBars.add(bossBar);
            }
        }
    }

    @EventHandler
    public void onCountdownJoin(PlayerJoinEvent event) {
        BossBar bossBar = createBossBar(event.getPlayer());
        this.bossBars.add(bossBar);
        bossBar.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onCountdownQuit(PlayerQuitEvent event) {
        BossBar bossBar = getBossBar(event.getPlayer());
        bossBar.removeAll();
        this.bossBars.remove(bossBar);
    }

    @EventHandler
    public void onCountdownQuit(PlayerKickEvent event) {
        BossBar bossBar = getBossBar(event.getPlayer());
        bossBar.removeAll();
        this.bossBars.remove(bossBar);
    }

    public abstract BossBar createBossBar(Player player);

    @Override
    public void disable() {
        for (BossBar bossBar : this.bossBars) {
            bossBar.removeAll();
        }

        HandlerList.unregisterAll(this);
    }

    @Override
    public final void run() {
        if (time < 0 || !canRun) cancelled = true;

        if (!isCancelled()) {

            for (BossBar bossBar : this.bossBars) {
                bossBar.setProgress(time / originalTime);
            }

            if (time % 20 == 0) {
                if (time == 0) {
                    for (BossBar bossBar : this.bossBars) {
                        if(bossBar.getPlayers().isEmpty()) continue;
                        bossBar.setTitle(getBossbarEndMessage(bossBar.getPlayers().get(0)).getMessage(bossBar.getPlayers().get(0).spigot().getLocale()));
                    }
                    setCancelled(true);
                } else {
                    for (BossBar bossBar : this.bossBars) {
                        if(bossBar.getPlayers().isEmpty()) continue;
                        bossBar.setTitle(getBossbarMessage(bossBar.getPlayers().get(0)).getMessage(bossBar.getPlayers().get(0).spigot().getLocale()));
                    }
                    onRun();
                }
            }
            time--;
        }
    }

    public void onRun() {}

    public boolean startCountdown(int time) {
        if (canStart() && time >= 0 ) {
            this.time = time * 20;
            this.originalTime = this.time;
            this.setCancelled(false);

            return true;
        } else {
            return false;
        }
    }

    public boolean canStart() {
        return true;
    }

    private boolean endCountdown() {
        if (canEnd()) {
            if (destroyOnEnd) {
                canRun = false;
                for (BossBar bossBar : this.bossBars) {
                    bossBar.setVisible(false);
                }
            }
            onCountdownEnd();
            return true;
        } return false;
    }

    public boolean canEnd() {
        return true;
    }

    public int getTime() {
        return time / 20;
    }

    // Bossbar messages
    public abstract ChatMessage getBossbarMessage(Player player);
    public abstract ChatMessage getBossbarEndMessage(Player player);

    // Actions
    public void onCountdownStart() {};
    public void onCountdownCancel() {};
    public void onCountdownEnd() {};

    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    @Override
    public final void setCancelled(boolean cancelled) {
        if (this.cancelled ^ cancelled && (canRun || cancelled)) {
            this.cancelled = cancelled;
            if (isCancelled()) {
                for (BossBar bossBar : this.bossBars) {
                    bossBar.setVisible(false);
                }

                if (time == 0) endCountdown();
                else onCountdownCancel();
            } else {
                onCountdownStart();
                if (time != 0) {
                    for (BossBar bossBar : this.bossBars) {
                        bossBar.setVisible(true);
                    }
                }
            }
        }
    }

    public static void stopCountdowns() {
        for (Countdown countdown : GameHandler.getGameHandler().getMatch().getModules().getModules(Countdown.class)) {
            countdown.setCancelled(true);
        }
    }

    public BossBar getBossBar(Player player) {
        for (BossBar bossBar : this.bossBars) {
            if (bossBar.getPlayers().contains(player)) {
                return bossBar;
            }
        }
        return null;
    }

}
