package it.frafol.cleanstaffchat.bukkit.adminchat.commands;

import it.frafol.cleanstaffchat.bukkit.CleanStaffChat;
import it.frafol.cleanstaffchat.bukkit.enums.SpigotConfig;
import it.frafol.cleanstaffchat.bukkit.objects.PlayerCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MuteCommand implements CommandExecutor {

    public final CleanStaffChat plugin;

    public MuteCommand(CleanStaffChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] strings) {

        if (command.getName().equalsIgnoreCase("acmute")
                || command.getName().equalsIgnoreCase("adminchatmute")
                || command.getName().equalsIgnoreCase("adminmute")) {

            if (!(SpigotConfig.ADMINCHAT_MUTE_MODULE.get(Boolean.class))) {

                sender.sendMessage((SpigotConfig.MODULE_DISABLED.color()
                        .replace("%prefix%", SpigotConfig.ADMINPREFIX.color())));

            }

            if (sender.hasPermission(SpigotConfig.ADMINCHAT_MUTE_PERMISSION.get(String.class))) {

                if (!PlayerCache.getMuted().contains("true")) {

                    PlayerCache.getMuted().add("true");

                    sender.sendMessage((SpigotConfig.ADMINCHAT_MUTED.color()
                            .replace("%prefix%", SpigotConfig.ADMINPREFIX.color())));

                } else {

                    PlayerCache.getMuted().remove("true");

                    sender.sendMessage((SpigotConfig.ADMINCHAT_UNMUTED.color()
                            .replace("%prefix%", SpigotConfig.ADMINPREFIX.color())));
                }

            } else {

                sender.sendMessage((SpigotConfig.NO_PERMISSION.color()
                        .replace("%prefix%", SpigotConfig.ADMINPREFIX.color())));


            }
        }

        return false;

    }
}
