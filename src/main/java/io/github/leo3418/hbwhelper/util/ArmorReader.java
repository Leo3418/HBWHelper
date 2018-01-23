/*
 * HBW Helper: Hypixel Bed Wars Helper Minecraft Forge Mod
 * Copyright (C) 2018 Leo3418
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.leo3418.hbwhelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

/**
 * Provides methods for getting information about the player's armor.
 * <p>
 * In a Bed Wars game, players cannot upgrade their helmet or their chestplate.
 * However, they can purchase better leggings and boots. What is more,
 * enchantment upgrades on armor also reflect on leggings. Therefore, we just
 * need to read status from the leggings to know the player's armor status in a
 * Bed Wars game. Hence, an alternative name of this class would be
 * {@code LeggingsReader}.
 *
 * @author Leo
 */
public class ArmorReader {
    /**
     * Index of leggings in the array of the player's armor
     */
    private static final int LEGGINGS_INDEX = 1;

    /**
     * Prevents instantiation of this class.
     */
    private ArmorReader() {
    }

    /**
     * Returns the material of the player's armor. If the player does not wear
     * armor, returns {@code null}.
     *
     * @return the material of the player's armor, or {@code null} if the player
     *         has no armor
     */
    public static ArmorMaterial getMaterial() {
        ItemArmor armor = getArmor();
        if (armor != null) {
            return armor.getArmorMaterial();
        }
        return null;
    }

    /**
     * Returns the player's armor. If the player does not wear armor, returns
     * {@code null}.
     *
     * @return an {@link ItemArmor} object which represents the player's armor,
     *         or {@code null} if the player has no armor
     */
    private static ItemArmor getArmor() {
        Iterator<ItemStack> armorItr = Minecraft.getMinecraft().thePlayer
                .getArmorInventoryList().iterator();
        for (int i = 0; i < LEGGINGS_INDEX; i++) {
            armorItr.next();
        }
        ItemStack leggings = armorItr.next();
        if (leggings != null) {
            Item leggingsItem = leggings.getItem();
            if (leggingsItem instanceof ItemArmor) {
                return (ItemArmor) leggingsItem;
            }
        }
        return null;
    }
}
