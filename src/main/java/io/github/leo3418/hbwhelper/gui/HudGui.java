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

package io.github.leo3418.hbwhelper.gui;

import io.github.leo3418.hbwhelper.util.ArmorReader;
import io.github.leo3418.hbwhelper.util.EffectsReader;
import io.github.leo3418.hbwhelper.util.GameDetector;
import io.github.leo3418.hbwhelper.util.GameManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The GUI of this mod shown in Minecraft's Head-Up Display (HUD).
 *
 * @author Leo
 * @see <a href="https://minecraft.gamepedia.com/Heads-up_display"
 *         target="_top">Minecraft Wiki's introduction on Minecraft's HUD</a>
 */
public class HudGui extends Gui {
    /**
     * The color of text displayed on this GUI
     */
    private static final int TEXT_COLOR = 0xFFFFFF;

    /**
     * Width from the left edge of the window to the left edge of this GUI
     */
    private static final int BEGINNING_WIDTH = 2;

    /**
     * Height from the top edge of the window to the top edge of this GUI
     */
    private static final int BEGINNING_HEIGHT = 24;

    /**
     * Height of a line of text on this GUI
     */
    private static final int LINE_HEIGHT = 10;

    /**
     * Height of an icon on this GUI
     */
    private static final int ICON_SIZE = 18;

    /**
     * The instance of Minecraft client
     */
    private final Minecraft mc;

    /**
     * Height of the next line of text that would be rendered
     */
    private int currentHeight;

    /**
     * Constructs a new instance of this GUI.
     *
     * @param mc the current Minecraft instance
     */
    public HudGui(Minecraft mc) {
        this.mc = mc;
        currentHeight = BEGINNING_HEIGHT;
    }

    /**
     * When vanilla Minecraft's HUD is rendered, renders this GUI on the HUD.
     *
     * @param event the event called when an element on the HUD is rendered
     */
    public void render(RenderGameOverlayEvent.Post event) {
        /*
        We only need to render this GUI per one object's rendering on the HUD,
        or some vanilla elements on the HUD might not display correctly. We
        just pick the hotbar as the object we monitor. Also, we only want this
        GUI to be displayed when the client is in Bed Wars.
         */
        if (GameDetector.getInstance().isIn()
                && event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            renderArmorInfo();
            renderGameInfo();
            renderEffectsInfo();
            // Resets height of the first line in the next rendering
            currentHeight = BEGINNING_HEIGHT;
        }
    }

    /**
     * Renders the player's armor information on this GUI.
     */
    private void renderArmorInfo() {
        if (ArmorReader.hasArmor()) {
            // If the player has armor, checks its enchantment
            int enchantmentLevel = ArmorReader.getProtectionLevel();
            String level = "";
            if (enchantmentLevel > 0) {
                level += enchantmentLevel;
            }
            drawItemIconAndString(ArmorReader.getArmorStack(), level);
        }
    }

    /**
     * Renders the player's effects information on this GUI.
     */
    private void renderEffectsInfo() {
        for (PotionEffect potionEffect : EffectsReader.getEffects()) {
            int iconIndex = EffectsReader.getIconIndex(potionEffect);
            // The numbers were obtained from Minecraft source code
            int textureX = iconIndex % 8 * ICON_SIZE;
            int textureY = 198 + iconIndex / 8 * ICON_SIZE;
            int amplifier = EffectsReader.getDisplayedAmplifier(potionEffect);
            String effectInfo = "";
            if (amplifier > 1) {
                effectInfo += amplifier + " ";
            }
            effectInfo += EffectsReader.getDuration(potionEffect);
            drawIconAndString(GuiContainer.INVENTORY_BACKGROUND, textureX, textureY,
                    effectInfo);
        }
    }

