package com.cactus.openui;

import net.minecraft.client.gui.GuiGraphics;

public abstract class Component {


    protected Modifier modifier = new Modifier();

    public Component modifier(Modifier modifier) {
        this.modifier = modifier;
        return this;
    }

    public abstract void render(GuiGraphics graphics, int x, int y);

}