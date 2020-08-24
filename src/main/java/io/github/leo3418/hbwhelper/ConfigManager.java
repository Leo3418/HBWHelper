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

package io.github.leo3418.hbwhelper;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import io.github.leo3418.hbwhelper.game.DreamMode;
import io.github.leo3418.hbwhelper.gui.HudGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ResourcePackLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static io.github.leo3418.hbwhelper.HbwHelper.MOD_ID;
import static io.github.leo3418.hbwhelper.HbwHelper.NAME;

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
    /*
     * Implementation note:
     *
     * In Minecraft Forge 25.0 (for Minecraft 1.13.2), Forge introduced
     * the `ForgeConfigSpec` class for mods to store their configuration to
     * replace the original `Configuration` class. Removed in Forge 25.0 was
     * also the configuration GUI for mods, but chances are a new configuration
     * GUI framework will be added back in a future Forge release. If this
     * happens, then the `ForgeConfigSpec` class will probably be a part of the
     * framework, so it might be a good idea to use it as the underlying utility
     * of this mod's configuration. But, that class does not support changing
     * configuration values yet. The Minecraft Forge development team might
     * think that because the configuration GUI is still absent now, the
     * configuration cannot be changed while Minecraft is running.
     *
     * HBW Helper strives to keep all of its functionality in the port to 1.14.4
     * even if some components of Forge that this mod depends on have been
     * removed and not added back yet. This means the configuration of this mod
     * is still modifiable when Minecraft is running. So, instead of the
     * `ForgeConfigSpec` class, the
     * `com.electronwill.nightconfig.core.file.CommentedFileConfig` interface -
     * a member of NightConfig library, which is the underlying part of Forge's
     * new configuration utilities - is used because it supports changing
     * configuration values.
     *
     * In the future, if `ForgeConfigSpec` supports changing configuration
     * values, then this class should start using it.
     */

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
    private static final ConfigManager INSTANCE = new ConfigManager();

    /**
     * {@link Path} to the configuration file of this mod
     */
    private static final Path CONFIG_PATH =
            Paths.get("config", MOD_ID + ".toml");

    /**
     * Configuration file of this mod
     */
    private CommentedFileConfig config;

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

    // Validations

    /**
     * Returns an {@link Optional} wrapping the {@link Class} of an option's
     * value in this mod's configuration, or an empty {@code Optional} if the
     * option being queried is invalid.
     *
     * @param option the name of the option whose value's {@code Class} is
     *         queried
     * @return an {@code Optional} wrapping the {@code Class} of the option's
     *         value in this mod's configuration if the {@code option} argument
     *         is a valid name
     * @throws NullPointerException if {@code option == null}
     */
    public static Optional<Class<?>> getClassOf(String option) {
        Objects.requireNonNull(option, "option");
        switch (option) {
            case "showGenerationTimes":
            case "showTeamUpgrades":
            case "showArmorInfo":
            case "showEffectsInfo":
            case "alwaysShowEffects":
                return Optional.of(Boolean.TYPE);
            case "hudX":
            case "hudY":
                return Optional.of(Integer.TYPE);
            case "currentDreamMode":
                return Optional.of(DreamMode.class);
            default:
                return Optional.empty();
        }
    }

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

    // Initialization

    /**
     * Initializes configuration of this mod.
     */
    void initConfig() {
        ModFile modFile = ResourcePackLoader.getResourcePackFor(MOD_ID)
                .orElseThrow(() -> new IllegalStateException(NAME +
                        " could get the resource pack of itself"))
                .getModFile();
        Path defaultResourcePath = modFile.getLocator()
                .findPath(modFile, "META-INF", MOD_ID + "-default.toml");
        config = CommentedFileConfig.builder(CONFIG_PATH)
                .sync()
                .defaultData(defaultResourcePath)
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
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
        return config.get("showGenerationTimes");
    }

    /**
     * Returns whether team upgrades should be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether team upgrades should be shown on {@code HudGui}
     */
    public boolean showTeamUpgrades() {
        return config.<Boolean>getOptional("showTeamUpgrades")
                .orElse(Boolean.TRUE);
    }

    /**
     * Returns whether armor information should be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether armor information should be shown on {@code HudGui}
     */
    public boolean showArmorInfo() {
        return config.<Boolean>getOptional("showArmorInfo")
                .orElse(Boolean.TRUE);
    }

    /**
     * Returns whether effects information should be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether effects information should be shown on {@code HudGui}
     */
    public boolean showEffectsInfo() {
        return config.<Boolean>getOptional("showEffectsInfo")
                .orElse(Boolean.TRUE);
    }

    /**
     * Returns whether status effects should always be shown on
     * {@link HudGui HudGui}.
     *
     * @return whether status effects should always be shown on {@code HudGui}
     */
    public boolean alwaysShowEffects() {
        return config.<Boolean>getOptional("alwaysShowEffects")
                .orElse(Boolean.FALSE);
    }

    /**
     * Returns width from the left edge of the Minecraft window to the left
     * edge of {@link HudGui HudGui}.
     *
     * @return width from the left edge of the Minecraft window to the left
     *         edge of {@code HudGui}.
     */
    public int hudX() {
        return config.<Integer>getOptional("hudX")
                .orElse(DEFAULT_HUD_X);
    }

    /**
     * Returns height from the top edge of the Minecraft window to the top
     * edge of {@link HudGui HudGui}.
     *
     * @return height from the top edge of the Minecraft window to the top
     *         edge of {@code HudGui}.
     */
    public int hudY() {
        return config.<Integer>getOptional("hudY")
                .orElse(DEFAULT_HUD_Y);
    }

    /**
     * Returns the current game for the Dream mode on Hypixel.
     *
     * @return the current game for the Dream mode on Hypixel
     */
    public DreamMode currentDreamMode() {
        return DreamMode.valueOfDisplayName(config.get("currentDreamMode"))
                .orElse(DreamMode.UNSELECTED);
    }

    // Modification Operations

    /**
     * Changes an option's value whose type is {@code boolean}.
     *
     * @param option the name of the option whose value is changed
     * @param newValue the new value for the option
     * @throws NullPointerException if {@code option == null}
     * @throws IllegalArgumentException if the type of the specified option's
     *         value is not {@code boolean}
     */
    public void changeBoolean(String option, boolean newValue) {
        Objects.requireNonNull(option, "option");
        if (!Boolean.TYPE.equals(getClassOf(option).orElse(null))) {
            throw new IllegalArgumentException("The value of " + option +
                    " is not boolean");
        }
        config.set(option, newValue);
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
        config.set("hudX", newValue);
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
        config.set("hudY", newValue);
    }

    /**
     * Changes the current game for the Dream mode on Hypixel.
     *
     * @param newValue the new {@linkplain DreamMode Dream mode}
     * @throws NullPointerException if {@code newValue == null}
     */
    public void changeCurrentDreamMode(DreamMode newValue) {
        Objects.requireNonNull(newValue, "newValue");
        config.set("currentDreamMode", newValue.toDisplayName());
    }

    /**
     * Saves changes to this mod's configuration.
     */
    public void save() {
        config.save();
    }
}
