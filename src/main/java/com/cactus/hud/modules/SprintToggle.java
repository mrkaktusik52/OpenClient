package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

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
            String text = client.player.isSprinting() ? "[Sprinting]" : client.player.isCrouching() ? "[Sneaking]": "";


            graphics.drawString(
                    client.font,
                    text,
                    (getX() + (getWidth() / 2)) - (client.font.width(text) / 2), (getY() + (getHeight() / 2)),
                    0xFFFFFFFF,
                    true);

        }

    @Override
    public void renderInEditor(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!enabled) return;

        if (this.isDragging) {
            this.setX(mouseX - this.dragOffsetX);
            this.setY(mouseY - this.dragOffsetY);
        }

        graphics.fill(getX() - 1, getY() - 1, getX() + getWidth() + 1, getY(), 0xFFFFFFFF);
        graphics.fill(getX() - 1, getY() + getHeight(), getX() + getWidth() + 1, getY() + getHeight() + 1, 0xFFFFFFFF);
        graphics.fill(getX() - 1, getY(), getX(), getY() + getHeight(), 0xFFFFFFFF);
        graphics.fill(getX() + getWidth(), getY(), getX() + getWidth() + 1, getY() + getHeight(), 0xFFFFFFFF);

        assert client.player != null;
        String text = "[Sprinting]";

        graphics.drawString(
                client.font,
                text,
                (getX() + (getWidth() / 2)) - (client.font.width(text) / 2), (getY() + (getHeight() / 2)),
                0xFFFFFFFF,
                true);
    }


//    public Identifier getIcon() {
//        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
//    };
@Override
public String getId() {
    return "sprintdisplay";
}
}


