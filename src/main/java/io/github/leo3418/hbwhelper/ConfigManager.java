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

package io.github.leo3418.hbwhelper;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Configuration manager of this mod, which reads from and writes to this mod's
 * configuration file.
 *
 * @author Leo
 */
public class ConfigManager {
    /**
     * A reference to the instance created from this class when Forge Mod
     * Loader initializes
     */
    private static ConfigManager instance;

    /**
     * Configuration file of this mod
     */
    private Configuration config;

    /**
     * The {@link Property} object storing whether status effects
     * should always be shown on {@link io.github.leo3418.hbwhelper.gui.GuiHud}
     */
    private Property alwaysShowEffects;

    /**
     * Constructs a new {@code ConfigManager} instance when Forge Mod Loader
     * initializes.
     *
     * @param event the event fired before Forge Mod Loader initializes
     */
    ConfigManager(FMLPreInitializationEvent event) {
        instance = this;
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        initConfig();
    }

    /**
     * Returns the reference to the instance created from this class when Forge
     * Mod Loader initializes.
     *
     * @return the reference to the instance created from this class when Forge
     *         Mod Loader initializes
     */
    public static ConfigManager getInstance() {
        return instance;
    }

    /**
     * Returns the {@link Property} object storing whether status effects
     * should always be shown on {@link io.github.leo3418.hbwhelper.gui.GuiHud}.
     *
     * @return the {@code Property} object storing whether status effects
     *         should always be shown on {@code GuiHud}
     */
    public Property getAlwaysShowEffectsProperty() {
        return alwaysShowEffects;
    }

    /**
     * Returns whether status effects should always be shown on
     * {@link io.github.leo3418.hbwhelper.gui.GuiHud}.
     *
     * @return whether status effects should always be shown on {@code GuiHud}
     */
    public boolean alwaysShowEffects() {
        return getAlwaysShowEffectsProperty().getBoolean();
    }

    /**
     * When configuration of this mod changes, save it to the configuration
     * file.
     *
     * @param event the event fired when configuration of any mod changes
     */
    public void save(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(HbwHelper.MOD_ID)) {
            config.save();
        }
    }

    /**
     * If the configuration file on disk is absent or incomplete, creates or
     * completes the configuration file. Then, loads the configuration file.
     */
    private void initConfig() {
        alwaysShowEffects = config.get(Configuration.CATEGORY_CLIENT,
                "alwaysShowEffects",
                false,
                I18n.format("hbwhelper.configGui.alwaysShowEffects.description"))
                .setLanguageKey("hbwhelper.configGui.alwaysShowEffects.title");
        config.save();
    }
}
