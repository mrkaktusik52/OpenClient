package com.cactus.openui.components;

import com.cactus.openui.Component;
import com.cactus.openui.Modifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Text extends Component {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.drawString(Minecraft.getInstance().font, text, x, y, 0xFFFFFFFF, true);
    }
}