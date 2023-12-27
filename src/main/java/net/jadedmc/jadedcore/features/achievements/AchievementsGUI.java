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
import net.jadedmc.jadedcore.features.player.JadedPlayer;
import net.jadedmc.jadedcore.features.player.ProfileGUI;
import net.jadedmc.jadedcore.utils.MathUtils;
import net.jadedmc.jadedcore.utils.gui.CustomGUI;
import net.jadedmc.jadedcore.utils.items.ItemBuilder;
import net.jadedmc.jadedcore.utils.items.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs teh AchievementsGUI, which shows the player all existing achievements.
 */
public class AchievementsGUI extends CustomGUI {
    private final JadedCorePlugin plugin;

    /**
     * Creates the main GUI, displaying all the available games.
     * @param plugin Instance of the plugin.
     * @param player Player to view achievements of.
     */
    public AchievementsGUI(final JadedCorePlugin plugin, Player player) {
        super(54, "Achievements");
        this.plugin = plugin;

        addFiller(1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        Game[] games = new Game[]{Game.GENERAL, Game.CACTUS_RUSH, Game.ELYTRAPVP, Game.HOUSING};
        int[] gameSlots = new int[]{19,20,21,22,23,24,25,28,29,30,31,32,33,34};

        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .asItemBuilder()
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> new ProfileGUI(plugin, p).open(p));

        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        int i = 0;
        for(Game game : games) {
            List<Achievement> totalAchievements = plugin.achievementManager().getAchievements(game);
            List<Achievement> playerAchievements = new ArrayList<>();

            int playerPoints = 0;

            for(Achievement achievement : jadedPlayer.getAchievements()) {
                if(achievement.getGame() != game) {
                    continue;
                }

                playerAchievements.add(achievement);
                playerPoints += achievement.getPoints();
            }

            int totalPoints = 0;

            for(Achievement achievement : totalAchievements) {
                totalPoints += achievement.getPoints();
            }

            ItemStack item = new ItemBuilder(game.getIconMaterial().parseMaterial())
                    .setDisplayName("<green>" + game.getName())
                    .addLore("<gray>Unlocked: <green>" + playerAchievements.size() + "<gray>/<green>" + totalAchievements.size() + " <dark_gray>(" + MathUtils.percent(playerAchievements.size(), totalAchievements.size()) + "%)")
                    .addLore("<gray>Points: <yellow>" + playerPoints + "<gray>/<yellow>" + totalPoints + " <dark_gray>(" + MathUtils.percent(playerPoints, totalPoints) + "%)")
                    .addLore("")
                    .addLore("&aClick to view achievements!")
                    .build();
            setItem(gameSlots[i], item, (p,a) -> new AchievementsGUI(plugin, p, game).open(p));
            i++;
        }
    }

    /**
     * Creates a game-specific GUI, showing all achievements in that game.
     * @param plugin Instance of the plugin.
     * @param player Player to get achievements of.
     * @param game Game to get achievements of.
     */
    public AchievementsGUI(final JadedCorePlugin plugin, Player player, Game game) {
        super(54, "Achievements - " + game.getName());
        this.plugin = plugin;

        addFiller(1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);
        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .asItemBuilder()
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> new AchievementsGUI(plugin, p).open(p));

        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        int i = 9;
        for(Achievement achievement : plugin.achievementManager().getAchievements(game)) {

            if(jadedPlayer.getAchievements().contains(achievement)) {
                ItemBuilder builder = new ItemBuilder(Material.DIAMOND, achievement.getPoints())
                        .setDisplayName("<green>" + achievement.getName())
                        .addLore("<gray>" + achievement.getDescription())
                        .addLore("")
                        .addLore("<gray>Rewards:");

                for(String reward : achievement.getRewards()) {
                    builder.addLore("  " + reward);
                }
                setItem(i, builder.build());
            }
            else {
                ItemBuilder builder = new ItemBuilder(Material.COAL, achievement.getPoints())
                        .setDisplayName("<red>" + achievement.getName())
                        .addLore("<gray>" + achievement.getDescription())
                        .addLore("")
                        .addLore("<gray>Rewards:");

                for(String reward : achievement.getRewards()) {
                    builder.addLore("  " + reward);
                }

                setItem(i, builder.build());
            }

            i++;
        }
    }
}