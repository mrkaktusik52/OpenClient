package com.cactus.openui;

import net.minecraft.client.gui.GuiGraphics;

public class OpenUI {
    private final Component root;

    public OpenUI(Component root) {
        this.root = root;
    }

    public void render(GuiGraphics graphics, int x, int y) {
        root.render(graphics, x, y);
    }

}
