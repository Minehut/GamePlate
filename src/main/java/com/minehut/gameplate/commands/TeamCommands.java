package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
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

    @Command(aliases = {"team"}, desc = "View your team.")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_COMMAND_PLAYERS_ONLY).getMessage(ChatUtil.getLocale(sender))));
            return;
        }

        Player player = (Player) sender;
        TeamModule teamModule = TeamManager.getTeamByPlayer(player);

        player.sendMessage(ChatColor.DARK_PURPLE + ChatUtil.divider);
        player.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "You are on the " + teamModule.getColor() + teamModule.getName() + ChatColor.DARK_AQUA + ".");
        player.sendMessage(ChatColor.DARK_PURPLE + "# ");
        player.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_AQUA + "There are " + ChatColor.AQUA + teamModule.getPlayers().size() + ChatColor.DARK_AQUA + " players on your team.");
        player.sendMessage(ChatColor.DARK_PURPLE + ChatUtil.divider);
    }
}
