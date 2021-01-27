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

package io.github.leo3418.hbwhelper.gui;

import io.github.leo3418.hbwhelper.ConfigManager;
import io.github.leo3418.hbwhelper.HbwHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The factory of this mod's GUI configuration screen.
 *
 * @author Leo
 * @see ConfigManager
 */
public final class ConfigGuiFactory implements IModGuiFactory {
    /**
     * Constructs a new {@link ConfigGuiFactory} instance. This constructor is
     * intended to be called only by Minecraft Forge.
     */
    public ConfigGuiFactory() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGuiScreen.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return new RuntimeOptionGuiHandler() {
            @Override
            public void addWidgets(List<Gui> widgetList, int x, int y, int w, int h) {
            }

            @Override
            public void paint(int x, int y, int w, int h) {
            }

            @Override
            public void actionCallback(int actionId) {
            }

            @Override
            public void close() {
            }
        };
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
        public ConfigGuiScreen(GuiScreen parent) {
            super(parent, ConfigManager.getInstance().getConfigElements(),
                    HbwHelper.MOD_ID, false, false,
                    I18n.format("hbwhelper.configGui.title", HbwHelper.NAME));
        }
    }
}
