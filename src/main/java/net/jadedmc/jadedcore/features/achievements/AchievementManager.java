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
package net.jadedmc.jadedcore.features.achievements;

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.features.games.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the creation and access of Achievements.
 */
public class AchievementManager {
    private final JadedCorePlugin plugin;
    private final Map<String, Achievement> achievements = new LinkedHashMap<>();

    /**
     * Creates the Achievement Manager.
     * @param plugin Instance of the plugin.
     */
    public AchievementManager(final JadedCorePlugin plugin) {
        this.plugin = plugin;

        createAchievement(Game.GENERAL, "general_3", "Am I in Trouble?", "Beat a staff member in a game.", 5);
        createAchievement(Game.GENERAL, "general_1", "A Whole New World", "Join the server for the first time.", 5);
        createAchievement(Game.GENERAL, "general_4", "Better With Friends", "Create or join a party with other players.", 5);
        createAchievement(Game.GENERAL, "general_2", "Let My Voice Be Heard!", "Use chat for the first time.", 5);
    }

    /**
     * Loads all achievements from MySQL.
     */
    public void loadAchievements() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM achievements_list");
                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    achievements.put(resultSet.getString("id"), new Achievement(plugin, Game.valueOf(resultSet.getString("mode")), resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getInt("achievementPoints"), resultSet.getString("rewards")));
                }
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Creates an Achievement.
     * @param game Game the achievement is for.
     * @param id ID of the achievement.
     * @param name Name of the Achievement.
     * @param description Description of the achievement.
     * @param points Number of Achievement Points that should be awarded.
     * @param rewards Other rewards.
     */
    public void createAchievement(final Game game, final String id, final String name, final String description, final int points, String... rewards) {

        StringBuilder rewardsString = new StringBuilder();

        for(int i = 0; i < rewards.length; i++) {
            if(i != 0) {
                rewardsString.append(";");
            }

            rewardsString.append(rewards[i]);
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO achievements_list (id,mode,name,description,achievementPoints,rewards) VALUES (?,?,?,?,?,?)");
                statement.setString(1, id);
                statement.setString(2, game.toString());
                statement.setString(3, name);
                statement.setString(4, description);
                statement.setInt(5, points);
                statement.setString(6, rewardsString.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        achievements.put(id, new Achievement(plugin, game, id, name, description, points, rewardsString.toString()));
    }

    /**
     * Get all achievements from a given game.
     * @param game Game to get achievements from.
     * @return List of achievements in that game.
     */
    public List<Achievement> getAchievements(Game game) {
        List<Achievement> gameAchievements = new ArrayList<>();

        for(Achievement achievement : achievements.values()) {
            if(achievement.getGame() == game) {
                gameAchievements.add(achievement);
            }
        }

        return gameAchievements;
    }

    /**
     * Get an achievement from its id.
     * @param id ID of the achievement.
     * @return Resulting achievement.
     */
    public Achievement getAchievement(String id) {
        if(achievements.containsKey(id)) {
            return achievements.get(id);
        }

        return null;
    }
}