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
package net.jadedmc.jadedcore;

import net.jadedmc.jadedcore.command.AbstractCommand;
import net.jadedmc.jadedcore.features.achievements.AchievementManager;
import net.jadedmc.jadedcore.features.leaderboards.LeaderboardManager;
import net.jadedmc.jadedcore.features.player.JadedPlayerManager;
import net.jadedmc.jadedcore.listeners.*;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.gui.GUIListeners;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedCorePlugin extends JavaPlugin {
    private AchievementManager achievementManager;
    private HookManager hookManager;
    private JadedPlayerManager jadedPlayerManager;
    private LeaderboardManager leaderboardManager;
    private SettingsManager settingsManager;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        // Set up JadedAPI
        new JadedAPI(this);

        // Initialize an audiences instance for the plugin
        BukkitAudiences adventure = BukkitAudiences.create(this);
        ChatUtils.setAdventure(adventure);

        // Load plugin settings.
        settingsManager = new SettingsManager(this);

        // Connect to MySQL
        mySQL = new MySQL(this);
        mySQL.openConnection();

        jadedPlayerManager = new JadedPlayerManager(this);
        achievementManager = new AchievementManager(this);
        hookManager = new HookManager(this);
        leaderboardManager = new LeaderboardManager(this);

        AbstractCommand.registerCommands(this);
        registerListeners();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        new Placeholders(this).register();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GUIListeners(), this);
        getServer().getPluginManager().registerEvents(new ChannelMessageSendListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        new UserDataRecalculateListener(this, LuckPermsProvider.get());
    }

    public AchievementManager achievementManager() {
        return achievementManager;
    }

    public HookManager hookManager() {
        return hookManager;
    }

    public JadedPlayerManager jadedPlayerManager() {
        return jadedPlayerManager;
    }

    /**
     * Get the Leaderboard Manager, which controls leaderboards.
     * @return Leaderboard Manager.
     */
    public LeaderboardManager leaderboardManager() {
        return leaderboardManager;
    }

    public MySQL mySQL() {
        return mySQL;
    }

    public SettingsManager settingsManager() {
        return settingsManager;
    }
}