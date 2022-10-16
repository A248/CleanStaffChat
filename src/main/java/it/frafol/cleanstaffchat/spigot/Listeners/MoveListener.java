package it.frafol.cleanstaffchat.spigot.Listeners;

import it.frafol.cleanstaffchat.spigot.CleanStaffChat;
import it.frafol.cleanstaffchat.spigot.enums.SpigotConfig;
import it.frafol.cleanstaffchat.spigot.objects.PlayerCache;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    public final CleanStaffChat PLUGIN;

    public MoveListener(CleanStaffChat plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        final Player player = event.getPlayer();

        if (!player.hasPermission(SpigotConfig.STAFFCHAT_AFK_PERMISSION.get(String.class))) {

            return;

        }

        if (!SpigotConfig.STAFFCHAT_NO_AFK_ONCHANGE_SERVER.get(Boolean.class)) {

            return;

        }

        if (PlayerCache.getAfk().contains(player.getUniqueId())) {

            if (Bukkit.getServer().getPluginManager().getPlugin("LuckPerms") != null) {

                final LuckPerms api = LuckPermsProvider.get();

                final User user = api.getUserManager().getUser(player.getUniqueId());

                assert user != null;
                final String prefix = user.getCachedData().getMetaData().getPrefix();
                final String suffix = user.getCachedData().getMetaData().getSuffix();
                final String user_prefix = prefix == null ? "" : prefix;
                final String user_suffix = suffix == null ? "" : suffix;

                CleanStaffChat.getInstance().getServer().getOnlinePlayers().stream().filter
                                (players -> players.hasPermission(SpigotConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> players.sendMessage(SpigotConfig.STAFFCHAT_AFK_OFF.color()
                                .replace("%prefix%", SpigotConfig.PREFIX.color())
                                .replace("%user%", player.getName())
                                .replace("%displayname%", user_prefix + player.getName() + user_suffix)
                                .replace("%userprefix%", user_prefix)
                                .replace("%usersuffix%", user_suffix)));

            } else {

                CleanStaffChat.getInstance().getServer().getOnlinePlayers().stream().filter
                                (players -> players.hasPermission(SpigotConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> players.sendMessage(SpigotConfig.STAFFCHAT_AFK_OFF.color()
                                .replace("%prefix%", SpigotConfig.PREFIX.color())
                                .replace("%user%", player.getName())
                                .replace("%userprefix%", "")
                                .replace("%usersuffix%", "")
                                .replace("%displayname%", player.getName())));

            }

            PlayerCache.getAfk().remove(player.getUniqueId());

        }
    }
}
