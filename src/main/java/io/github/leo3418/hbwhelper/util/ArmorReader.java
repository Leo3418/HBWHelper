/*
 * Copyright (C) 2018-2020 Leo3418 <https://github.com/Leo3418>
 *
 * This file is part of Hypixel Bed Wars Helper (HBW Helper).
 *
 * HBW Helper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HBW Helper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.leo3418.hbwhelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Objects;

/**
 * Provides methods for getting information about the player's armor.
 * <p>
 * In a Bed Wars game, players cannot upgrade their helmet or their chestplate.
 * However, they can purchase better leggings and boots. What is more,
 * enchantment upgrades on armor also reflect on boots. Therefore, we just
 * need to read status from the boots to know the player's armor status in a
 * Bed Wars game. Hence, an alternative name of this class would be
 * {@code BootsReader}.
 * <p>
 * Like some other classes under this package, this class is designed <b>to be
 * used only when the client is in a Minecraft world</b>. Calling some methods
 * when the client is not in a Minecraft world (e.g. in the main menu) might
 * produce {@link NullPointerException}.
 *
 * @author Leo
 */
public class ArmorReader {
    /**
     * Index of boots in the array of the player's armor
     */
    private static final int BOOTS_INDEX = 0;

    /**
     * Prevents instantiation of this class.
     */
    private ArmorReader() {
    }

    /**
     * Returns the level of Protection enchantment on the player's armor, or
     * {@code -1} if the player does not wear armor.
     *
     * @return the level of Protection enchantment on the player's armor, or
     *         {@code -1} if the player does not wear armor
     */
    public static int getProtectionLevel() {
        if (hasArmor()) {
            return EnchantmentHelper.getEnchantmentLevel(
                    Enchantments.PROTECTION, getArmorStack());
        }
        return -1;
    }

    /**
     * Returns whether the player is wearing armor.
     *
     * @return whether the player is wearing armor
     */
    public static boolean hasArmor() {
        return getArmor() != null;
    }

    /**
     * Returns an {@link ItemStack} object which represents the player's armor,
     * or {@code null} if the player does not wear armor.
     *
     * @return an {@code ItemStack} object which represents the player's armor,
     *         or {@code null} if the player does not wear armor
     */
    public static ItemStack getArmorStack() {
        return Objects.requireNonNull(Minecraft.getMinecraft().thePlayer)
                .inventory.armorItemInSlot(BOOTS_INDEX);
    }

    /**
     * Returns the player's armor, or {@code null} if the player does not wear
     * armor.
     *
     * @return the player's armor, or {@code null} if the player does not wear
     *         armor
     */
    @SuppressWarnings("ConstantConditions")
    private static ItemArmor getArmor() {
        ItemStack boots = getArmorStack();
        if (boots != null) {
            Item bootsItem = boots.getItem();
            if (bootsItem instanceof ItemArmor) {
                return (ItemArmor) bootsItem;
            }
        }
        return null;
    }
}
