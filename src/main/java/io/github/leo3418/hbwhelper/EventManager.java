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

import io.github.leo3418.hbwhelper.event.ClientRejoinGameEvent;
import io.github.leo3418.hbwhelper.event.GameStartEvent;
import io.github.leo3418.hbwhelper.gui.GuiHud;
import io.github.leo3418.hbwhelper.gui.GuiQuickJoinMenu;
import io.github.leo3418.hbwhelper.util.GameDetector;
import io.github.leo3418.hbwhelper.util.GameManager;
import io.github.leo3418.hbwhelper.util.HypixelDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * Event manager of this mod, which responds to events fired on
 * Minecraft Forge's event bus and this mod's {@link EventManager#EVENT_BUS
 * proprietary event bus}, and also holds that proprietary event bus.
 * <p>
 * This class is the place where most core behaviors of this mod is defined. It
 * decides what actions to take upon each kind of event, whereas other classes
 * define the actions' details and complete the actions.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class EventManager {
    /**
     * The proprietary event bus of this mod
     */
    public static final EventBus EVENT_BUS = new EventBus();

    /**
     * Prefix of prompt sent from this mod to the client
     */
    private static final String PROMPT_PREFIX = "[" + HbwHelper.NAME + "] ";

    /**
     * The only instance of this class
     */
    private static final EventManager INSTANCE = new EventManager();

    /**
     * The {@link HypixelDetector} instance
     */
    private final HypixelDetector hypixelDetector;

    /**
     * The {@link GameDetector} instance
     */
    private final GameDetector gameDetector;

    /**
     * The {@link ConfigManager} instance
     */
    private final ConfigManager configManager;

    /**
     * The {@link GuiHud} instance
     */
    private final GuiHud guiHud;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private EventManager() {
        hypixelDetector = HypixelDetector.getInstance();
        gameDetector = GameDetector.getInstance();
        configManager = ConfigManager.getInstance();
        guiHud = GuiHud.getInstance();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Registers this class as listener of Minecraft Forge's event bus and this
     * mod's {@link #EVENT_BUS proprietary event bus}.
     */
    void registerOnEventBus() {
        MinecraftForge.EVENT_BUS.register(this);
        EventManager.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onFMLNetworkEvent(FMLNetworkEvent event) {
        hypixelDetector.update(event);
        gameDetector.update(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGuiOpen(GuiOpenEvent event) {
        gameDetector.update(event);
        configManager.update(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        gameDetector.update(event);
        if (gameDetector.isIn() && GameManager.getInstance() != null) {
            GameManager.getInstance().update(event);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        guiHud.render(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGameStart(GameStartEvent event) {
        new GameManager();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientRejoinGame(ClientRejoinGameEvent event) {
        if (GameManager.getInstance() == null) {
            // Client is rejoining a Bed Wars game after restart of Minecraft
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new TextComponentString(PROMPT_PREFIX
                            + I18n.format("hbwhelper.messages.clientRestart")));
            new GameManager();
        } else {
            // Client is rejoining a Bed Wars game, but Minecraft is not closed
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new TextComponentString(PROMPT_PREFIX
                            + I18n.format("hbwhelper.messages.clientRejoin")));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        configManager.save(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (hypixelDetector.isIn() && KeyBindings.QUICK_JOIN.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiQuickJoinMenu());
        }
    }
}
