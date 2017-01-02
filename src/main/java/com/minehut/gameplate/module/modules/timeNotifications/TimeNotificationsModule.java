package com.minehut.gameplate.module.modules.timeNotifications;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.chat.UnlocalizedChatMessage;
import com.minehut.gameplate.module.TaskedModule;
import com.minehut.gameplate.module.modules.time.MatchTimerModule;
import com.minehut.gameplate.module.modules.timeLimit.TimeLimitModule;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by luke on 12/31/16.
 */
public class TimeNotificationsModule extends TaskedModule {

    private int lastSecond = -1;

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {

            int limit = TimeLimitModule.getTimeLimit();
            if (limit > 0) {
                int timeRemaining = (int) (TimeLimitModule.getTimeLimit() - MatchTimerModule.getTimeInSeconds());
                if (timeRemaining != lastSecond && (timeRemaining % 60 == 0 || timeRemaining == 10 || timeRemaining <= 5)) {
                    lastSecond = timeRemaining;
                    String formatted = Strings.formatTime(timeRemaining);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatUtil.TEXT + new LocalizedChatMessage(ChatConstant.UI_TIME_REMAINING, new UnlocalizedChatMessage(ChatUtil.HIGHLIGHT + "{0}", formatted)).getMessage(player.spigot().getLocale()));
                    }
                }
            }


        }
    }
}
