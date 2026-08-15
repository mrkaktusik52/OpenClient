package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import com.cactus.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Coords extends HudModule {
    public Coords() {
        setX(100);
        setY(10);
    }
    @Override
    public String getName() {
        return "Coordinates";
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (!enabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int padding = 5;
        int gap = 2;
        int lineHeight = mc.font.lineHeight;

        String xText = "X: " + (int) mc.player.getX();
        String yText = "Y: " + (int) mc.player.getY();
        String zText = "Z: " + (int) mc.player.getZ();



        int width = 0;
        int lines = 0;

        if (showX.getValue()) {
            width = Math.max(width, mc.font.width(xText));
            lines++;
        }

        if (showY.getValue()) {
            width = Math.max(width, mc.font.width(yText));
            lines++;
        }

        if (showZ.getValue()) {
            width = Math.max(width, mc.font.width(zText));
            lines++;
        }

        if (lines == 0) {
            setWidth(0);
            setHeight(0);
            return;
        }

        width += padding * 2;

        int height =
                padding * 2
                        + lines * lineHeight
                        + (lines - 1) * gap;

        setWidth(width);
        setHeight(height);


        graphics.fill(
                getX(),
                getY(),
                getX() + getWidth(),
                getY() + getHeight(),
                0x80000000
        );



        int yOffset = getY() + padding;

        if (showX.getValue()) {
            graphics.drawString(
                    mc.font,
                    xText,
                    getX() + padding,
                    yOffset,
                    0xFFFFFFFF,
                    true
            );

            yOffset += lineHeight + gap;
        }

        if (showY.getValue()) {
            graphics.drawString(
                    mc.font,
                    yText,
                    getX() + padding,
                    yOffset,
                    0xFFFFFFFF,
                    true
            );

            yOffset += lineHeight + gap;
        }

        if (showZ.getValue()) {
            graphics.drawString(
                    mc.font,
                    zText,
                    getX() + padding,
                    yOffset,
                    0xFFFFFFFF,
                    true
            );
        }
    }
//    public Identifier getIcon() {
//        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
//    };

    private final BooleanSetting showX = new BooleanSetting("Show X", "show_x", true);
    private final BooleanSetting showY = new BooleanSetting("Show Y", "show_y", true);
    private final BooleanSetting showZ = new BooleanSetting("Show Z", "show_z", true);

    @Override
    public String getId() {
        return "coordinates";
    }
}
