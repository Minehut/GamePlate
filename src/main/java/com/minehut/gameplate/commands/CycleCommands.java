package com.minehut.gameplate.commands;

import com.minehut.gameplate.GameHandler;
import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.module.modules.team.TeamModule;
import com.minehut.gameplate.module.modules.teamManager.TeamManager;
import com.minehut.gameplate.util.ChatUtil;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    @Command(aliases = {"worlds"}, desc = "View loaded worlds")
    public static void worlds(final CommandContext cmd, CommandSender sender) throws CommandException {
        sender.sendMessage("Loaded Worlds: " + Bukkit.getWorlds().size());
    }
}
