package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
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
    public void render(GuiGraphics graphics){
        if (!enabled) return;

        Minecraft mc = Minecraft.getInstance();
        assert mc.player != null;
        String x = "X: " + (int) mc.player.getX();
        String y = "Y: " + (int) mc.player.getY();
        String z = "Z: " + (int) mc.player.getZ();

        setWidth(Math.max(mc.font.width(x), Math.max(mc.font.width(y), mc.font.width(z))) + 10);
        setHeight(mc.font.lineHeight * 3 + 20);

        graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);

        graphics.drawString(mc.font, x, getX() + 5, getY() + 5, 0xFFFFFFFF, true);
        graphics.drawString(mc.font, y, getX() + 5, getY() + mc.font.lineHeight + 10, 0xFFFFFFFF, true);
        graphics.drawString(mc.font, z, getX() + 5, getY() + mc.font.lineHeight + 25, 0xFFFFFFFF, true);
    }
//    public Identifier getIcon() {
//        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
//    };
    @Override
    public boolean hasSettings() {return true;};
}
