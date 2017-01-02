package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.event.api.GamePlateAllowedChatEvent;
import com.minehut.gameplate.event.api.GamePlatePrefixEvent;
import com.minehut.gameplate.module.modules.chat.ChatModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by luke on 12/23/16.
 */
public class TeamCommands {

    @Command(aliases = {"join"}, desc = "Join the game.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_COMMAND_PLAYERS_ONLY).getMessage(ChatUtil.getLocale(sender))));
            return;
        }
        Player player = (Player) sender;

        TeamManager teamManager = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamManager.class);
        TeamModule existingTeam = TeamManager.getTeamByPlayer(player);

        if (teamManager.getTeamType() == TeamManager.TeamType.SOLO) {
            teamManager.createAndJoinTeamForPlayer(player);
        } else {
            if (cmd.argsLength() > 0) {
                TeamModule found = TeamManager.getTeamByName(cmd.getString(0));
                if (found == null) {
                    ChatUtil.sendWarningMessage(player, ChatConstant.ERROR_NO_TEAM_FOUND);
                    return;
                }

                if (found == existingTeam) {
                    ChatUtil.sendWarningMessage(player, ChatConstant.ERROR_JOIN_SAME_TEAM);
                    return;
                }

                teamManager.attemptJoinTeam(player, found);
            } else {

                if (existingTeam.isObserver()) {
                    teamManager.attemptJoinTeam(player, TeamManager.getTeamWithFewestPlayers());
                } else {
                    player.sendMessage(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_ON_TEAM).getMessage(player.spigot().getLocale()));
                }
            }
        }
    }

    @Command(aliases = {"myteam"}, desc = "View your team.")
    public static void myteam(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_COMMAND_PLAYERS_ONLY).getMessage(ChatUtil.getLocale(sender))));
            return;
        }

        Player player = (Player) sender;
        TeamModule teamModule = TeamManager.getTeamByPlayer(player);

        player.sendMessage(ChatUtil.HEADER + ChatUtil.DIVIDER);
        player.sendMessage(ChatUtil.HEADER + "= " + ChatUtil.TEXT + "You are on the " + teamModule.getColor() + teamModule.getName() + ChatUtil.TEXT + ".");
        player.sendMessage(ChatUtil.HEADER + "= ");
        player.sendMessage(ChatUtil.HEADER + "= " + ChatUtil.TEXT + "There are " + ChatUtil.HIGHLIGHT + teamModule.getPlayers().size() + ChatUtil.TEXT + " players on your team.");
        player.sendMessage(ChatUtil.HEADER + ChatUtil.DIVIDER);
    }

    @Command(aliases = {"team", "t"}, desc = "Send a message to your team")
    public static void teamChat(final CommandContext cmd, CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        TeamModule team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            return;
        }
        if (cmd.argsLength() == 0) {
            ChatUtil.sendMessage(player, ChatConstant.ERROR_NO_MESSAGE);
            return;
        }

        GamePlateAllowedChatEvent canChatEvent = new GamePlateAllowedChatEvent(player);
        Bukkit.getPluginManager().callEvent(canChatEvent);

        GamePlatePrefixEvent prefixEvent = new GamePlatePrefixEvent(player);
        Bukkit.getPluginManager().callEvent(prefixEvent);

        if (canChatEvent.isAllowedChat()) {
            if (prefixEvent.getPrefix().equals("")) {
                ChatModule.sendToTeam(team, new LocalizedChatMessage(ChatConstant.UI_TEAM_CHAT, team.getColor().toString(), player.getDisplayName(), ChatColor.GRAY + cmd.getJoinedStrings(0)).getMessage(ChatUtil.getLocale(sender)));
            } else {
                ChatModule.sendToTeam(team, new LocalizedChatMessage(ChatConstant.UI_TEAM_CHAT, team.getColor().toString(), prefixEvent.getPrefix() + " " + player.getDisplayName(), cmd.getJoinedStrings(0)).getMessage(ChatUtil.getLocale(sender)));
            }
        }
    }

}
