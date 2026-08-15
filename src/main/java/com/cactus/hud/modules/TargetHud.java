package com.cactus.hud.modules;

import com.cactus.gui.HudEditorScreen;
import com.cactus.hud.HudModule;
import com.cactus.settings.BooleanSetting;
import com.cactus.settings.SliderSetting;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;


public class TargetHud extends HudModule {
    Minecraft mc = Minecraft.getInstance();
    private Entity lastHit = null;
    private long lastTargetTime;

    private static final long TARGET_TIMEOUT = 4000; // 4 секунды

    public TargetHud() {

        setX(500);
        setY(300);
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player == Minecraft.getInstance().player) {
                lastHit = entity;
                lastTargetTime = System.currentTimeMillis();
            }

            return InteractionResult.PASS;
        });

        addSetting(showArmor);
        addSetting(showHealth);
        addSetting(timeout);
    }

    private void validateTarget() {
        if (lastHit == null) return;

        long now = System.currentTimeMillis();

        if (!lastHit.isAlive()
                || lastHit.isRemoved()
                || now - lastTargetTime > TARGET_TIMEOUT) {
            lastHit = null;
        }
    }

    @Override
    public String getName() {
        return "Target HUD";
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (!enabled) return;

        validateTarget();
        if (lastHit == null) return;

        Minecraft mc = Minecraft.getInstance();

        String name = lastHit.getDisplayName().getString();
        float hp = 0.0f;
        float maxHp = 0.0f;
        ItemStack helmet= ItemStack.EMPTY;
        ItemStack chest = ItemStack.EMPTY;
        ItemStack legs = ItemStack.EMPTY;
        ItemStack feet = ItemStack.EMPTY;

        if (lastHit instanceof LivingEntity living) {
            hp = living.getHealth();
            maxHp = living.getMaxHealth();
            helmet = living.getItemBySlot(EquipmentSlot.HEAD);
            chest = living.getItemBySlot(EquipmentSlot.CHEST);
            legs = living.getItemBySlot(EquipmentSlot.LEGS);
            feet = living.getItemBySlot(EquipmentSlot.FEET);
        }

        if (lastHit instanceof Player player) {
            int food = player.getFoodData().getFoodLevel();

        }



        this.setWidth(Math.max(mc.font.width(name), mc.font.width(hp + " / " + maxHp)) + 10);
        setHeight(45);

        graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
        graphics.drawString(mc.font, name, getX() + 5, getY() + 5, 0xFFFFFFFF);
        graphics.drawString(mc.font, hp + " / " + maxHp, getX() + 5, getY() + 15, 0xFFFFFFFF);
        graphics.renderItem(helmet, getX() + 2, getY() + 25);
        graphics.renderItem(chest, getX() + 17, getY() + 25);
        graphics.renderItem(legs, getX() + 32, getY() + 25);
        graphics.renderItem(feet, getX() + 47, getY() + 25);
    }
    //    public Identifier getIcon() {
//        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
//    };

    private final BooleanSetting showArmor =
            new BooleanSetting(
                    "Show Armor",
                    "showArmor",
                    true
            );

    private final BooleanSetting showHealth =
            new BooleanSetting(
                    "Show Health",
                    "showHealth",
                    true
            );

    private final SliderSetting timeout =
            new SliderSetting(
                    "Timeout",
                    "timeout",
                    4.0,
                    1.0,
                    10.0,
                    0.5
            );

    @Override
    public String getId() {
        return "targethud";
    }
}
