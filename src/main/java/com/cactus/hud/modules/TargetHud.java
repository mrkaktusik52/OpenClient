package com.cactus.hud.modules;

import com.cactus.gui.HudEditorScreen;
import com.cactus.hud.HudModule;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;


public class TargetHud extends HudModule {
    Minecraft mc = Minecraft.getInstance();
    private Entity lastHit = null;

    public TargetHud() {

        setX(500);
        setY(300);
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player == Minecraft.getInstance().player) {
                lastHit = entity;
            }
            return InteractionResult.PASS;
        });
    }
    @Override
    public String getName() {
        return "Target HUD";
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (!enabled) return;
        if (lastHit == null) return;

        String name = lastHit.getDisplayName().getString();

        float hp = 0.0f;
        float maxHp = 0.0f;

        if (lastHit instanceof LivingEntity living) {
            hp = living.getHealth();
            maxHp = living.getMaxHealth();
        }

        if (lastHit instanceof Player player) {
            int food = player.getFoodData().getFoodLevel();

        }

        this.setWidth(Math.max(mc.font.width(name), mc.font.width(hp + " / " + maxHp)) + 10);
        setHeight(30);

        graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
        graphics.drawString(mc.font, name, getX() + 5, getY() + 5, 0xFFFFFFFF);
        graphics.drawString(mc.font, hp + " / " + maxHp, getX() + 5, getY() + 15, 0xFFFFFFFF);
    }
    //    public Identifier getIcon() {
//        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
//    };
    @Override
    public boolean hasSettings() {return true;};
    @Override
    public String getId() {
        return "targethud";
    }
}
