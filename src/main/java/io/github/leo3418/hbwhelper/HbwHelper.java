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

import com.mojang.brigadier.CommandDispatcher;
import io.github.leo3418.hbwhelper.command.CommandManager;
import io.github.leo3418.hbwhelper.command.ConfigCommand;
import io.github.leo3418.hbwhelper.gui.ConfigScreen;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main class of HBW Helper mod.
 *
 * @author Leo
 */
@Mod(HbwHelper.MOD_ID)
public final class HbwHelper {
    public static final String NAME = "HBW Helper";
    public static final String MOD_ID = "hbwhelper";

    public HbwHelper() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
        ModList.get().getModContainerById(MOD_ID)
                .orElseThrow(() -> new IllegalStateException(NAME +
                        " could not find the mod container of itself"))
                .registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                        () -> (mc, screen) -> new ConfigScreen(screen));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ConfigManager.getInstance().initConfig();
        EventManager.getInstance().registerOnEventBus();
        KeyBindings.registerBindings();
        CommandDispatcher<CommandSource> commandDispatcher =
                CommandManager.getInstance().getDispatcher();
        ConfigCommand.register(commandDispatcher);
    }
}
