package it.frafol.cleanstaffchat.bukkit.donorchat.commands;

import it.frafol.cleanstaffchat.bukkit.CleanStaffChat;
import it.frafol.cleanstaffchat.bukkit.enums.SpigotConfig;
import it.frafol.cleanstaffchat.bukkit.objects.PlayerCache;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DonorChatCommand implements CommandExecutor {

    public final CleanStaffChat plugin;

    public DonorChatCommand(CleanStaffChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {

        if (!command.getName().equalsIgnoreCase("dc")
                || command.getName().equalsIgnoreCase("donorchat")
                || command.getName().equalsIgnoreCase("donor")) {

            return false;

        }

        if (strings.length == 0) {

            if (!(sender instanceof Player)) {

                sender.sendMessage((SpigotConfig.DONORARGUMENTS.color()
                        .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                return false;

            }

            Player player = (Player) sender;

            if (sender.hasPermission(SpigotConfig.DONORCHAT_USE_PERMISSION.get(String.class))) {

                if (!PlayerCache.getToggled_2_donor().contains(player.getUniqueId())) {

                    if (!(SpigotConfig.DONORCHAT_TALK_MODULE.get(Boolean.class))) {

                        sender.sendMessage((SpigotConfig.DONORARGUMENTS.color()
                                .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                        return false;

                    }

                    if (!PlayerCache.getMuted().contains("true")) {

                        PlayerCache.getToggled_2_donor().add(player.getUniqueId());

                        sender.sendMessage((SpigotConfig.DONORCHAT_TALK_ENABLED.color()
                                .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                    } else {

                        sender.sendMessage((SpigotConfig.DONORARGUMENTS.color()
                                .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                        return false;

                    }

                    return false;

                } else if (PlayerCache.getToggled_2_donor().contains(player.getUniqueId())) {

                    PlayerCache.getToggled_2_donor().remove(player.getUniqueId());

                    sender.sendMessage((SpigotConfig.DONORCHAT_TALK_DISABLED.color()
                            .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                    return false;

                }

            } else {

                sender.sendMessage(("§7This server is using §dCleanStaffChat §7by §dfrafol§7."));

                return false;

            }

            return false;

        }

        String message = String.join(" ", Arrays.copyOfRange(strings, 0, strings.length));

        String commandsender = !(sender instanceof Player) ? SpigotConfig.CONSOLE_PREFIX.get(String.class) :
                sender.getName();

        if (sender.hasPermission(SpigotConfig.DONORCHAT_USE_PERMISSION.get(String.class))) {

            if (!PlayerCache.getMuted().contains("true")) {

                if (sender instanceof Player) {

                    if (SpigotConfig.PREVENT_COLOR_CODES.get(Boolean.class)) {
                        if (message.contains("&0") ||
                                message.contains("&1") ||
                                message.contains("&2") ||
                                message.contains("&3") ||
                                message.contains("&4") ||
                                message.contains("&5") ||
                                message.contains("&6") ||
                                message.contains("&7") ||
                                message.contains("&8") ||
                                message.contains("&9") ||
                                message.contains("&a") ||
                                message.contains("&b") ||
                                message.contains("&c") ||
                                message.contains("&d") ||
                                message.contains("&e") ||
                                message.contains("&f") ||
                                message.contains("&k") ||
                                message.contains("&l") ||
                                message.contains("&m") ||
                                message.contains("&n") ||
                                message.contains("&o") ||
                                message.contains("&r")) {

                            sender.sendMessage(SpigotConfig.COLOR_CODES.color()
                                    .replace("%prefix%", SpigotConfig.DONORPREFIX.color())
                                    .replace("&", "§"));

                            return false;

                        }
                    }

                    if (PlayerCache.getCooldown().contains(((Player) sender).getUniqueId())) {

                        sender.sendMessage(SpigotConfig.DONORCHAT_COOLDOWN_MESSAGE.color()
                                .replace("%prefix%", SpigotConfig.DONORPREFIX.color()));

                        return false;

                    }

                    if (Bukkit.getServer().getPluginManager().getPlugin("LuckPerms") != null) {

                        LuckPerms api = LuckPermsProvider.get();

                        User user = api.getUserManager().getUser(((Player) sender).getUniqueId());
                        assert user != null;
                        final String prefix = user.getCachedData().getMetaData().getPrefix();
                        final String suffix = user.getCachedData().getMetaData().getSuffix();
                        final String user_prefix = prefix == null ? "" : prefix;
                        final String user_suffix = suffix == null ? "" : suffix;

                        CleanStaffChat.getInstance().getServer().getOnlinePlayers().stream().filter
                                        (players -> players.hasPermission(SpigotConfig.DONORCHAT_USE_PERMISSION.get(String.class))
                                                && !(PlayerCache.getToggled_donor().contains(players.getUniqueId())))
                                .forEach(players -> players.sendMessage(SpigotConfig.DONORCHAT_FORMAT.color()
                                        .replace("%prefix%", SpigotConfig.DONORPREFIX.color())
                                        .replace("%user%", commandsender)
                                        .replace("%displayname%", user_prefix + commandsender + user_suffix)
                                        .replace("%message%", message)
                                        .replace("%userprefix%", user_prefix)
                                        .replace("%usersuffix%", user_suffix)
                                        .replace("%server%", "")
                                        .replace("&", "§")));

                    } else {

                        CleanStaffChat.getInstance().getServer().getOnlinePlayers().stream().filter
                                        (players -> players.hasPermission(SpigotConfig.DONORCHAT_USE_PERMISSION.get(String.class))
                                                && !(PlayerCache.getToggled_donor().contains(players.getUniqueId())))
                                .forEach(players -> players.sendMessage(SpigotConfig.DONORCHAT_FORMAT.color()
                                        .replace("%prefix%", SpigotConfig.DONORPREFIX.color())
                                        .replace("%user%", commandsender)
                                        .replace("%userprefix%", "")
                                        .replace("%usersuffix%", "")
                                        .replace("%displayname%", commandsender)
                                        .replace("%message%", message)
                                        .replace("%server%", "")
                                        .replace("&", "§")));

                    }

                    PlayerCache.getCooldown().add(((Player) sender).getUniqueId());

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            PlayerCache.getCooldown().remove(((Player) sender).getUniqueId());
                            cancel();

                        }

                    }.runTaskTimer(plugin, Math.multiplyExact(SpigotConfig.DONOR_TIMER.get(Integer.class), 20), 1);


                } else if (SpigotConfig.CONSOLE_CAN_TALK.get(Boolean.class)) {

                    if (!PlayerCache.getMuted().contains("true")) {

                        CleanStaffChat.getInstance().getServer().getOnlinePlayers().stream().filter
                                        (players -> players.hasPermission(SpigotConfig.DONORCHAT_USE_PERMISSION.get(String.class))
                                                && !(PlayerCache.getToggled_donor().contains(players.getUniqueId())))
                                .forEach(players -> players.sendMessage((SpigotConfig.DONORCHAT_FORMAT.color()
                                        .replace("%prefix%", SpigotConfig.DONORPREFIX.color())
                                        .replace("%user%", commandsender)
                                        .replace("%userprefix%", "")
                                        .replace("%usersuffix%", "")
                                        .replace("%displayname%", commandsender)
                                        .replace("%server%", "")
                                        .replace("%message%", message))));

                    } else {

                        sender.sendMessage((SpigotConfig.DONORCHAT_MUTED_ERROR.color()
                                .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                        return false;

                    }

                    sender.sendMessage((SpigotConfig.DONORCHAT_FORMAT.color()
                            .replace("%prefix%", SpigotConfig.DONORPREFIX.color())
                            .replace("%user%", commandsender)
                            .replace("%displayname%", commandsender)
                            .replace("%userprefix%", "")
                            .replace("%usersuffix%", "")
                            .replace("%server%", "")
                            .replace("%message%", message)));

                    return false;

                } else {

                    sender.sendMessage((SpigotConfig.PLAYER_ONLY.color()
                            .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

                }

                return false;

            } else {

                sender.sendMessage((SpigotConfig.DONORCHAT_MUTED_ERROR.color()
                        .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

            }

            return false;

        } else {

            sender.sendMessage((SpigotConfig.NO_PERMISSION.color()
                    .replace("%prefix%", SpigotConfig.DONORPREFIX.color())));

        }

        return false;

    }
}