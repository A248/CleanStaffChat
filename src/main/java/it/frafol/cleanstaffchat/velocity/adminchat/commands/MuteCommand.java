package it.frafol.cleanstaffchat.velocity.adminchat.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import it.frafol.cleanstaffchat.velocity.CleanStaffChat;
import it.frafol.cleanstaffchat.velocity.enums.VelocityConfig;
import it.frafol.cleanstaffchat.velocity.objects.Placeholder;
import it.frafol.cleanstaffchat.velocity.objects.PlayerCache;

import static it.frafol.cleanstaffchat.velocity.enums.VelocityConfig.*;

public class MuteCommand implements SimpleCommand {

    public final CleanStaffChat PLUGIN;

    public MuteCommand(CleanStaffChat plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource commandSource = invocation.source();

        if (!(ADMINCHAT_MUTE_MODULE.get(Boolean.class))) {
            MODULE_DISABLED.send(commandSource, new Placeholder("prefix", ADMINPREFIX.color()));
            return;
        }

        if (commandSource.hasPermission(VelocityConfig.ADMINCHAT_MUTE_PERMISSION.get(String.class))) {
            if (!PlayerCache.getMuted().contains("true")) {
                PlayerCache.getMuted().add("true");
                ADMINCHAT_MUTED.send(commandSource,
                        new Placeholder("prefix", ADMINPREFIX.color()));
            } else {
                PlayerCache.getMuted().remove("true");
                ADMINCHAT_UNMUTED.send(commandSource,
                        new Placeholder("prefix", ADMINPREFIX.color()));
            }
        } else {
            NO_PERMISSION.send(commandSource,
                    new Placeholder("prefix", ADMINPREFIX.color()));
        }
    }
}