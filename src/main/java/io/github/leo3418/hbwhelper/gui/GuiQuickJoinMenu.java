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

import io.github.leo3418.hbwhelper.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * The GUI of this mod for the Bed Wars quick join feature.
 *
 * @author Leo
 */
public class GuiQuickJoinMenu extends GuiScreen {
    /**
     * Height of the title of this GUI
     */
    private static final int TITLE_HEIGHT = 40;

    /**
     * Vertical distance between borders of two adjacent buttons
     */
    private static final int BUTTONS_INTERVAL = 4;

    /**
     * Width of long buttons on this GUI
     */
    private static final int LONG_BUTTON_WIDTH = 200;

    /**
     * Width of short buttons on this GUI
     */
    private static final int SHORT_BUTTON_WIDTH =
            (LONG_BUTTON_WIDTH - BUTTONS_INTERVAL) / 2;

    /**
     * Height of buttons on this GUI
     */
    private static final int BUTTON_HEIGHT = 20;

    /**
     * Vertical distance between the top of a button and the top of the button
     * right below it
     */
    private static final int BUTTONS_TRANSLATION_INTERVAL =
            BUTTON_HEIGHT + BUTTONS_INTERVAL;

    /**
     * Initializes this GUI with buttons.
     */
    @Override
    public void initGui() {
        int buttonX = (this.width - LONG_BUTTON_WIDTH) / 2;
        int firstButtonY = this.height / 4 + BUTTONS_INTERVAL;
        // Number of lines of buttons added so far, used for calculating height
        // of the next button
        int line = 0;
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.solo"),
                "/play bedwars_eight_one"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.doubles"),
                "/play bedwars_eight_two"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.3v3v3v3"),
                "/play bedwars_four_three"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"),
                "/play bedwars_four_four"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.SHORT,
                buttonX, firstButtonY + (line * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.capture.noParties"),
                "/play bedwars_capture_solo"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.SHORT,
                (this.width + BUTTONS_INTERVAL) / 2,
                firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.capture.parties"),
                "/play bedwars_capture_party"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.SHORT,
                buttonX, firstButtonY + (line * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.dream.doubles"),
                "/play bedwars_eight_two_ultimate"
        ));
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.SHORT,
                (this.width + BUTTONS_INTERVAL) / 2,
                firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.dream.4v4v4v4"),
                "/play bedwars_four_four_ultimate"
        ));
        // The following should always be the last button
        this.buttonList.add(QuickJoinGuiButton.getButton(
                QuickJoinGuiButton.Variant.LONG,
                buttonX, firstButtonY + (line * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.backToGame"),
                null
        ));
    }

    /**
     * Draws this GUI on the screen.
     *
     * @param mouseX horizontal location of the mouse
     * @param mouseY vertical location of the mouse
     * @param partialTicks number of partial ticks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj,
                I18n.format("hbwhelper.quickJoinGui.title"),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * When a button on this GUI is clicked, performs the action the button
     * defines.
     *
     * @param button the button being clicked
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof QuickJoinGuiButton) {
            ((QuickJoinGuiButton) button).performAction(this);
        }
    }

    /**
     * When a key is typed when this GUI is shown, checks which key is pressed,
     * and closes this GUI if client presses {@code ESC} or the hot key for Bed
     * Wars quick join.
     *
     * @param typedChar the character typed
     * @param keyCode the code of the key pressed
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 || keyCode == KeyBindings.QUICK_JOIN.getKeyCode()) {
            close();
        }
    }

    /**
     * Closes this GUI.
     */
    private void close() {
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
    }

    /**
     * A button shown on this GUI screen.
     * <p>
     * The original {@link GuiButton} class tracks an numeric ID for a button
     * created from the class. Client programs of the class should use the ID
     * to differentiate which button is pressed and perform corresponding
     * actions.
     * <p>
     * For buttons shown on this screen, they do either of the following
     * things: perform a in-game command on the client's behalf, or merely hide
     * this GUI. The actions are simple and already well-defined by a command
     * string, so it makes better sense to make the command a property of the
     * button instead of deciding what action to take in the client program.
     */
    private static class QuickJoinGuiButton extends GuiButton {
        /**
         * The command being run when this button is clicked
         * <p>
         * If this field is set to {@code null}, then this GUI will be closed
         * immediately when this button is pressed, and nothing else will be
         * done.
         */
        private final String command;

        /**
         * Constructs a new button for this GUI.
         *
         * @param x the horizontal location of this button on the GUI
         * @param y the vertical location of this button on the GUI
         * @param width the width of this button
         * @param buttonText the text shown on this button
         * @param command the command being run in game when this button is
         *         pressed
         */
        private QuickJoinGuiButton(int x, int y, int width, String buttonText,
                                   String command) {
            super(0, x, y, width, BUTTON_HEIGHT, buttonText);
            this.command = command;
        }

        /**
         * Returns a button for this GUI in specified variant of the button's
         * width.
         *
         * @param variant the {@link Variant Variant} of the button's width
         * @param x the horizontal location of this button on the GUI
         * @param y the vertical location of this button on the GUI
         * @param buttonText the text shown on this button
         * @param command the command being run in game when this button is
         *         pressed
         * @return a button for this GUI in the specified variant
         */
        private static QuickJoinGuiButton getButton(Variant variant,
                                                    int x, int y,
                                                    String buttonText,
                                                    String command) {
            return new QuickJoinGuiButton(x, y, variant.width, buttonText,
                    command);
        }

        /**
         * If a command is defined for this button, runs the command. Then,
         * closes this GUI.
         *
         * @param gui the object for this GUI
         */
        private void performAction(GuiQuickJoinMenu gui) {
            if (command != null) {
                // Runs the command by sending it to the chat box
                Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
            }
            gui.close();
        }

        /**
         * Enumeration of variants of the button.
         */
        private enum Variant {
            /**
             * Long button, whose width is the same as a default Minecraft
             * button
             */
            LONG(LONG_BUTTON_WIDTH),
            /**
             * Short button, whose width times 2 plus the default interval
             * between two Minecraft buttons would be the same as a long button
             */
            SHORT(SHORT_BUTTON_WIDTH);

            /**
             * Width of a button of this variant
             */
            private final int width;

            /**
             * Constructs a new constant of variants of the button.
             *
             * @param width the width of a button of this variant
             */
            Variant(int width) {
                this.width = width;
            }
        }
    }
}
