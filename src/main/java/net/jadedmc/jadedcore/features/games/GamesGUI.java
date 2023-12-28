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
package net.jadedmc.jadedcore.features.games;

import com.cryptomorin.xseries.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class GamesGUI extends CustomGUI {

    /**
     * Creates the GUI.
     */
    public GamesGUI() {
        super(54, "Games");

        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        addGame(20, Game.CACTUS_RUSH);
        addGame(21, Game.ELYTRAPVP);
        addGame(23, Game.TURFWARS);
        //addGame(24, Game.HOUSING);
        addGame(31, Game.LOBBY);

        ItemStack comingSoon = new ItemBuilder(XMaterial.GRAY_DYE.parseItem())
                .setDisplayName("&c&lComing Soon")
                .build();

        setItem(24, comingSoon);

        ItemStack duels = new ItemBuilder(XMaterial.IRON_SWORD)
                .setDisplayName("&a&lDuels")
                .addLore("")
                .addLore(ChatPaginator.wordWrap(Game.DUELS.getDescription(), 25), "&7")
                .addLore("")
                .addLore("&a▸ Click to Connect")
                .addLore(ChatUtils.translateLegacy(PlaceholderAPI.setPlaceholders(Bukkit.getOnlinePlayers().iterator().next(), "&7Join %math_0_{bungee_modernduels}+{bungee_legacyduels}+{bungee_tournament}% others playing!")))
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        setItem(22, duels, (p, a) -> new DuelsGUI().open(p));
    }

    /**
     * Adds a game to the GUI.
     * @param slot Slot to add game to.
     * @param game Game to add.
     */
    private void addGame(int slot, Game game) {
        setItem(slot, getGameIcon(game), (p, a) -> JadedAPI.sendBungeecordMessage(p, "BungeeCord", "Connect", game.getServer()));
    }

    /**
     * Gets the ItemStack a Game should use in the gui.
     * @param game Game to get ItemStack of.
     * @return ItemStack for the game.
     */
    private ItemStack getGameIcon(Game game) {
        XMaterial material = game.getIconMaterial();

        if(game == Game.LOBBY) {
            return new ItemBuilder(material)
                    .setDisplayName("&a&l" + game.getName())
                    .addLore("")
                    .addLore("&a▸ Click to Connect")
                    .addLore(ChatUtils.translateLegacy(PlaceholderAPI.setPlaceholders(Bukkit.getOnlinePlayers().iterator().next(), "&7Join %bungee_" + game.getServer() + "% others playing!")))
                    .build();
        }

        ItemBuilder builder = new ItemBuilder(material)
                .setDisplayName("&a&l" + game.getName())
                .addLore("&8" + game.getType())
                .addLore("")
                .addLore(ChatPaginator.wordWrap(game.getDescription(), 25), "&7")
                .addLore("")
                .addLore("&a▸ Click to Connect")
                .addLore(ChatUtils.translateLegacy(PlaceholderAPI.setPlaceholders(Bukkit.getOnlinePlayers().iterator().next(), "&7Join %bungee_" + game.getServer() + "% others playing!")));
        return builder.build();
    }
}