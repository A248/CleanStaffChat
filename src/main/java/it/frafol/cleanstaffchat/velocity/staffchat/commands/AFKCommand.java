package it.frafol.cleanstaffchat.velocity.staffchat.commands;

import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.frafol.cleanstaffchat.velocity.CleanStaffChat;
import it.frafol.cleanstaffchat.velocity.enums.VelocityConfig;
import it.frafol.cleanstaffchat.velocity.enums.VelocityMessages;
import it.frafol.cleanstaffchat.velocity.enums.VelocityRedis;
import it.frafol.cleanstaffchat.velocity.objects.Placeholder;
import it.frafol.cleanstaffchat.velocity.objects.PlayerCache;
import it.frafol.cleanstaffchat.velocity.utils.ChatUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class AFKCommand implements SimpleCommand {

    public final CleanStaffChat PLUGIN;

    public AFKCommand(CleanStaffChat plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public void execute(SimpleCommand.Invocation invocation) {

        CommandSource commandSource = invocation.source();

        if (!(commandSource instanceof Player)) {
            VelocityMessages.PLAYER_ONLY.send(commandSource, new Placeholder("prefix", VelocityMessages.PREFIX.color()));
            return;
        }

        if (!VelocityConfig.STAFFCHAT_AFK_MODULE.get(Boolean.class)) {

            VelocityMessages.MODULE_DISABLED.send(commandSource, new Placeholder("prefix", VelocityMessages.PREFIX.color()));

            return;

        }

        if (!commandSource.hasPermission(VelocityConfig.STAFFCHAT_AFK_PERMISSION.get(String.class))) {

            VelocityMessages.NO_PERMISSION.send(commandSource, new Placeholder("prefix", VelocityMessages.PREFIX.color()));

            return;

        }

        if (!((Player) commandSource).getCurrentServer().isPresent()) {

            return;

        }

        if (!PlayerCache.getAfk().contains(((Player) commandSource).getUniqueId())) {
            if (PLUGIN.getServer().getPluginManager().isLoaded("luckperms")) {

                final LuckPerms api = LuckPermsProvider.get();

                final User user = api.getUserManager().getUser(((Player) commandSource).getUniqueId());

                if (user == null) {
                            return;
                        }
                final String prefix = user.getCachedData().getMetaData().getPrefix();
                final String suffix = user.getCachedData().getMetaData().getSuffix();
                final String user_prefix = prefix == null ? "" : prefix;
                final String user_suffix = suffix == null ? "" : suffix;

                if (PLUGIN.getServer().getPluginManager().isLoaded("redisbungee") && VelocityRedis.REDIS_ENABLE.get(Boolean.class)) {

                    final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

                    final String final_message = VelocityMessages.STAFFCHAT_AFK_ON.get(String.class)
                            .replace("%user%", ((Player) commandSource).getUsername())
                            .replace("%displayname%", ChatUtil.translateHex(user_prefix) + commandSource + ChatUtil.translateHex(user_suffix))
                            .replace("%userprefix%", ChatUtil.translateHex(user_prefix))
                            .replace("%usersuffix%", ChatUtil.translateHex(user_suffix))
                            .replace("%server%", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName())
                            .replace("%prefix%", VelocityMessages.PREFIX.color())
                            .replace("&", "§");

                    redisBungeeAPI.sendChannelMessage("CleanStaffChat-StaffAFKMessage-RedisBungee", final_message);

                    return;

                }

                CleanStaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> VelocityMessages.STAFFCHAT_AFK_ON.send(players,
                                new Placeholder("user", ((Player) commandSource).getUsername()),
                                new Placeholder("displayname", ChatUtil.translateHex(user_prefix) + commandSource + ChatUtil.translateHex(user_suffix)),
                                new Placeholder("userprefix", ChatUtil.translateHex(user_prefix)),
                                new Placeholder("usersuffix", ChatUtil.translateHex(user_suffix)),
                                new Placeholder("server", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName()),
                                new Placeholder("prefix", VelocityMessages.PREFIX.color())));

            } else {

                if (PLUGIN.getServer().getPluginManager().isLoaded("redisbungee") && VelocityRedis.REDIS_ENABLE.get(Boolean.class)) {

                    final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

                    final String final_message = VelocityMessages.STAFFCHAT_AFK_ON.get(String.class)
                            .replace("%user%", ((Player) commandSource).getUsername())
                            .replace("%displayname%", ((Player) commandSource).getUsername())
                            .replace("%userprefix%", "")
                            .replace("%usersuffix%", "")
                            .replace("%server%", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName())
                            .replace("%prefix%", VelocityMessages.PREFIX.color())
                            .replace("&", "§");

                    redisBungeeAPI.sendChannelMessage("CleanStaffChat-StaffAFKMessage-RedisBungee", final_message);

                    return;

                }

                CleanStaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> VelocityMessages.STAFFCHAT_AFK_ON.send(players,
                                new Placeholder("user", ((Player) commandSource).getUsername()),
                                new Placeholder("displayname", ((Player) commandSource).getUsername()),
                                new Placeholder("userprefix", ""),
                                new Placeholder("usersuffix", ""),
                                new Placeholder("server", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName()),
                                new Placeholder("prefix", VelocityMessages.PREFIX.color())));

            }

            PlayerCache.getAfk().add(((Player) commandSource).getUniqueId());

        } else {

            if (PLUGIN.getServer().getPluginManager().isLoaded("luckperms")) {

                final LuckPerms api = LuckPermsProvider.get();

                final User user = api.getUserManager().getUser(((Player) commandSource).getUniqueId());

                if (user == null) {
                            return;
                        }
                final String prefix = user.getCachedData().getMetaData().getPrefix();
                final String suffix = user.getCachedData().getMetaData().getSuffix();
                final String user_prefix = prefix == null ? "" : prefix;
                final String user_suffix = suffix == null ? "" : suffix;

                if (PLUGIN.getServer().getPluginManager().isLoaded("redisbungee") && VelocityRedis.REDIS_ENABLE.get(Boolean.class)) {

                    final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

                    final String final_message = VelocityMessages.STAFFCHAT_AFK_OFF.get(String.class)
                            .replace("%user%", ((Player) commandSource).getUsername())
                            .replace("%displayname%", ChatUtil.translateHex(user_prefix) + commandSource + ChatUtil.translateHex(user_suffix))
                            .replace("%userprefix%", ChatUtil.translateHex(user_prefix))
                            .replace("%usersuffix%", ChatUtil.translateHex(user_suffix))
                            .replace("%server%", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName())
                            .replace("%prefix%", VelocityMessages.PREFIX.color())
                            .replace("&", "§");

                    redisBungeeAPI.sendChannelMessage("CleanStaffChat-StaffAFKMessage-RedisBungee", final_message);

                    return;

                }

                CleanStaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                (players -> players.hasPermission(VelocityConfig.STAFFCHAT_AFK_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> VelocityMessages.STAFFCHAT_AFK_OFF.send(players,
                                new Placeholder("user", ((Player) commandSource).getUsername()),
                                new Placeholder("displayname", ChatUtil.translateHex(user_prefix) + commandSource + ChatUtil.translateHex(user_suffix)),
                                new Placeholder("userprefix", ChatUtil.translateHex(user_prefix)),
                                new Placeholder("usersuffix", ChatUtil.translateHex(user_suffix)),
                                new Placeholder("server", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName()),
                                new Placeholder("prefix", VelocityMessages.PREFIX.color())));

            } else {

                if (PLUGIN.getServer().getPluginManager().isLoaded("redisbungee") && VelocityRedis.REDIS_ENABLE.get(Boolean.class)) {

                    final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

                    final String final_message = VelocityMessages.STAFFCHAT_AFK_OFF.get(String.class)
                            .replace("%user%", ((Player) commandSource).getUsername())
                            .replace("%displayname%", ((Player) commandSource).getUsername())
                            .replace("%userprefix%", "")
                            .replace("%usersuffix%", "")
                            .replace("%server%", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName())
                            .replace("%prefix%", VelocityMessages.PREFIX.color())
                            .replace("&", "§");

                    redisBungeeAPI.sendChannelMessage("CleanStaffChat-StaffAFKMessage-RedisBungee", final_message);

                    return;

                }

                CleanStaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                (players -> players.hasPermission(VelocityConfig.STAFFCHAT_AFK_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> VelocityMessages.STAFFCHAT_AFK_OFF.send(players,
                                new Placeholder("user", ((Player) commandSource).getUsername()),
                                new Placeholder("displayname", ((Player) commandSource).getUsername()),
                                new Placeholder("userprefix", ""),
                                new Placeholder("usersuffix", ""),
                                new Placeholder("server", ((Player) commandSource).getCurrentServer().get().getServer().getServerInfo().getName()),
                                new Placeholder("prefix", VelocityMessages.PREFIX.color())));

            }

            PlayerCache.getAfk().remove(((Player) commandSource).getUniqueId());

        }
    }
}
