package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Collection;

import static net.minecraft.client.gui.Gui.getMobEffectSprite;

public class PotionHud extends HudModule {


    private final Minecraft mc = Minecraft.getInstance();

    public PotionHud() {
        setX(10);
        setY(300);
        setWidth(82);
        setHeight(15);
    }

    @Override
    public String getName() {
        return "Potions HUD";
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (!enabled) return;
        if (mc.player == null) return;

        Collection<MobEffectInstance> effects = mc.player.getActiveEffects();
        if (effects.isEmpty()) return;

        int currentY = getY();

        for (MobEffectInstance effect : effects) {
            if (!effect.showIcon()) continue;

            Holder<MobEffect> holder = effect.getEffect();

            String effectName = holder.value().getDisplayName().getString();
            if (effect.getAmplifier() > 0) {
                effectName += " " + (effect.getAmplifier() + 1);
            }
            String timeText = formatEffectDuration(effect);

            int nameWidth = mc.font.width(effectName);
            int timeWidth = mc.font.width(timeText);

            int backgroundWidth = Math.max(nameWidth, timeWidth) + 32;
            int backgroundHeight = 24;

            graphics.fill(
                    getX(),
                    currentY,
                    getX() + backgroundWidth,
                    currentY + backgroundHeight,
                    0x80000000
            );

            Identifier spriteLocation = getMobEffectSprite(holder);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteLocation, getX() + 3, currentY + 3, 18, 18);

            graphics.drawString(mc.font, effectName, getX() + 26, currentY + 3, 0xFFFFFFFF, true);
            graphics.drawString(mc.font, timeText, getX() + 26, currentY + 13, 0xFFFFFFFF, true);

            currentY += backgroundHeight + 2;
        }

        setHeight(Math.max(30, currentY - getY()));
    }


    private String formatEffectDuration(MobEffectInstance effect) {
        if (effect.isInfiniteDuration()) {
            return "**:**";
        }
        int totalSeconds = effect.getDuration() / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public boolean hasSettings() {
        return true;
    }
}
