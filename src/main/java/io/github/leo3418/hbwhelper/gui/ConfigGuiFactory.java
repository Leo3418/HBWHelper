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
     * Called when instantiated to initialize with the active Minecraft
     * instance.
     *
     * @param minecraftInstance the instance
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
     * Return an initialized {@link GuiScreen}. This screen will be displayed
     * when the "config" button is pressed in the mod list. It will
     * have a single argument constructor - the "parent" screen, the same as
     * all Minecraft GUIs. The expected behaviour is that this screen will
     * replace the "mod list" screen completely, and will return to the mod
     * list screen through the parent link, once the appropriate action is
     * taken from the config screen.
     * <p>
     * This config GUI is anticipated to provide configuration to the mod in a
     * friendly visual way. It should not be abused to set internals such as
     * IDs (they're gonna keep disappearing anyway), but rather, interesting
     * behaviours. This config GUI is never run when a server game is running,
     * and should be used to configure desired behaviours that affect server
     * state. Costs, mod game modes, stuff like that can be changed here.
     *
     * @param parentScreen the screen to which must be returned when closing
     *         the returned screen
     * @return A class that will be instantiated on clicks on the config button
     *         or {@code null} if no GUI is desired
     */
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGuiScreen(parentScreen);
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
                    I18n.format("hbwhelper.configGui.title"));
        }
    }
}
