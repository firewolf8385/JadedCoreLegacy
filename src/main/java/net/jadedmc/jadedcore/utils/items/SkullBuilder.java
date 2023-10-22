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
package net.jadedmc.jadedcore.utils.items;

import com.cryptomorin.xseries.SkullUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Makes player skulls easier to create.
 */
@SuppressWarnings("unused")
public class SkullBuilder {
    private final ItemStack itemStack;
    private final SkullMeta skullMeta;

    /**
     * Create a SkullBuilder using a given identifier.
     * Can be a uuid, username, url, or base64.
     * @param identifier Identifier.
     */
    public SkullBuilder(String identifier) {
        itemStack = new ItemStack(Material.SKULL_ITEM);
        skullMeta = SkullUtils.applySkin(itemStack.getItemMeta(), identifier);
    }

    /**
     * Create a SkullBuilder using an (Offline)Player.
     * @param offlinePlayer (Offline)Player to use.
     */
    public SkullBuilder(OfflinePlayer offlinePlayer) {
        this(offlinePlayer.getUniqueId().toString());
    }

    /**
     * Convert the SkullBuilder to an ItemBuilder.
     * @return ItemBuilder.
     */
    public ItemBuilder asItemBuilder() {
        itemStack.setItemMeta(skullMeta);
        return new ItemBuilder(itemStack);
    }
}