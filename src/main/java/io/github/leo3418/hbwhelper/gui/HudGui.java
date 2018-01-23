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

import io.github.leo3418.hbwhelper.util.ArmorReader;
import io.github.leo3418.hbwhelper.util.GameDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * The GUI of this mod shown in Minecraft's Head-Up Display (HUD).
 *
 * @author Leo
 * @see <a href="https://minecraft.gamepedia.com/Heads-up_display"
 *         target="_top">Minecraft Wiki's introduction on Minecraft's HUD</a>
 */
public class HudGui extends Gui {
    /**
     * The color of text displayed on this GUI
     */
    private static final int TEXT_COLOR = 0xFFFFFF;

    /**
     * Width from the left edge of the window to the left edge of this GUI
     */
    private static final int BEGINNING_WIDTH = 2;

    /**
     * Height from the top edge of the window to the top edge of this GUI
     */
    private static final int BEGINNING_HEIGHT = 24;

    /**
     * Height between two text lines on this GUI
     */
    private static final int LINE_SPACE = 10;

    private final Minecraft mc;

    /**
     * Constructs a new instance of this GUI.
     *
     * @param mc the current Minecraft instance
     */
    public HudGui(Minecraft mc) {
        this.mc = mc;
    }

    /**
     * When vanilla Minecraft's HUD is rendered, renders this GUI on the HUD.
     *
     * @param event the event called when an element on the HUD is rendered
     */
    public void render(RenderGameOverlayEvent.Post event) {
        /*
        We only need to render this GUI per one object's rendering on the HUD,
        or some vanilla elements on the HUD might not display correctly. We
        just pick the hotbar as the object we monitor. Also, we only want this
        GUI to be displayed when the client is in Bed Wars.
         */
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR &&
                GameDetector.getInstance().isIn()) {
            int currentHeight = BEGINNING_HEIGHT;

            // Renders the first line
            String armorInfo;
            ArmorMaterial armorMaterial = ArmorReader.getMaterial();
            if (armorMaterial == null) {
                armorInfo = "Not wearing armor";
            } else {
                armorInfo = "Wearing " + armorMaterial.toString()
                        .toLowerCase() + " armor";
            }
            drawString(mc.fontRenderer, armorInfo, BEGINNING_WIDTH,
                    currentHeight, TEXT_COLOR);

            // Renders the second line
            currentHeight += LINE_SPACE;
            // Code that renders the second line goes here
        }
    }
}
