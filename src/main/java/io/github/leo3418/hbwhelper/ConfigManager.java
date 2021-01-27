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

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import io.github.leo3418.hbwhelper.game.DreamMode;
import io.github.leo3418.hbwhelper.gui.HudGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static io.github.leo3418.hbwhelper.HbwHelper.MOD_ID;

/**
 * Configuration manager of this mod, which reads from and writes to this mod's
 * configuration file.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 * <p>
 * The methods that change this mod's configuration do not automatically write
 * those changes to the configuration file on disk. Instead, they only update
 * the configuration in memory. To write any changes, use the {@link #save()}
 * method.
 *
 * @author Leo
 */
public class ConfigManager {
    /**
     * Default width from the left edge of the Minecraft window to the left
     * edge of {@link HudGui HudGui}
     */
    private static final int DEFAULT_HUD_X = 2;

    /**
     * Default height from the top edge of the Minecraft window to the top edge
     * of {@link HudGui HudGui}
     */
    private static final int DEFAULT_HUD_Y = 2;

    /**
     * The only instance of this class
     */
    private static final ConfigManager INSTANCE;

    /**
     * The {@link ForgeConfigSpec} instance for this mod's configuration
     */
    private static final ForgeConfigSpec SPEC;

    /**
     * {@link Path} to the configuration file of this mod
     */
    private static final Path CONFIG_PATH =
            Paths.get("config", MOD_ID + ".toml");

