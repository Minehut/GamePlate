package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.chat.UnlocalizedChatMessage;
import com.minehut.gameplate.map.LoadedMap;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.map.repository.repositories.Repository;
import com.minehut.gameplate.match.MatchState;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.module.modules.timers.Countdown;
import com.minehut.gameplate.module.modules.timers.CycleTimer;
import com.minehut.gameplate.module.modules.timers.StartTimer;
import com.minehut.gameplate.util.ChatUtil;
import com.sk89q.minecraft.util.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Created by luke on 12/23/16.
 */
public class CycleCommands {

    @Command(aliases = {"cycle"}, desc = "Cycle the map.")
    @CommandPermissions("gameplate.cycle")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        sender.sendMessage("cycling...");
        try {
            GameHandler.getGameHandler().load();
        } catch (RotationLoadException e) {
            e.printStackTrace();
        }
        sender.sendMessage("cycled!");
    }

    @Command(aliases = {"setnext", "sn"}, desc = "Set the next map.", usage = "[map]", min = 1)
    public static void setNext(final CommandContext cmd, CommandSender sender) throws CommandException {
        LoadedMap found = null;

        for (Repository repository : GameHandler.getGameHandler().getRepositoryManager().getRepos()) {
            for (LoadedMap loadedMap : repository.getLoaded()) {
                if (loadedMap.getName().equalsIgnoreCase(cmd.getJoinedStrings(0))) {
                    found = loadedMap;
                }
            }
        }

        if (found == null) {
            for (Repository repository : GameHandler.getGameHandler().getRepositoryManager().getRepos()) {
                for (LoadedMap loadedMap : repository.getLoaded()) {
                    if (loadedMap.getName().toLowerCase().startsWith(cmd.getJoinedStrings(0).toLowerCase())) {
                        found = loadedMap;
                    }
                }
            }
        }

        if (found == null) {
            sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_MAP_NOT_FOUND).getMessage(ChatUtil.getLocale(sender))));
            return;
        }

        GameHandler.getGameHandler().getRepositoryManager().getRotation().setNextMap(found);
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_NEXT_MAP_SET, new UnlocalizedChatMessage(ChatColor.AQUA + "{0}", found.getName())).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"end"}, desc = "End the match.")
    public static void end(final CommandContext cmd, CommandSender sender) throws CommandException {
        TeamModule teamModule = null;

        if (cmd.argsLength() > 0) {
            teamModule = TeamManager.getTeamByName(cmd.getJoinedStrings(0));
            if (teamModule == null) {
                sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_FOUND).getMessage(ChatUtil.getLocale(sender))));
                return;
            }
        }

        GameHandler.getGameHandler().getMatch().end(teamModule);
    }

    @Command(aliases = {"cycle"}, desc = "Cycle the map.")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            sender.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_UNABLE_TO_CYCLE_MID_MATCH).getMessage(ChatUtil.getLocale(sender))));
            return;
        }

        GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).setCancelled(true);

        int time = 5;

        if (cmd.argsLength() > 0) {
            time = Integer.parseInt(cmd.getString(0));
        }
        CycleTimer cycleTimer = GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimer.class);
        cycleTimer.setTime(time);
        cycleTimer.setCancelled(false);
    }

    @Command(aliases = {"start"}, desc = "Start the match.")
    public static void start(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning() || GameHandler.getGameHandler().getMatch().isState(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().isState(MatchState.CYCLING)) {
            sender.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_MATCH_ALREADY_STARTED).getMessage(ChatUtil.getLocale(sender))));
            return;
        }

        int time = 5;

        if (cmd.argsLength() > 0) {
            time = Integer.parseInt(cmd.getString(0));
        }
        StartTimer startTimer = GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class);
        startTimer.setCancelled(false);
        startTimer.setTime(time);
    }

    @Command(aliases = {"cancel"}, desc = "Cancel countdowns.")
    public static void cancel(final CommandContext cmd, CommandSender sender) throws CommandException {
        Countdown.stopCountdowns();
    }

    @Command(aliases = {"refresh"}, desc = "Refresh the map repositories and rotation file.")
    public static void refresh(final CommandContext cmd, CommandSender sender) throws CommandException, IOException, RotationLoadException {
        GameHandler.getGameHandler().getRepositoryManager().refreshRepos();
        GameHandler.getGameHandler().getRepositoryManager().refreshRotation();
        sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.UI_REFRESHED).getMessage(ChatUtil.getLocale(sender)));
    }
}
