package com.cactus.openui.components;

import com.cactus.openui.Component;
import com.cactus.openui.Modifier;
import com.cactus.openui.modifiers.*;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class Column extends Component {

    private final List<Component> children = new ArrayList<>();
    private int x, y;

    public Column(Modifier modifier) {
        this.modifier = modifier;
    }

    public Column child(Component component) {
        children.add(component);
        return this;
    }

    @Override
    public Column modifier(Modifier modifier) {
        super.modifier(modifier);
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        int padding = 0;
        int width = -1;
        int height = -1;

        for (ModifierElement element : modifier.getElements()) {
            if (element instanceof PaddingModifier p) {
                padding = p.value;
            }

            if (element instanceof WidthModifier w) {
                width = w.value;
            }

            if (element instanceof HeightModifier h) {
                height = h.value;
            }

            if (element instanceof BackgroundModifier b) {
                graphics.fill(x, y, x + width, y + height, b.color);
            }
        }

        if (width != -1) {
            graphics.fill(
                    x,
                    y,
                    x + width,
                    y + height,
                    0x80000000
            );
        }

        int currentY = y + padding;

        for (Component child : children) {
            child.render(
                    graphics,
                    x + padding,
                    currentY
            );

            currentY += 20;
        }
    }
}