    static {
        Pair<ConfigManager, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(ConfigManager::new);
        INSTANCE = specPair.getLeft();
        SPEC = specPair.getRight();
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH)
                .sync()
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
        SPEC.setConfig(config);
    }

    /**
     * Whether diamond and emerald generation times should be shown on
     * {@link HudGui}
     */
    private final BooleanValue showGenerationTimes;

    /**
     * Whether team upgrades should be shown on {@link HudGui}
     */
    private final BooleanValue showTeamUpgrades;

    /**
     * Whether armor information should be shown on {@link HudGui}
     */
    private final BooleanValue showArmorInfo;

    /**
     * Whether effects information should be shown on {@link HudGui}
     */
    private final BooleanValue showEffectsInfo;

    /**
     * Whether status effects should always be shown on {@link HudGui}
     */
    private final BooleanValue alwaysShowEffects;

    /**
     * Width from the left edge of the Minecraft window to the left edge of
     * {@link HudGui}
     */
    private final IntValue hudX;

    /**
     * Height from the top edge of the Minecraft window to the top edge of
     * {@link HudGui}
     */
    private final IntValue hudY;

    /**
     * The current game for the Dream Mode on Hypixel
     */
    private final EnumValue<DreamMode> currentDreamMode;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
        // Comments are not added because there was no way to translate
        // descriptions from translate keys here

        showGenerationTimes = configSpecBuilder
                .translation("hbwhelper.configGui.showGenerationTimes.title")
                .define("showGenerationTimes", true);
        showTeamUpgrades = configSpecBuilder
                .translation("hbwhelper.configGui.showTeamUpgrades.title")
                .define("showTeamUpgrades", true);
        showArmorInfo = configSpecBuilder
                .translation("hbwhelper.configGui.showArmorInfo.title")
                .define("showArmorInfo", true);
        showEffectsInfo = configSpecBuilder
                .translation("hbwhelper.configGui.showEffectsInfo.title")
                .define("showEffectsInfo", true);
        alwaysShowEffects = configSpecBuilder
                .translation("hbwhelper.configGui.alwaysShowEffects.title")
                .define("alwaysShowEffects", false);

        // The maximum values of HUD-related parameters are determined by the
        // window size and thus can change very often, so we do not set maximum
        // limits here but validate the settings on the run
        hudX = configSpecBuilder
                .translation("hbwhelper.configGui.hudX.title")
                .defineInRange("hudX", DEFAULT_HUD_X, 0, Integer.MAX_VALUE);
        hudY = configSpecBuilder
                .translation("hbwhelper.configGui.hudY.title")
                .defineInRange("hudY", DEFAULT_HUD_Y, 0, Integer.MAX_VALUE);

        currentDreamMode = configSpecBuilder
                .translation("hbwhelper.configGui.currentDreamMode.title")
                .defineEnum("currentDreamMode", DreamMode.UNSELECTED);
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    // Validations

    /**
     * Returns the maximum value permitted for the width from the left edge of
     * the Minecraft window to the left edge of {@link HudGui HudGui}.
     *
     * @return the maximum value permitted for the width from the left edge of
     *         the Minecraft window to the left edge of {@code HudGui}
     */
    public int maxHudX() {
        return Minecraft.getInstance().mainWindow.getScaledWidth();
    }

    /**
     * Returns the maximum value permitted for the height from the top edge of
     * the Minecraft window to the left edge of {@link HudGui HudGui}.
     *
     * @return the maximum value permitted for the height from the top edge of
     *         the Minecraft window to the left edge of {@code HudGui}
     */
    public int maxHudY() {
        return Minecraft.getInstance().mainWindow.getScaledHeight();
    }

    // Query Operations

    /**
     * Returns whether diamond and emerald generation times should be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether diamond and emerald generation times should be shown on
     *         {@code HudGui}
     */
    public boolean showGenerationTimes() {
        return showGenerationTimes.get();
    }

    /**
     * Returns whether team upgrades should be shown on {@link HudGui HudGui}.
     *
     * @return whether team upgrades should be shown on {@code HudGui}
     */
    public boolean showTeamUpgrades() {
        return showTeamUpgrades.get();
    }

    /**
     * Returns whether armor information should be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether armor information should be shown on {@code HudGui}
     */
    public boolean showArmorInfo() {
        return showArmorInfo.get();
    }

    /**
     * Returns whether effects information should be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether effects information should be shown on {@code HudGui}
     */
    public boolean showEffectsInfo() {
        return showEffectsInfo.get();
    }

    /**
     * Returns whether status effects should always be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether status effects should always be shown on {@code HudGui}
     */
    public boolean alwaysShowEffects() {
        return alwaysShowEffects.get();
    }

    /**
     * Returns width from the left edge of the Minecraft window to the left
     * edge of {@link HudGui HudGui}.
     *
     * @return width from the left edge of the Minecraft window to the left
     *         edge of {@code HudGui}.
     */
    public int hudX() {
        return hudX.get();
    }

    /**
     * Returns height from the top edge of the Minecraft window to the top
     * edge of {@link HudGui HudGui}.
     *
     * @return height from the top edge of the Minecraft window to the top
     *         edge of {@code HudGui}.
     */
    public int hudY() {
        return hudY.get();
    }

    /**
     * Returns the current game for the Dream mode on Hypixel.
     *
     * @return the current game for the Dream mode on Hypixel
     */
    public DreamMode currentDreamMode() {
        return currentDreamMode.get();
    }

    // Modification Operations

    /**
     * Changes whether diamond and emerald generation times should be shown on
     * {@link HudGui HudGui}.
     *
     * @param newValue whether diamond and emerald generation times should be
     *         shown on {@code HudGui}
     */
    public void changeShowGenerationTimes(boolean newValue) {
        showGenerationTimes.set(newValue);
    }

    /**
     * Changes whether team upgrades should be shown on {@link HudGui HudGui}.
     *
     * @param newValue whether team upgrades should be shown on {@code HudGui}
     */
    public void changeShowTeamUpgrades(boolean newValue) {
        showTeamUpgrades.set(newValue);
    }

    /**
     * Changes whether armor information should be shown on
     * {@link HudGui HudGui}.
     *
     * @param newValue whether armor information should be shown on
     *         {@code HudGui}
     */
    public void changeShowArmorInfo(boolean newValue) {
        showArmorInfo.set(newValue);
    }

    /**
     * Changes whether effects information should be shown on
     * {@link HudGui HudGui}.
     *
     * @param newValue whether effects information should be shown on
     *         {@code HudGui}
     */
    public void changeShowEffectsInfo(boolean newValue) {
        showEffectsInfo.set(newValue);
    }

    /**
     * Changes whether status effects should always be shown on
     * {@link HudGui HudGui}.
     *
     * @param newValue whether status effects should always be shown on
     *         {@code HudGui}
     */
    public void changeAlwaysShowEffects(boolean newValue) {
        alwaysShowEffects.set(newValue);
    }

    /**
     * Changes the width from the left edge of the Minecraft window to the left
     * edge of {@link HudGui HudGui}.
     *
     * @param newValue the new value for the width
     * @throws IllegalArgumentException if the new value is out of the permitted
     *         range (0 to {@link #maxHudX()}, inclusive)
     */
    public void changeHudX(int newValue) {
        int max = maxHudX();
        if (newValue < 0 || newValue > max) {
            throw new IllegalArgumentException("New value out of range " +
                    " (0-" + max + "): " + newValue);
        }
        hudX.set(newValue);
    }

    /**
     * Changes the height from the left edge of the Minecraft window to the top
     * edge of {@link HudGui HudGui}.
     *
     * @param newValue the new value for the height
     * @throws IllegalArgumentException if the new value is out of the permitted
     *         range (0 to {@link #maxHudY()}, inclusive)
     */
    public void changeHudY(int newValue) {
        int max = maxHudY();
        if (newValue < 0 || newValue > max) {
            throw new IllegalArgumentException("New value out of range " +
                    " (0-" + max + "): " + newValue);
        }
        hudY.set(newValue);
    }

    /**
     * Changes the current game for the Dream mode on Hypixel.
     *
     * @param newValue the new {@linkplain DreamMode Dream mode}
     * @throws NullPointerException if {@code newValue == null}
     */
    public void changeCurrentDreamMode(DreamMode newValue) {
        Objects.requireNonNull(newValue, "newValue");
        currentDreamMode.set(newValue);
    }

    /**
     * Saves changes to this mod's configuration.
     */
    public void save() {
        SPEC.save();
    }
}
