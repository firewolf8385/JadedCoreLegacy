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

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Makes player skulls easier to create.
 */
@SuppressWarnings("unused")
public class SkullBuilder {
    private final ItemStack itemStack;
    private final SkullMeta skullMeta;

    /**
     * Create a SkullBuilder using a given identifier.
     * Uses Base64.
     * @param base64 Base64 to use.
     */
    public SkullBuilder(String base64) {
        itemStack = new ItemStack(Material.SKULL_ITEM);
        this.itemStack.setDurability((short) 3);

        skullMeta = (SkullMeta) itemStack.getItemMeta();

        // https://www.spigotmc.org/threads/how-to-create-heads-with-custom-base64-texture.352562/
        {
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", base64));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a SkullBuilder using an (Offline)Player.
     * @param offlinePlayer (Offline)Player to use.
     */
    public SkullBuilder(OfflinePlayer offlinePlayer) {
        this.itemStack = new ItemBuilder(XMaterial.PLAYER_HEAD).build();
        this.skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        this.skullMeta.setOwner(offlinePlayer.getName());
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