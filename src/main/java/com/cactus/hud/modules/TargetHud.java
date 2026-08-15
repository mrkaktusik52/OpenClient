package com.cactus.hud.modules;

import com.cactus.gui.HudEditorScreen;
import com.cactus.hud.HudModule;
import com.cactus.settings.BooleanSetting;
import com.cactus.settings.SliderSetting;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
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

    private static final Identifier HEART_CONTAINER =
            Identifier.withDefaultNamespace("hud/heart/container");

    private static final Identifier HEART_FULL =
            Identifier.withDefaultNamespace("hud/heart/full");

    private static final Identifier HEART_HALF =
            Identifier.withDefaultNamespace("hud/heart/half");

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

    @Override
    public String getName() {
        return "Target HUD";
    }

    private void renderHearts(
            GuiGraphics graphics,
            float health,
            float maxHealth,
            int x,
            int y
    ) {
        int maxHearts = Mth.ceil(maxHealth / 2.0F);
        int healthPoints = Mth.ceil(health);

        for (int i = 0; i < maxHearts; i++) {
            int heartX = x + i * 8;

            // Пустое сердце
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    HEART_CONTAINER,
                    heartX,
                    y,
                    9,
                    9
            );

            int hpIndex = i * 2;

            if (hpIndex + 1 < healthPoints) {
                graphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_FULL,
                        heartX,
                        y,
                        9,
                        9
                );
            } else if (hpIndex < healthPoints) {
                graphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_HALF,
                        heartX,
                        y,
                        9,
                        9
                );
            }
        }
    }

    private void validateTarget() {
        if (lastHit == null) return;

        long now = System.currentTimeMillis();
        long timeoutMs = (long) (timeout.getValue() * 1000.0);

        if (!lastHit.isAlive()
                || lastHit.isRemoved()
                || now - lastTargetTime > timeoutMs) {
            lastHit = null;
        }
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

        ItemStack helmet = ItemStack.EMPTY;
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

        int padding = 5;
        int gap = 2;
        int lineHeight = mc.font.lineHeight;





        int width = mc.font.width(name) + padding * 2;
        int height = padding + lineHeight;

        if (showHealth.getValue()) {
            int heartCount = Mth.ceil(maxHp / 2.0F);

            int heartsWidth = heartCount > 0
                    ? (heartCount - 1) * 8 + 9
                    : 0;

            width = Math.max(
                    width,
                    heartsWidth + padding * 2
            );

            height += gap + 9;
        }

        if (showArmor.getValue()) {
            int armorWidth = 4 * 16 + 3 * gap;

            width = Math.max(
                    width,
                    armorWidth + padding * 2
            );

            height += gap + 16;
        }

        height += padding;

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

        // Имя
        graphics.drawString(
                mc.font,
                name,
                getX() + padding,
                yOffset,
                0xFFFFFFFF
        );

        yOffset += lineHeight;

        if (showHealth.getValue()) {
            yOffset += gap;

            renderHearts(
                    graphics,
                    hp,
                    maxHp,
                    getX() + padding,
                    yOffset
            );

            yOffset += 9;
        }

        if (showArmor.getValue()) {
            yOffset += gap;

            int xOffset = getX() + padding;

            graphics.renderItem(
                    helmet,
                    xOffset,
                    yOffset
            );

            graphics.renderItem(
                    chest,
                    xOffset + 16 + gap,
                    yOffset
            );

            graphics.renderItem(
                    legs,
                    xOffset + (16 + gap) * 2,
                    yOffset
            );

            graphics.renderItem(
                    feet,
                    xOffset + (16 + gap) * 3,
                    yOffset
            );
        }
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
