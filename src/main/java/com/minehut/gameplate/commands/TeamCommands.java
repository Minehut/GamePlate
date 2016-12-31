package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.ChatMessage;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.module.modules.chat.ChatModule;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
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
            TeamModule found = TeamManager.getTeamByName(cmd.getString(0));
            if (found == null) {
                ChatUtil.sendWarningMessage(player, ChatConstant.ERROR_NO_TEAM_FOUND);
                return;
            }

            teamManager.attemptJoinTeam(player, found);
        }
    }

    @Command(aliases = {"team", "t"}, desc = "Send a message to your team")
    public static void teamChat(final CommandContext cmd, CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        TeamModule team = TeamManager.getTeamByPlayer(player);
        if (team == null || team.equals(TeamManager.getObservers())) {
            return;
        }
        if (cmd.argsLength() == 0) {
            player.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_NO_MESSAGE).getMessage(ChatUtil.getLocale(sender))));
            return;
        }
        ChatModule.sendToTeam(team, new LocalizedChatMessage(ChatConstant.UI_TEAM_CHAT, team.getColor().toString(), player.getDisplayName(), cmd.getJoinedStrings(0)).getMessage(ChatUtil.getLocale(sender)));
    }

}
