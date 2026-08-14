package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;

public class Keystrokes extends HudModule {

    public Keystrokes() {
        setX(100);
        setY(500);
        setHeight(100);
        setWidth(50);
    }



    private int colorPressed   = 0xFFD1D1D1;
    private int colorReleased  = 0x80000000;
    private int colorText      = 0xFFFFFFFF;


    private static final int KEY_SIZE = 22;
    private static final int GAP      = 2;


    private record KeyEntry(String label, KeyMapping key) {}

    private KeyEntry[] getKeys() {
        var o = Minecraft.getInstance().options;
        return new KeyEntry[]{
                new KeyEntry("W",     o.keyUp),
                new KeyEntry("A",     o.keyLeft),
                new KeyEntry("S",     o.keyDown),
                new KeyEntry("D",     o.keyRight),
                new KeyEntry("Space",     o.keyJump),
                new KeyEntry("Shift",     o.keyShift),
                new KeyEntry("Ctrl",    o.keySprint),
                new KeyEntry("LMB",   o.keyAttack),
                new KeyEntry("RMB",   o.keyUse),
        };
    }


    @Override
    public void render(GuiGraphics graphics) {
        if (!enabled) return;

        var mc = Minecraft.getInstance();
        if (mc.player == null) return;

        var o   = mc.options;
        var font = mc.font;
        int x   = getX();
        int y   = getY();
        int s   = KEY_SIZE;
        int g   = GAP;


        renderKey(graphics, o.keyUp,    "W",     x + s + g,     y,                 s, s);

        renderKey(graphics, o.keyLeft,  "A",     x,             y + s + g,         s, s);
        renderKey(graphics, o.keyDown,  "S",     x + s + g,     y + s + g,         s, s);
        renderKey(graphics, o.keyRight, "D",     x + (s + g)*2, y + s + g,         s, s);

        renderKey(graphics, o.keyJump,  "SPACE", x,             y + (s + g)*2,     s*3 + g*2, s);

        int halfW = (s*3 + g*2 - g) / 2;
        renderKey(graphics, o.keyShift,  "SNEAK",  x,            y + (s + g)*3, halfW, s);
        renderKey(graphics, o.keySprint, "SPRINT",  x + halfW + g, y + (s + g)*3, halfW, s);

        renderKey(graphics, o.keyAttack, "LMB", x,             y + (s + g)*4, halfW, s);
        renderKey(graphics, o.keyUse,    "RMB", x + halfW + g, y + (s + g)*4, halfW, s);

        setWidth(s * 3 + g * 2);
        setHeight(s * 5 + g * 4);
    }

    private void renderKey(GuiGraphics graphics, KeyMapping key,
                           String label, int x, int y, int w, int h) {

        boolean pressed = key.isDown();
        int bg  = pressed ? colorPressed : colorReleased;

        graphics.fill(x, y, x + w, y + h, bg);

        var font  = Minecraft.getInstance().font;
        int textX = x + (w - font.width(label)) / 2;
        int textY = y + (h - font.lineHeight) / 2;
        graphics.drawString(font, label, textX, textY, colorText, true);
    }

    @Override
    public String getName() { return "Keystrokes"; }
}