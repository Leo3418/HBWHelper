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

import io.github.leo3418.hbwhelper.ConfigManager;
import io.github.leo3418.hbwhelper.util.ArmorReader;
import io.github.leo3418.hbwhelper.util.EffectsReader;
import io.github.leo3418.hbwhelper.util.GameDetector;
import io.github.leo3418.hbwhelper.util.GameManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
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
public class GuiHud extends Gui {
    /**
     * The color of text displayed on this GUI
     */
    private static final int TEXT_COLOR = 0xFFFFFF;

    /**
     * Height of a line of text on this GUI
     */
    private static final int LINE_HEIGHT = 10;

    /**
     * Height of icon of an item
     */
    private static final int ITEM_ICON_SIZE = 16;

    /**
     * Height of icon of a status effect
     */
    private static final int EFFECT_ICON_SIZE = 18;

    /**
     * Threshold of a status effect's remaining time in seconds that if the
     * time is shorter than this value, the remaining time displayed starts
     * flashing
     */
    private static final int WEAR_OUT_THRESHOLD = 5;

    /**
     * Time between color switching in milliseconds when a status effect's
     * remaining time flashes
     */
    private static final int FLASH_INTERVAL = 500;

    /**
     * The color code which changes the color of a status effect's remaining
     * time displayed when flashing
     * <p>
     * The Unicode encoding for the section sign ({@code §}) must be used in
     * place of the section sign because Minecraft does not permit directly
     * using the section sign in most places in-game.
     *
     * @see <a href="https://minecraft.gamepedia.com/Formatting_codes"
     *         target="_top">Color codes in Minecraft</a>
     * @see <a href="https://en.wikipedia.org/wiki/Section_sign">
     *         The section sign's information on Wikipedia</a>
     */
    private static final String FLASH_COLOR_PREFIX = "\u00A7c";

    /**
     * The instance of Minecraft client
     */
    private final Minecraft mc;

    /**
     * The {@link GameDetector} instance
     */
    private final GameDetector gameDetector;

    /**
     * The {@link ConfigManager} of this mod
     */
    private final ConfigManager configManager;

    /**
     * Height of the next line of text that would be rendered
     */
    private int currentHeight;

    /**
     * Constructs a new instance of this GUI.
     */
    public GuiHud() {
        mc = Minecraft.getMinecraft();
        gameDetector = GameDetector.getInstance();
        configManager = ConfigManager.getInstance();
        currentHeight = configManager.getHudY();
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
        just pick the hotbar as the object we monitor.
        To prevent elements on this mod covering chat box contents and debug
        information, the HUD only renders when neither chat screen nor debug
        screen shows.
         */
        if (shouldRender() && event.getType() ==
                RenderGameOverlayEvent.ElementType.HOTBAR) {
            if (gameDetector.isIn()) {
                renderGameInfo();
                renderArmorInfo();
                renderEffectsInfo();
            } else if (configManager.alwaysShowEffects()) {
                renderEffectsInfo();
            }
            // Resets height of the first line in the next rendering
            currentHeight = configManager.getHudY();
        }
    }

    /**
     * Returns whether this GUI should be rendered.
     *
     * @return whether this GUI should be rendered
     */
    private boolean shouldRender() {
        return !(mc.currentScreen instanceof GuiChat)
                && !mc.gameSettings.showDebugInfo;
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
     * <p>
     * When a status effect's remaining time is lower than
     * {@link GuiHud#WEAR_OUT_THRESHOLD}, the remaining time displayed on this
     * GUI starts to flash.
     */
    private void renderEffectsInfo() {
        for (PotionEffect potionEffect : EffectsReader.getEffects()) {
            int iconIndex = EffectsReader.getIconIndex(potionEffect);
            // The numbers were obtained from Minecraft source code
            int textureX = iconIndex % 8 * EFFECT_ICON_SIZE;
            int textureY = 198 + iconIndex / 8 * EFFECT_ICON_SIZE;
            int amplifier = EffectsReader.getDisplayedAmplifier(potionEffect);
            String effectInfo = "";
            if (amplifier > 1) {
                effectInfo += amplifier + " ";
            }
            int duration = EffectsReader.getDuration(potionEffect);
            String displayedDuration =
                    EffectsReader.getDisplayedDuration(potionEffect);
            if (duration == 0 || (duration > 0 && duration <= WEAR_OUT_THRESHOLD
                    && System.currentTimeMillis() % (FLASH_INTERVAL * 2)
                    < FLASH_INTERVAL)) {
                displayedDuration = FLASH_COLOR_PREFIX + displayedDuration
                        + "\u00A7r";
            }
            effectInfo += displayedDuration;
            drawIconAndString(GuiContainer.INVENTORY_BACKGROUND, textureX,
                    textureY, EFFECT_ICON_SIZE, EFFECT_ICON_SIZE, effectInfo);
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
     * @param width the width of the icon
     * @param height the height of the icon
     * @param text the text to be rendered
     */
    private void drawIconAndString(ResourceLocation texture, int textureX,
                                   int textureY, int width, int height,
                                   String text) {
        mc.getTextureManager().bindTexture(texture);
        // Removes black background of the first icon rendered
        GlStateManager.enableBlend();
        drawTexturedModalRect(configManager.getHudX(), currentHeight, textureX,
                textureY, width, height);
        drawString(mc.fontRendererObj, " " + text, width + configManager.getHudX(),
                currentHeight + (height - LINE_HEIGHT) / 2 + 1, TEXT_COLOR);
        currentHeight += height + 1;
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
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack,
                configManager.getHudX() + (EFFECT_ICON_SIZE - ITEM_ICON_SIZE) / 2,
                currentHeight);
        RenderHelper.disableStandardItemLighting();
        drawString(mc.fontRendererObj, " " + text, ITEM_ICON_SIZE + configManager.getHudX(),
                currentHeight + (ITEM_ICON_SIZE - LINE_HEIGHT) / 2 + 1,
                TEXT_COLOR);
        currentHeight += ITEM_ICON_SIZE + 1;
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
        int currentWidth = configManager.getHudX()
                + (EFFECT_ICON_SIZE - ITEM_ICON_SIZE) / 2;
        RenderHelper.enableGUIStandardItemLighting();
        for (ItemStack itemStack : itemStacks) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack,
                    currentWidth, currentHeight);
            currentWidth += ITEM_ICON_SIZE + 1;
        }
        RenderHelper.disableStandardItemLighting();
        if (!itemStacks.isEmpty()) {
            currentHeight += ITEM_ICON_SIZE + 1;
        }
    }
}
