/*
 * Copyright (C) 2018-2021 Leo3418 <https://github.com/Leo3418>
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

package io.github.leo3418.hbwhelper;

import io.github.leo3418.hbwhelper.game.DreamMode;
import io.github.leo3418.hbwhelper.gui.ConfigGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration manager of this mod, which reads from and writes to this mod's
 * configuration file.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 * @see ConfigGuiFactory
 */
public class ConfigManager {
    /**
     * Default width from the left edge of the Minecraft window to the left
     * edge of {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private static final int DEFAULT_HUD_X = 2;

    /**
     * Default height from the top edge of the Minecraft window to the top edge
     * of {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private static final int DEFAULT_HUD_Y = 2;

    /**
     * The only instance of this class
     */
    private static final ConfigManager INSTANCE = new ConfigManager();

    /**
     * Configuration file of this mod
     */
    private Configuration config;

    /**
     * The {@link Property} object storing whether diamond and emerald
     * generation times should be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private Property showGenerationTimes;

    /**
     * The {@link Property} object storing whether team upgrades should be
     * shown on {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private Property showTeamUpgrades;

    /**
     * The {@link Property} object storing whether armor information should be
     * shown on {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private Property showArmorInfo;

    /**
     * The {@link Property} object storing whether effects information should
     * be shown on {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private Property showEffectsInfo;

    /**
     * The {@link Property} object storing whether status effects
     * should always be shown on {@link io.github.leo3418.hbwhelper.gui.GuiHud
     * GuiHud}
     */
    private Property alwaysShowEffects;

    /**
     * The {@link Property} object storing width from the left edge of the
     * Minecraft window to the left edge of
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private Property hudX;

    /**
     * The {@link Property} object storing height from the top edge of the
     * Minecraft window to the top edge of
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}
     */
    private Property hudY;

    /**
     * The {@link Property} object storing the current game for the Dream mode
     * on Hypixel
     */
    private Property currentDreamMode;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private ConfigManager() {
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes configuration when Minecraft Forge is starting.
     * <p>
     * This method should be called when a {@link FMLPreInitializationEvent} is
     * fired.
     *
     * @param event the event fired before Minecraft Forge's initialization
     */
    void initConfig(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        initConfig();
    }

    /**
     * Returns whether diamond and emerald generation times should be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return whether diamond and emerald generation times should be shown on
     *         {@code GuiHud}
     */
    public boolean showGenerationTimes() {
        return showGenerationTimes.getBoolean();
    }

    /**
     * Returns whether team upgrades should be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return whether team upgrades should be shown on {@code GuiHud}
     */
    public boolean showTeamUpgrades() {
        return showTeamUpgrades.getBoolean();
    }

    /**
     * Returns whether armor information should be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return whether armor information should be shown on {@code GuiHud}
     */
    public boolean showArmorInfo() {
        return showArmorInfo.getBoolean();
    }

    /**
     * Returns whether effects information should be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return whether effects information should be shown on {@code GuiHud}
     */
    public boolean showEffectsInfo() {
        return showEffectsInfo.getBoolean();
    }

    /**
     * Returns whether status effects should always be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return whether status effects should always be shown on {@code GuiHud}
     */
    public boolean alwaysShowEffects() {
        return alwaysShowEffects.getBoolean();
    }

    /**
     * Returns width from the left edge of the Minecraft window to the left
     * edge of {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return width from the left edge of the Minecraft window to the left
     *         edge of {@code GuiHud}.
     */
    public int hudX() {
        return hudX.getInt();
    }

    /**
     * Returns height from the top edge of the Minecraft window to the top
     * edge of {@link io.github.leo3418.hbwhelper.gui.GuiHud GuiHud}.
     *
     * @return height from the top edge of the Minecraft window to the top
     *         edge of {@code GuiHud}.
     */
    public int hudY() {
        return hudY.getInt();
    }

    /**
     * Returns the current game for the Dream mode on Hypixel.
     *
     * @return the current game for the Dream mode on Hypixel
     */
    public DreamMode currentDreamMode() {
        return DreamMode.valueOfTranslateKey(currentDreamMode.getString())
                .orElse(DreamMode.UNSELECTED);
    }

    /**
     * Returns a {@link List} storing settings elements to be displayed on
     * {@link io.github.leo3418.hbwhelper.gui.ConfigGuiFactory.ConfigGuiScreen
     * ConfigGuiScreen}.
     *
     * @return a {@code List} storing settings elements to be displayed on
     *         {@code ConfigGuiScreen}
     */
    public List<IConfigElement> getConfigElements() {
        List<IConfigElement> configElements = new ArrayList<>();
        configElements.add(new ConfigElement(showGenerationTimes));
        configElements.add(new ConfigElement(showTeamUpgrades));
        configElements.add(new ConfigElement(showArmorInfo));
        configElements.add(new ConfigElement(showEffectsInfo));
        configElements.add(new ConfigElement(alwaysShowEffects));
        configElements.add(new ConfigElement(hudX));
        configElements.add(new ConfigElement(hudY));
        configElements.add(new ConfigElement(currentDreamMode));
        return configElements;
    }

    /**
     * When configuration of this mod changes, save it to the configuration
     * file.
     * <p>
     * This method should be called whenever an
     * {@link ConfigChangedEvent.OnConfigChangedEvent OnConfigChangedEvent}
     * is fired.
     *
     * @param event the event fired when configuration of any mod changes
     */
    void save(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(HbwHelper.MOD_ID)) {
            config.save();
        }
    }

