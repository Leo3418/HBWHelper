/*
 * HBW Helper: Hypixel Bed Wars Helper Minecraft Forge Mod
 * Copyright (C) 2019 Leo3418
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
import io.github.leo3418.hbwhelper.game.CountedTrap;
import io.github.leo3418.hbwhelper.game.GameManager;
import io.github.leo3418.hbwhelper.util.ArmorReader;
import io.github.leo3418.hbwhelper.util.EffectsReader;
import io.github.leo3418.hbwhelper.util.GameDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

/**
 * The GUI of this mod shown in Minecraft's Head-Up Display (HUD).
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 * @see <a href="https://minecraft.gamepedia.com/Heads-up_display"
 *         target="_top">Minecraft Wiki's introduction on Minecraft's HUD</a>
 */
public class GuiHud extends Gui {
    /**
     * Color of text displayed on this GUI
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
     * Color code which changes the color of a status effect's remaining time
     * displayed when flashing
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
     * The only instance of this class
     */
    private static final GuiHud INSTANCE = new GuiHud();

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
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private GuiHud() {
        mc = Minecraft.getMinecraft();
        gameDetector = GameDetector.getInstance();
        configManager = ConfigManager.getInstance();
        currentHeight = configManager.hudY();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static GuiHud getInstance() {
        return INSTANCE;
    }

    /**
     * When vanilla Minecraft's HUD is rendered, renders this GUI on the HUD.
     * <p>
     * This method should be called whenever {@link RenderGameOverlayEvent.Post}
     * is fired.
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
        if (shouldRender() && event.type ==
                RenderGameOverlayEvent.ElementType.HOTBAR) {
            if (gameDetector.isIn()) {
                renderGameInfo();
                renderArmorInfo();
                renderEffectsInfo();
            } else if (configManager.alwaysShowEffects()) {
                renderEffectsInfo();
            }
            // Resets height of the first line in the next rendering
            currentHeight = configManager.hudY();
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
        if (configManager.showArmorInfo() && ArmorReader.hasArmor()) {
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
        if (configManager.showEffectsInfo()) {
            for (PotionEffect potionEffect : EffectsReader.getEffects()) {
                int iconIndex = EffectsReader.getIconIndex(potionEffect);

                String effectInfo = "";
                int amplifier = EffectsReader.getDisplayedAmplifier(potionEffect);
                if (amplifier > 1) {
                    effectInfo += amplifier + " ";
                }
                int duration = EffectsReader.getDuration(potionEffect);
                String displayedDuration =
                        EffectsReader.getDisplayedDuration(potionEffect);
                // Changes color of the remaining time string when the effect
                // is expiring
                if (duration == 0 || (duration > 0 && duration <= WEAR_OUT_THRESHOLD
                        && System.currentTimeMillis() % (FLASH_INTERVAL * 2)
                        < FLASH_INTERVAL)) {
                    displayedDuration = FLASH_COLOR_PREFIX + displayedDuration
                            + "\u00A7r";
                }
                effectInfo += displayedDuration;

                drawEffectIconAndString(iconIndex, effectInfo);
            }
        }
    }

    /**
     * Renders information of the current game session on this GUI.
     */
    private void renderGameInfo() {
        GameManager game = GameManager.getInstance();
        if (game != null) {
            if (configManager.showGenerationTimes()) {
                String nextDiamond;
                if (game.getNextDiamond() != -1) {
                    nextDiamond = game.getNextDiamond() + "s";
                } else {
                    nextDiamond =
                            I18n.format("hbwhelper.hudGui.findingGenerator");
                }
                String nextEmerald;
                if (game.getNextEmerald() != -1) {
                    nextEmerald = game.getNextEmerald() + "s";
                } else {
                    nextEmerald =
                            I18n.format("hbwhelper.hudGui.findingGenerator");
                }
                drawItemIconAndString(new ItemStack(diamond), nextDiamond);
                drawItemIconAndString(new ItemStack(emerald), nextEmerald);
            }

            if (configManager.showTeamUpgrades()) {
                // Level of resource generation speed
                List<ItemStack> itemsForForgeLevels = new ArrayList<ItemStack>(2);
                itemsForForgeLevels.add(new ItemStack(furnace));
                switch (game.getForgeLevel()) {
                    case ORDINARY_FORGE:
                        break;
                    case IRON_FORGE:
                        itemsForForgeLevels.add(new ItemStack(iron_ingot));
                        break;
                    case GOLDEN_FORGE:
                        itemsForForgeLevels.add(new ItemStack(gold_ingot));
                        break;
                    case EMERALD_FORGE:
                        itemsForForgeLevels.add(new ItemStack(emerald));
                        break;
                    case MOLTEN_FORGE:
                        itemsForForgeLevels.add(new ItemStack(lava_bucket));
                        break;
                }
                drawItemIcons(itemsForForgeLevels);

                // Other team upgrades
                List<ItemStack> itemsForUpgrades = new ArrayList<ItemStack>();
                if (game.hasHealPool()) {
                    itemsForUpgrades.add(new ItemStack(beacon));
                }
                if (game.hasDragonBuff()) {
                    itemsForUpgrades.add(new ItemStack(dragon_egg));
                }
                drawItemIcons(itemsForUpgrades);

                // Trap queue
                List<ItemStack> itemsForTraps =
                        new ArrayList<ItemStack>(GameManager.MAX_TRAPS + 1);
                itemsForTraps.add(new ItemStack(leather));
                for (CountedTrap countedTrap : game.getTraps()) {
                    switch (countedTrap.getTrapType()) {
                        case ORDINARY:
                            itemsForTraps.add(new ItemStack(tripwire_hook));
                            break;
                        case COUNTER:
                            itemsForTraps.add(new ItemStack(feather));
                            break;
                        case ALARM:
                            itemsForTraps.add(new ItemStack(redstone_torch));
                            break;
                        case MINER_FATIGUE:
                            itemsForTraps.add(new ItemStack(iron_pickaxe));
                            break;
                    }
                }
                drawItemIcons(itemsForTraps);
            }
        }
    }

    /**
     * Renders icon of a status effect with a string to its right on this GUI.
     * <p>
     * The icon aligns this GUI's left edge, and it is under the previous
     * element on this GUI. The string's color is defined by
     * {@link #TEXT_COLOR}.
     * <p>
     * After this element is rendered, sets height of the next element to be
     * directly below this element.
     *
     * @param iconIndex the index of the status effect's icon
     * @param text the text to be rendered
     */
    private void drawEffectIconAndString(int iconIndex, String text) {
        mc.getTextureManager().bindTexture(
                new ResourceLocation("textures/gui/container/inventory.png"));
        // The numbers were obtained from Minecraft source code
        int textureX = iconIndex % 8 * EFFECT_ICON_SIZE;
        int textureY = 198 + iconIndex / 8 * EFFECT_ICON_SIZE;
        // Removes black background of the first icon rendered
        GlStateManager.enableBlend();
        drawTexturedModalRect(configManager.hudX(), currentHeight, textureX,
                textureY, EFFECT_ICON_SIZE, EFFECT_ICON_SIZE);
        drawString(mc.fontRendererObj, " " + text,
                EFFECT_ICON_SIZE + configManager.hudX(),
                currentHeight + (EFFECT_ICON_SIZE - LINE_HEIGHT) / 2 + 1,
                TEXT_COLOR);
        currentHeight += EFFECT_ICON_SIZE + 1;
    }

    /**
     * Renders an icon of an item with a string to its right on this GUI.
     * <p>
     * The icon aligns this GUI's left edge, and it is under the previous
     * element on this GUI. The string's color is defined by
     * {@link #TEXT_COLOR}.
     * <p>
     * After this element is rendered, sets height of the next element to be
     * directly below this element.
     *
     * @param itemStack the {@link ItemStack} for the item
     * @param text the text to be rendered
     */
    private void drawItemIconAndString(ItemStack itemStack, String text) {
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack,
                configManager.hudX() + (EFFECT_ICON_SIZE - ITEM_ICON_SIZE) / 2,
                currentHeight);
        RenderHelper.disableStandardItemLighting();
        drawString(mc.fontRendererObj, " " + text, ITEM_ICON_SIZE + configManager.hudX(),
                currentHeight + (ITEM_ICON_SIZE - LINE_HEIGHT) / 2 + 1,
                TEXT_COLOR);
        currentHeight += ITEM_ICON_SIZE + 1;
    }

    /**
     * Renders icons of a {@link List} of items on this GUI in a single line.
     * <p>
     * The first icon aligns this GUI's left edge. The icons are under the
     * previous element on this GUI.
     * <p>
     * After the icons are rendered, sets height of the next element to be
     * directly below these icons.
     *
     * @param itemStacks the {@code List} of {@link ItemStack} for each item
     */
    private void drawItemIcons(List<ItemStack> itemStacks) {
        int currentWidth = configManager.hudX()
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
