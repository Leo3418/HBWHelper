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
import io.github.leo3418.hbwhelper.HbwHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Collections;
import java.util.Set;

/**
 * The factory of this mod's GUI configuration screen.
 *
 * @author Leo
 * @see ConfigManager
 */
public class ConfigGuiFactory implements IModGuiFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    /**
     * Tells Minecraft Forge that this mod has a GUI for configuration settings.
     *
     * @return {@code true}
     */
    @Override
    public boolean hasConfigGui() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGuiScreen(parentScreen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }

    /**
     * The configuration screen of this mod.
     */
    public static class ConfigGuiScreen extends GuiConfig {
        /**
         * Constructs a new configuration screen.
         *
         * @param parent the parent screen of this configuration screen
         */
        private ConfigGuiScreen(GuiScreen parent) {
            super(parent, ConfigManager.getInstance().getConfigElements(),
                    HbwHelper.MOD_ID, false, false,
                    I18n.format("hbwhelper.configGui.title", HbwHelper.NAME));
        }
    }
}
