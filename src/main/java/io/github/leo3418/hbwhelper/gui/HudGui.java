/*
 * Copyright (C) 2018-2020 Leo3418 <https://github.com/Leo3418>
 *
 * This file is part of Hypixel Bed Wars Helper (HBW Helper).
 *
 * HBW Helper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * HBW Helper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Under section 7 of GPL version 3, you are granted additional
 * permissions described in the HBW Helper MC Exception.
 *
 * You should have received a copy of the GNU GPL and a copy of the
 * HBW Helper MC Exception along with this program's source code; see
 * the files LICENSE.txt and LICENSE-MCE.txt respectively.  If not, see
 * <http://www.gnu.org/licenses/> and
 * <https://github.com/Leo3418/HBWHelper>.
 */

package io.github.leo3418.hbwhelper.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.leo3418.hbwhelper.ConfigManager;
import io.github.leo3418.hbwhelper.game.CountedTrap;
import io.github.leo3418.hbwhelper.game.GameManager;
import io.github.leo3418.hbwhelper.util.ArmorReader;
import io.github.leo3418.hbwhelper.util.EffectsReader;
import io.github.leo3418.hbwhelper.util.GameDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.item.Items.*;

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
public class HudGui extends AbstractGui {
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
     * @see <a href="https://minecraft.gamepedia.com/Formatting_codes#Use_in_server.properties_and_pack.mcmeta"
     *         target="_top">Relevant information on Minecraft Wiki</a>
     */
    private static final String FLASH_COLOR_PREFIX = "\u00A7c";

    /**
     * The only instance of this class
     */
    private static final HudGui INSTANCE = new HudGui();

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
    private HudGui() {
        mc = Minecraft.getInstance();
        gameDetector = GameDetector.getInstance();
        configManager = ConfigManager.getInstance();
        currentHeight = configManager.hudY();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static HudGui getInstance() {
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
            currentHeight = configManager.hudY();
        }
    }

    /**
     * Returns whether this GUI should be rendered.
     *
     * @return whether this GUI should be rendered
     */
    private boolean shouldRender() {
        return !(mc.currentScreen instanceof ChatScreen)
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
     * {@link HudGui#WEAR_OUT_THRESHOLD}, the remaining time displayed on this
     * GUI starts to flash.
     */
    private void renderEffectsInfo() {
        if (configManager.showEffectsInfo()) {
            for (EffectInstance potionEffect : EffectsReader.getEffects()) {
                TextureAtlasSprite icon = EffectsReader.getIcon(potionEffect);

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

                drawEffectIconAndString(icon, effectInfo);
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
                drawItemIconAndString(new ItemStack(DIAMOND), nextDiamond);
                drawItemIconAndString(new ItemStack(EMERALD), nextEmerald);
            }

            if (configManager.showTeamUpgrades()) {
                // Level of resource generation speed
                List<ItemStack> itemsForForgeLevels = new ArrayList<>(2);
                itemsForForgeLevels.add(new ItemStack(FURNACE));
                switch (game.getForgeLevel()) {
                    case ORDINARY_FORGE:
                        break;
                    case IRON_FORGE:
                        itemsForForgeLevels.add(new ItemStack(IRON_INGOT));
                        break;
                    case GOLDEN_FORGE:
                        itemsForForgeLevels.add(new ItemStack(GOLD_INGOT));
                        break;
                    case EMERALD_FORGE:
                        itemsForForgeLevels.add(new ItemStack(EMERALD));
                        break;
                    case MOLTEN_FORGE:
                        itemsForForgeLevels.add(new ItemStack(LAVA_BUCKET));
                        break;
                }
                drawItemIcons(itemsForForgeLevels);

                // "Heal Pool" and "Dragon Buff"
                List<ItemStack> itemsForUpgrades = new ArrayList<>();
                if (game.hasHealPool()) {
                    itemsForUpgrades.add(new ItemStack(BEACON));
                }
                if (game.hasDragonBuff()) {
                    itemsForUpgrades.add(new ItemStack(DRAGON_EGG));
                }
                drawItemIcons(itemsForUpgrades);

                // "DeadShot"
                int deadShotLevelInt = game.getDeadShotLevel();
                String deadShotLevelString;
                if (deadShotLevelInt > 0) {
                    deadShotLevelString = Integer.toString(deadShotLevelInt);
                    drawItemIconAndString(new ItemStack(DIAMOND_HOE),
                            deadShotLevelString);
                }

                // Trap queue
                List<ItemStack> itemsForTraps =
                        new ArrayList<>(GameManager.MAX_TRAPS + 1);
                itemsForTraps.add(new ItemStack(LEATHER));
                for (CountedTrap countedTrap : game.getTraps()) {
                    switch (countedTrap.getTrapType()) {
                        case ORDINARY:
                            itemsForTraps.add(new ItemStack(TRIPWIRE_HOOK));
                            break;
                        case COUNTER:
                            itemsForTraps.add(new ItemStack(FEATHER));
                            break;
                        case ALARM:
                            itemsForTraps.add(new ItemStack(REDSTONE_TORCH));
                            break;
                        case MINER_FATIGUE:
                            itemsForTraps.add(new ItemStack(IRON_PICKAXE));
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
     * @param icon the status effect's icon
     * @param text the text to be rendered
     */
    private void drawEffectIconAndString(TextureAtlasSprite icon, String text) {
        mc.getTextureManager()
                .bindTexture(icon.getAtlasTexture().getTextureLocation());
        // Removes black background of the first icon rendered
        RenderSystem.enableBlend();
        blit(configManager.hudX(), currentHeight, this.getBlitOffset(),
                EFFECT_ICON_SIZE, EFFECT_ICON_SIZE, icon);
        drawString(mc.fontRenderer, " " + text,
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
        RenderHelper.enableStandardItemLighting();
        mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack,
                configManager.hudX() + (EFFECT_ICON_SIZE - ITEM_ICON_SIZE) / 2,
                currentHeight);
        RenderHelper.disableStandardItemLighting();
        drawString(mc.fontRenderer, " " + text, ITEM_ICON_SIZE + configManager.hudX(),
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
        RenderHelper.enableStandardItemLighting();
        for (ItemStack itemStack : itemStacks) {
            mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack,
                    currentWidth, currentHeight);
            currentWidth += ITEM_ICON_SIZE + 1;
        }
        RenderHelper.disableStandardItemLighting();
        if (!itemStacks.isEmpty()) {
            currentHeight += ITEM_ICON_SIZE + 1;
        }
    }
}
