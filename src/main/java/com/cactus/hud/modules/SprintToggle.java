package com.cactus.hud.modules;

import com.cactus.OpenClient;
import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

public class SprintToggle extends HudModule {
    public SprintToggle() {
        setX(500);
        setY(10);
        setWidth(50);
        setHeight(20);
    }

    @Override
    public String getName() {
        return "Sprint Display";
    }

    Minecraft client = Minecraft.getInstance();

        @Override
        public void render(GuiGraphics graphics){
            if (!enabled) return;

            assert client.player != null;
            String text = client.player.isSprinting() ? "[Sprinting]" : client.player.isShiftKeyDown() ? "[Sneaking]": "";;

            graphics.drawString(
                    client.font,
                    text,
                    (getX() + (getWidth() / 2)) - (client.font.width(text) / 2), (getY() + (getHeight() / 2)),
                    0xFFFFFFFF,
                    false);

        }

    @Override
    public boolean hasSettings() {return true;};

    public Identifier getIcon() {
        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
    };
}


