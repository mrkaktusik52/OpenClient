package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Fps extends HudModule {
    public Fps() {
        setX(1);
        setY(10);
    }
    @Override
    public String getName() {
        return "FPS";
    }

    @Override
    public void render(GuiGraphics graphics){
        if (!enabled) return;

        Minecraft mc = Minecraft.getInstance();
        String text = "FPS: " + mc.getFps();

        setWidth(mc.font.width(text) + 10);
        setHeight(mc.font.lineHeight + 10);

        graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);

        graphics.drawString(mc.font, text, getX() + 5, getY() + 5, 0xFFFFFFFF, true);
    }
//    public Identifier getIcon() {
//        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
//    };
    @Override
    public boolean hasSettings() {return true;};
    @Override
    public String getId() {
        return "fps";
    }
}

