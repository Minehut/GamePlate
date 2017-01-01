package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.match.Match;
import com.minehut.gameplate.module.modules.time.MatchTimerModule;
import com.minehut.gameplate.module.modules.timeLimit.TimeLimitModule;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Strings;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.command.CommandSender;

/**
 * Created by luke on 12/31/16.
 */
public class MatchCommands {


    @Command(aliases = {"match"}, desc = "View the running match information.")
    public static void match(final CommandContext cmd, CommandSender sender) throws CommandException {
        Match match = GameHandler.getGameHandler().getMatch();

        sender.sendMessage(ChatColor.DARK_PURPLE + ChatUtil.divider);
        sender.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "Playing on " + ChatColor.AQUA + match.getCurrentMap().getMap().getName());
        sender.sendMessage(ChatColor.DARK_PURPLE + "# ");
        sender.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "The current match time is " + ChatColor.AQUA + Strings.formatTime(MatchTimerModule.getTimeInSeconds()));
        sender.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "The time limit is " + ChatColor.RED + Strings.formatTime(TimeLimitModule.getTimeLimit()));
        sender.sendMessage(ChatColor.DARK_PURPLE + ChatUtil.divider);
    }
}
