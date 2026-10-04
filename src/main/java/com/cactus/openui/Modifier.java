package com.cactus.openui;

import com.cactus.openui.modifiers.*;

import java.util.ArrayList;
import java.util.List;

public class Modifier {
    private final List<ModifierElement> elements = new ArrayList<>();

    public static Modifier modifier() {
        return new Modifier();
    }

    public Modifier padding(int padding) {
        elements.add(new PaddingModifier(padding));
        return this;
    }

    public Modifier width(int width) {
        elements.add(new WidthModifier(width));
        return this;
    }

    public Modifier height(int height) {
        elements.add(new HeightModifier(height));
        return this;
    }

    public List<ModifierElement> getElements() {
        return elements;
    }

    public Modifier background(int color) {
        elements.add(new BackgroundModifier(color));
        return this;
    }

    public Modifier background() {
        elements.add(new BackgroundModifier());
        return this;
    }
}