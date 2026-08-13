package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Biome extends HudModule {
    public Biome() {
        setX(1);
        setY(50);
    }
    @Override
    public String getName() {
        return "Biome Display";
    }

    @Override
    public void render(GuiGraphics graphics){
        if (!enabled) return;

        Minecraft mc = Minecraft.getInstance();
        assert mc.player != null;
        assert mc.level != null;

        String rawName = mc.level.getBiome(mc.player.blockPosition()).getRegisteredName();

        String path = rawName.contains(":") ? rawName.split(":")[1] : rawName;

        StringBuilder formatted = new StringBuilder();
        for (String word : path.split("_")) {
            if (!word.isEmpty()) {
                formatted.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        String text = "Biome: " + formatted.toString().trim();
//        String text = "Biome: " + mc.level.getBiome(mc.player.blockPosition()).getRegisteredName();

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

}
