package com.minehut.gameplate;

import com.minehut.gameplate.chat.ChatConstant;
import com.minehut.gameplate.chat.LocaleHandler;
import com.minehut.gameplate.chat.LocalizedChatMessage;
import com.minehut.gameplate.commands.CycleCommands;
import com.minehut.gameplate.commands.RotationCommands;
import com.minehut.gameplate.commands.TeamCommands;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import com.minehut.gameplate.util.ChatUtil;
import com.minehut.gameplate.util.Config;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lucas on 12/18/2016.
 */
public class GamePlate extends JavaPlugin {

    private static GamePlate instance;

    private LocaleHandler localeHandler;
    private GameHandler gameHandler;

    private CommandsManager commands;

    @Override
    public void onEnable() {
        instance = this;

        setupCommands();

        Config.reload(getConfig());
        saveConfig();

        try {
            this.localeHandler = new LocaleHandler(this, Arrays.asList("lang/en.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.gameHandler = new GameHandler();
            gameHandler.load();
        } catch (RotationLoadException e) {
            e.printStackTrace();
        }
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };

        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);

        cmdRegister.register(TeamCommands.class);
        cmdRegister.register(CycleCommands.class);
        cmdRegister.register(RotationCommands.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String locale = ChatUtil.getLocale(sender);
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(locale)));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatUtil.getWarningMessage(e.getUsage().replace("{cmd}", cmd.getName())));
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatUtil.getWarningMessage(e.getMessage()));
            sender.sendMessage(ChatUtil.getWarningMessage(e.getUsage()));
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_NUMBER_STRING).getMessage(locale)));
            } else {
                sender.sendMessage(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_UNKNOWN_ERROR).getMessage(locale)));
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatUtil.getWarningMessage(ChatColor.RED + e.getMessage()));
        }
        return true;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static GamePlate getInstance() { return instance; }

    public void register(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public LocaleHandler getLocaleHandler() {
        return localeHandler;
    }
}