    /**
     * When client opens up this mod's configuration, updates allowed values of
     * HUD parameters, so they reflect the current size of the Minecraft window.
     * <p>
     * This method should be called whenever a {@link GuiOpenEvent} is fired.
     *
     * @param event the event fired when this mod's configuration screen is
     *         opened
     */
    void update(GuiOpenEvent event) {
        if (event.gui instanceof ConfigGuiFactory.ConfigGuiScreen) {
            updateHudParamRanges();
        }
    }

    /**
     * If the configuration file on disk is absent or incomplete, creates or
     * completes the configuration file. Then, loads the configuration file.
     */
    private void initConfig() {
        showArmorInfo = config.get(Configuration.CATEGORY_CLIENT,
                "showArmorInfo",
                true,
                I18n.format("hbwhelper.configGui.showArmorInfo.description"))
                .setLanguageKey("hbwhelper.configGui.showArmorInfo.title");
        showEffectsInfo = config.get(Configuration.CATEGORY_CLIENT,
                "showEffectsInfo",
                true,
                I18n.format("hbwhelper.configGui.showEffectsInfo.description"))
                .setLanguageKey("hbwhelper.configGui.showEffectsInfo.title");
        showGenerationTimes = config.get(Configuration.CATEGORY_CLIENT,
                "showGenerationTimes",
                true,
                I18n.format("hbwhelper.configGui.showGenerationTimes.description"))
                .setLanguageKey("hbwhelper.configGui.showGenerationTimes.title");
        showTeamUpgrades = config.get(Configuration.CATEGORY_CLIENT,
                "showTeamUpgrades",
                true,
                I18n.format("hbwhelper.configGui.showTeamUpgrades.description"))
                .setLanguageKey("hbwhelper.configGui.showTeamUpgrades.title");
        alwaysShowEffects = config.get(Configuration.CATEGORY_CLIENT,
                "alwaysShowEffects",
                false,
                I18n.format("hbwhelper.configGui.alwaysShowEffects.description"))
                .setLanguageKey("hbwhelper.configGui.alwaysShowEffects.title");
        updateHudParamRanges();
        currentDreamMode = config.get(Configuration.CATEGORY_CLIENT,
                "currentDreamMode",
                "hbwhelper.configGui.unselected",
                I18n.format("hbwhelper.configGui.currentDreamMode.description"),
                DreamMode.translateKeys().toArray(new String[0]))
                .setLanguageKey("hbwhelper.configGui.currentDreamMode.title");
        // If the setting for currentDreamMode is no longer valid, resets it
        if (!DreamMode.valueOfTranslateKey(currentDreamMode.getString())
                .isPresent()) {
            currentDreamMode.setToDefault();
        }
        config.save();
    }

    /**
     * Updates allowed values of HUD parameters to let them reflect the current
     * size of the Minecraft window.
     */
    private void updateHudParamRanges() {
        ScaledResolution scaledResolution =
                new ScaledResolution(Minecraft.getMinecraft());
        hudX = config.get(Configuration.CATEGORY_CLIENT,
                "hudX",
                DEFAULT_HUD_X,
                I18n.format("hbwhelper.configGui.hudX.description"),
                0,
                scaledResolution.getScaledWidth())
                .setLanguageKey("hbwhelper.configGui.hudX.title");
        hudY = config.get(Configuration.CATEGORY_CLIENT,
                "hudY",
                DEFAULT_HUD_Y,
                I18n.format("hbwhelper.configGui.hudY.description"),
                0,
                scaledResolution.getScaledHeight())
                .setLanguageKey("hbwhelper.configGui.hudY.title");
    }
}
