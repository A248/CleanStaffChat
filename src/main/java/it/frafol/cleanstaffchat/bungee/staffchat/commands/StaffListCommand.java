package it.frafol.cleanstaffchat.bungee.staffchat.commands;

import it.frafol.cleanstaffchat.bungee.CleanStaffChat;
import it.frafol.cleanstaffchat.bungee.enums.BungeeCommandsConfig;
import it.frafol.cleanstaffchat.bungee.enums.BungeeConfig;
import it.frafol.cleanstaffchat.bungee.enums.BungeeMessages;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffListCommand extends Command {

    public StaffListCommand() {
        super(BungeeCommandsConfig.STAFFLIST.getStringList().get(0),"", BungeeCommandsConfig.STAFFLIST.getStringList().toArray(new String[0]));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission(BungeeConfig.STAFFLIST_PERMISSION.get(String.class))) {
            return;
        }

        if (args.length == 0) {

            LuckPerms api = LuckPermsProvider.get();

            sender.sendMessage(TextComponent.fromLegacyText(BungeeMessages.LIST_HEADER.color()
                    .replace("%prefix%", BungeeMessages.PREFIX.color())));

            for (ProxiedPlayer players : CleanStaffChat.getInstance().getProxy().getPlayers()) {

                if (players.hasPermission(BungeeConfig.STAFFLIST_PERMISSION.get(String.class))) {

                    User user = api.getUserManager().getUser(players.getUniqueId());

                    if (user == null) {
                        return;
                    }

                    final String prefix = user.getCachedData().getMetaData().getPrefix();
                    Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());

                    if (group == null || group.getDisplayName() == null) {
                        return;
                    }

                    String user_prefix = prefix == null ? group.getDisplayName() : prefix;

                    sender.sendMessage(TextComponent.fromLegacyText(BungeeMessages.LIST_FORMAT.color()
                            .replace("%userprefix%", user_prefix)
                            .replace("%player%", players.getName())
                            .replace("%server%", "")
                            .replace("%prefix%", BungeeMessages.PREFIX.color())));

                }
            }
            sender.sendMessage(TextComponent.fromLegacyText(BungeeMessages.LIST_HEADER.color()
                    .replace("%prefix%", BungeeMessages.PREFIX.color())));
        }
    }
}