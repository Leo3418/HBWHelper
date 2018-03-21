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
import io.github.leo3418.hbwhelper.HbwHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The factory for making this mod's GUI configuration screen.
 *
 * @author Leo
 */
public class ConfigGuiFactory implements IModGuiFactory {
    /**
     * Called when instantiated to initialize with the active minecraft
     * instance.
     *
     * @param minecraftInstance the instance
     */
    public void initialize(Minecraft minecraftInstance) {
    }

    /**
     * Return the name of a class extending {@link GuiScreen}. This class will
     * be instantiated when the "config" button is pressed in the mod list. It
     * will have a single argument constructor - the "parent" screen, the same
     * as all Minecraft GUIs. The expected behaviour is that this screen will
     * replace the "mod list" screen completely, and will return to the mod
     * list screen through the parent link, once the appropriate action is
     * taken from the config screen.
     * <p>
     * A null from this method indicates that the mod does not provide a
     * "config" button GUI screen, and the config button will be
     * hidden/disabled.
     * <p>
     * This config GUI is anticipated to provide configuration to the mod in a
     * friendly visual way. It should not be abused to set internals such as
     * IDs (they're gonna keep disappearing anyway), but rather, interesting
     * behaviours. This config GUI is never run when a server game is running,
     * and should be used to configure desired behaviours that affect server
     * state. Costs, mod game modes, stuff like that can be changed here.
     *
     * @return A class that will be instantiated on clicks on the config button
     *         or null if no GUI is desired.
     */
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGuiScreen.class;
    }

    /**
     * Return a list of the "runtime" categories this mod wishes to populate
     * with GUI elements.
     * <p>
     * Runtime categories are created on demand and organized in a 'lite' tree
     * format. The parent represents the parent node in the tree. There is one
     * special parent 'Help' that will always list first, and is generally
     * meant to provide Help type content for mods. The remaining parents will
     * sort alphabetically, though this may change if there is a lot of
     * alphabetic abuse. "AAA" is probably never a valid category parent.
     * <p>
     * Runtime configuration itself falls into two flavours: in-game help,
     * which is generally non interactive except for the text it wishes to
     * show, and client-only affecting behaviours. This would include things
     * like toggling minimaps, or cheat modes or anything NOT affecting the
     * behaviour of the server. Please don't abuse this to change the state of
     * the server in any way, this is intended to behave identically when the
     * server is local or remote.
     *
     * @return the set of options this mod wishes to have available, or empty
     *         if none
     */
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    /**
     * Return an instance of a {@link RuntimeOptionGuiHandler} that handles
     * painting the right hand side option screen for the specified
     * {@link RuntimeOptionCategoryElement}.
     *
     * @param element The element we wish to paint for
     * @return The Handler for painting it
     */
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    /**
     * The configuration screen of this mod.
     */
    private static class ConfigGuiScreen extends GuiConfig {
        /**
         * The {@link ConfigManager} of this mod
         */
        private static final ConfigManager CONFIG_MANAGER =
                ConfigManager.getInstance();

        /**
         * Constructs a new configuration screen.
         *
         * @param parent the parent screen of this configuration screen
         */
        private ConfigGuiScreen(GuiScreen parent) {
            super(parent, getConfigElements(), HbwHelper.MOD_ID, false, false,
                    I18n.format("hbwhelper.configGui.title"));
        }

        /**
         * Returns a {@link List} storing settings elements to be displayed on
         * this screen.
         *
         * @return a {@code List} storing settings elements to be displayed on
         *         this screen
         */
        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> configElements = new ArrayList<IConfigElement>();
            configElements.add(new ConfigElement(
                    CONFIG_MANAGER.getAlwaysShowEffectsProperty()));
            return configElements;
        }
    }
}
