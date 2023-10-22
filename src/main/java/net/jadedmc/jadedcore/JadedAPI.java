package net.jadedmc.jadedcore;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.jadedmc.jadedcore.features.player.JadedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class JadedAPI {
    private static JadedCorePlugin plugin = null;

    public JadedAPI(JadedCorePlugin pl) {
        plugin = pl;
    }

    public static Connection getDatabase() {
        return plugin.mySQL().getConnection();
    }

    public static JadedCorePlugin getPlugin() {
        return plugin;
    }


    public static void sendBungeecordMessage(String channel, String subChannel, String message) {

        // Makes sure there is a player online.
        if(Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        // Picks the first player online to send the message to.
        sendBungeecordMessage(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channel, subChannel, message);
    }

    public static void sendBungeecordMessage(Player player, String channel, String subChannel, String message) {

        // Creates the message
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);
        out.writeUTF(message);

        // Sends the message using the first player online.
        player.sendPluginMessage(plugin, channel, out.toByteArray());
    }

    public static JadedPlayer getJadedPlayer(Player player) {
        return plugin.jadedPlayerManager().getPlayer(player);
    }
}