package com.cactus.openui.modifiers;

public class BackgroundModifier implements ModifierElement{
    public final int color;

    public BackgroundModifier() {
        color = 0x80000000;
    }
    public BackgroundModifier(int color) {
        this.color = color;
    }
}
