/*
 * This file is part of JadedCoreLegacy, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedcore.features.player;

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.features.achievements.Achievement;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a Player on the server. Stores plugin-specific data about them.
 * Should only be created async.
 */
public class JadedPlayer {
    private final JadedCorePlugin plugin;
    private final Player player;
    private final Rank rank;

    private boolean spying = false;
    private boolean vanished = false;
    private int experience = 0;
    private int level = 1;
    private Timestamp firstJoined;
    private final Collection<Achievement> achievements = new ArrayList<>();

    /**
     * Creates the JadedPlayer
     * @param plugin Instance of the plugin.
     * @param player Player object to use.
     */
    public JadedPlayer(final JadedCorePlugin plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;

        // Update the player's rank.
        this.rank = Rank.fromName(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup());

        try {

            // Player Info
            {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM player_info where uuid = ? LIMIT 1");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    level = resultSet.getInt("level");
                    experience = resultSet.getInt("experience");
                    firstJoined = resultSet.getTimestamp("firstOnline");
                }
            }

            // Achievements
            {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM player_achievements WHERE uuid = ?");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    Achievement achievement = plugin.achievementManager().getAchievement(resultSet.getString("achievementID"));

                    if(achievement != null) {
                        achievements.add(achievement);
                    }
                }
            }

            // Staff Settings
            {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM staff_settings WHERE uuid = ? LIMIT 1");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet results = statement.executeQuery();

                if(results.next()) {
                    spying = results.getBoolean(3);
                    vanished = results.getBoolean(2);
                }
                else {
                    PreparedStatement statement2 = plugin.mySQL().getConnection().prepareStatement("INSERT INTO staff_settings (uuid) VALUES (?)");
                    statement2.setString(1, player.getUniqueId().toString());
                    statement2.executeUpdate();

                    spying = false;
                    vanished = false;
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public Collection<Achievement> getAchievements() {
        return achievements;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public Timestamp getFirstJoined() {
        return firstJoined;
    }

    /**
     * Get the player data is being stored for.
     * @return Player of the JadedPlayer.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the rank of the player.
     * @return Player's rank.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Check if the player has the permissions of a specific rank.
     * @param toCheck Rank to check.
     * @return Whether they have the permissions of it or not.
     */
    public boolean hasRank(Rank toCheck) {
        return (rank.getWeight() >= toCheck.getWeight());
    }

    /**
     * Get whether the player is a staff member.
     * @return If the player is a staff member.
     */
    public boolean isStaffMember() {
        return rank.isStaffRank();
    }

    /**
     * Get if the player is spying on commands.
     *
     * @return Whether they are spying on the commands.
     */
    public boolean isSpying() {
        return spying;
    }

    /**
     * Get if the player is vanished.
     *
     * @return Whether they are vanished.
     */
    public boolean isVanished() {
        return vanished;
    }

    /**
     * Set if the player is spying on commands.
     *
     * @param spying Whether they are spying on commands.
     */
    public void setSpying(boolean spying) {
        this.spying = spying;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE staff_settings SET commandSpy = ? WHERE uuid = ?");
                statement.setBoolean(1, spying);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set if the player is currently vanished.
     *
     * @param vanished Whether they are vanished.
     */
    public void setVanished(boolean vanished) {
        this.vanished = vanished;

        if (!vanished) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.equals(player)) {
                    continue;
                }

                pl.showPlayer(player);
            }
        } else {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.equals(player)) {
                    continue;
                }

                pl.hidePlayer(player);
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE staff_settings SET vanish = ? WHERE uuid = ?");
                statement.setBoolean(1, vanished);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}