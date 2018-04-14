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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * The main class of HBW Helper mod.
 *
 * @author Leo
 */
@Mod(
        name = HbwHelper.NAME,
        modid = HbwHelper.MOD_ID,
        version = HbwHelper.VERSION,
        acceptedMinecraftVersions = "@compatible_versions@",
        clientSideOnly = true,
        guiFactory = "io.github.leo3418.hbwhelper.gui.ConfigGuiFactory",
        updateJSON = "https://leo3418.github.io/HBWHelper/promotions.json"
)
public class HbwHelper {
    public static final String NAME = "HBW Helper";
    public static final String MOD_ID = "hbwhelper";
    public static final String VERSION = "@version@";

    @EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        ConfigManager.getInstance().init(event);
    }

    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        EventManager.getInstance().registerOnEventBus();
        KeyBindingManager.registerBindings();
    }
}
