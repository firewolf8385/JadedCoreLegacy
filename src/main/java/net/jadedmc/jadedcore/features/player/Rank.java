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

/**
 * Represents a rank in the server.
 */
public enum Rank {
    OWNER("owner", 10, "<red><bold>Owner</bold></red>", "<red>"),
    ADMIN("admin", 9, "<red><bold>Admin</bold></red>", "<red>"),
    MOD("mod", 8, "<gold><bold>Mod</bold></gold>", "<gold>"),
    TRIAL("trial", 7, "<gold><bold>Trial</bold></gold>", "<gold>"),
    BUILDER("builder", 6, "<yellow><bold>Builder</bold></yellow>", "<yellow>"),
    DEVELOPER("developer", 5, "<yellow><bold>Developer</bold></yellow>", "<yellow>"),
    YOUTUBE("youtube", 4, "<bold><red>You</red><white>Tube</white></bold>", "<gray>"),
    JADED("jaded", 3, "<green><bold>Jaded</bold></green>", "<gray>"),
    SAPPHIRE("sapphire", 2, "<blue><bold>Sapphire</bold></blue>", "<gray>"),
    AMETHYST("amethyst", 1, "<dark_purple><bold>Amethyst</bold></dark_purple>", "<gray>"),
    DEFAULT("default", 0, "<gray>Default</gray>", "<gray>");

    private final String name;
    private final String displayName;
    private final String rankColor;
    private final int weight;

    /**
     * Creates the rank.
     * @param name Name of the rank.
     * @param weight Weight of the rank.
     * @param displayName Display name of the rank.
     * @param rankColor Chat color of the rank.
     */
    Rank(final String name, final int weight, final String displayName, final String rankColor) {
        this.name = name;
        this.weight = weight;
        this.displayName = displayName;
        this.rankColor = rankColor;
    }

    /**
     * Get a rank from its name.
     * @param name Name of the rank.
     * @return Resulting rank.
     */
    public static Rank fromName(String name) {
        return Rank.valueOf(name.toUpperCase());
    }

    /**
     * Gets the rank's color.
     * @return Color of the rank.
     */
    public String getRankColor() {
        return rankColor;
    }

    /**
     * Gets the chat prefix of the rank,
     * @return Rank chat prefix.
     */
    public String getChatPrefix() {
        if(weight == 0) {
            return "";
        }

        return displayName + " ";
    }

    /**
     * Gets the display name of the rank.
     * @return Rank display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the name of the rank.
     * @return Rank's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the weight of the rank.
     * @return Rank's weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get whether the rank is a Staff Rank.
     * @return If the rank is a staff rank.
     */
    public boolean isStaffRank() {
        return weight >= 5;
    }
}