    /**
     * Renders information of the current game session on this GUI.
     */
    private void renderGameInfo() {
        GameManager game = GameManager.getInstance();
        if (game != null) {
            drawItemIconAndString(new ItemStack(Items.DIAMOND), game.getNextDiamond());
            drawItemIconAndString(new ItemStack(Items.EMERALD), game.getNextEmerald());

            Collection<ItemStack> itemsForForgeLevels = new ArrayList<ItemStack>(2);
            itemsForForgeLevels.add(new ItemStack(Blocks.FURNACE));
            itemsForForgeLevels.add(game.getForgeLevelIcon());
            drawItemIcons(itemsForForgeLevels);

            Collection<ItemStack> itemsForUpgrades = new ArrayList<ItemStack>();
            if (game.hasHealPool()) {
                itemsForUpgrades.add(new ItemStack(Blocks.BEACON));
            }
            if (game.hasDragonBuff()) {
                itemsForUpgrades.add(new ItemStack(Blocks.DRAGON_EGG));
            }
            drawItemIcons(itemsForUpgrades);

            Collection<ItemStack> itemsForTraps =
                    new ArrayList<ItemStack>(GameManager.MAX_TRAPS + 1);
            itemsForTraps.add(new ItemStack(Items.LEATHER));
            Collection<ItemStack> traps = game.getTrapIcons();
            itemsForTraps.addAll(traps);
            drawItemIcons(itemsForTraps);
        }
    }

    /**
     * Renders an icon with a string to its right on this GUI with default
     * parameters.
     * <p>
     * The icon aligns this GUI's left edge, and it is under the previous
     * element on this GUI. The string is in the default color.
     * <p>
     * After this element is rendered, sets height of the next element to be
     * directly below this element.
     *
     * @param texture the texture containing the icon being rendered
     * @param textureX the x-axis of the icon on the texture
     * @param textureY the y-axis of the icon on the texture
     * @param text the text to be rendered
     */
    private void drawIconAndString(ResourceLocation texture, int textureX,
                                   int textureY, String text) {
        mc.getTextureManager().bindTexture(texture);
        // Removes black background of the first icon rendered
        GlStateManager.enableBlend();
        drawTexturedModalRect(BEGINNING_WIDTH, currentHeight, textureX,
                textureY, ICON_SIZE, ICON_SIZE);
        drawString(mc.fontRendererObj, " " + text, ICON_SIZE + BEGINNING_WIDTH,
                currentHeight + (ICON_SIZE - LINE_HEIGHT) / 2 + 1,
                TEXT_COLOR);
        currentHeight += ICON_SIZE;
    }

    /**
     * Renders an icon of an item with a string to its right on this GUI with
     * default parameters.
     * <p>
     * The icon aligns this GUI's left edge, and it is under the previous
     * element on this GUI. The string is in the default color.
     * <p>
     * After this element is rendered, sets height of the next element to be
     * directly below this element.
     *
     * @param itemStack the {@link ItemStack} of the item
     * @param text the text to be rendered
     */
    private void drawItemIconAndString(ItemStack itemStack, String text) {
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack,
                BEGINNING_WIDTH, currentHeight);
        drawString(mc.fontRendererObj, " " + text, ICON_SIZE + BEGINNING_WIDTH,
                currentHeight + (ICON_SIZE - LINE_HEIGHT) / 2 + 1,
                TEXT_COLOR);
        currentHeight += ICON_SIZE;
    }

    /**
     * Renders icons of a collection of items on this GUI in-line with default
     * parameters.
     * <p>
     * The first icon aligns this GUI's left edge. The icons are under the
     * previous element on this GUI.
     * <p>
     * After the icons are rendered, sets height of the next element to be
     * directly below these icons.
     *
     * @param itemStacks the {@link Collection} of {@link ItemStack} of each
     *         item
     */
    private void drawItemIcons(Collection<ItemStack> itemStacks) {
        int currentWidth = BEGINNING_WIDTH;
        for (ItemStack itemStack : itemStacks) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack,
                    currentWidth, currentHeight);
            currentWidth += ICON_SIZE;
        }
        if (!itemStacks.isEmpty()) {
            currentHeight += ICON_SIZE;
        }
    }
}
