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

package io.github.leo3418.hbwhelper;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import io.github.leo3418.hbwhelper.game.DreamMode;
import io.github.leo3418.hbwhelper.gui.HudGui;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ResourcePackLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.leo3418.hbwhelper.HbwHelper.MOD_ID;
import static io.github.leo3418.hbwhelper.HbwHelper.NAME;

/**
 * Configuration manager of this mod, which reads from and writes to this mod's
 * configuration file.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
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

    /**
     * Initializes configuration of this mod.
     */
    void initConfig() {
        ModFile modFile = ResourcePackLoader.getResourcePackFor(MOD_ID)
                .orElseThrow(() -> {
                    throw new IllegalStateException(NAME +
                            " could get the resource pack of itself");
                })
                .getModFile();
        Path defaultResourcePath = modFile.getLocator()
                .findPath(modFile, "META-INF", MOD_ID + "-default.toml");
        System.out.println(defaultResourcePath);
        config = CommentedFileConfig.builder(CONFIG_PATH)
                .sync()
                .defaultData(defaultResourcePath)
                .autoreload()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
    }

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
}
