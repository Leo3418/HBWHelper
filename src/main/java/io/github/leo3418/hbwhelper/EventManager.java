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
import io.github.leo3418.hbwhelper.gui.HudGui;
import io.github.leo3418.hbwhelper.util.GameDetector;
import io.github.leo3418.hbwhelper.util.GameManager;
import io.github.leo3418.hbwhelper.util.HypixelDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * Event manager of this mod, which responds to events fired on
 * Minecraft Forge's event bus and this mod's {@link EventManager#EVENT_BUS
 * proprietary event bus}, and also holds that proprietary event bus.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 * @see EventManager#EVENT_BUS
 */
public class EventManager {
    /**
     * The proprietary event bus of this mod
     */
    public static final EventBus EVENT_BUS = new EventBus();

    /**
     * Prompt client receives when Minecraft has been restarted, and it rejoins
     * a Bed Wars game
     */
    private static final String CLIENT_RESTARTED_PROMPT =
            "[" + HbwHelper.NAME + "] " + "We have detected that you " +
                    "restarted your Minecraft client. Unfortunately, we " +
                    "cannot retrieve what team upgrade you have got before.";

    /**
     * Prompt client receives when Minecraft has <b>not</b> been restarted, and
     * it rejoins a Bed Wars game
     */
    private static final String CLIENT_REJOINED_PROMPT =
            "[" + HbwHelper.NAME + "] " + "We have detected that you rejoined " +
                    "a Bed Wars game. Any team upgrade your teammate has got " +
                    "since your disconnection cannot be displayed.";

    /**
     * The only instance of this class
     */
    private static volatile EventManager instance;

    private final HypixelDetector hypixelDetector;
    private final GameDetector gameDetector;
    private HudGui hudGui;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
        EventManager.EVENT_BUS.register(this);
        hudGui = new HudGui();
        hypixelDetector = HypixelDetector.getInstance();
        gameDetector = GameDetector.getInstance();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    @SubscribeEvent
    public void onFMLNetworkEvent(FMLNetworkEvent event) {
        hypixelDetector.update(event);
        gameDetector.update(event);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        gameDetector.update(event);
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        gameDetector.update(event);
        if (gameDetector.isIn() && GameManager.getInstance() != null) {
            GameManager.getInstance().update(event);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        hudGui.render(event);
    }

    @SubscribeEvent
    public void onGameStart(GameStartEvent event) {
        new GameManager();
    }

    @SubscribeEvent
    public void onClientRejoinGame(ClientRejoinGameEvent event) {
        if (GameManager.getInstance() == null) {
            // Client is rejoining a Bed Wars game after restart of Minecraft
            Minecraft.getMinecraft().thePlayer.addChatMessage(new
                    ChatComponentText(CLIENT_RESTARTED_PROMPT));
            new GameManager();
        } else {
            // Client is rejoining a Bed Wars game, but Minecraft is not closed
            Minecraft.getMinecraft().thePlayer.addChatMessage(new
                    ChatComponentText(CLIENT_REJOINED_PROMPT));
        }
    }
}
