package com.cactus.hud.modules;

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
import net.minecraft.world.item.ItemStack;

public class TargetHud extends HudModule {

    private final Minecraft mc = Minecraft.getInstance();

    private Entity lastHit = null;
    private long lastTargetTime;

    private static final Identifier HEART_CONTAINER =
            Identifier.withDefaultNamespace("hud/heart/container");

    private static final Identifier HEART_FULL =
            Identifier.withDefaultNamespace("hud/heart/full");

    private static final Identifier HEART_HALF =
            Identifier.withDefaultNamespace("hud/heart/half");

    private static final int HEARTS_PER_ROW = 10;
    private static final int HEART_SIZE = 9;
    private static final int HEART_SPACING = 8;
    private static final int HEART_ROW_SPACING = 10;

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
            int centerX,
            int y
    ) {
        int maxHearts = Mth.ceil(maxHealth / 2.0F);
        int healthPoints = Mth.ceil(health);

        for (int i = 0; i < maxHearts; i++) {
            int row = i / HEARTS_PER_ROW;
            int column = i % HEARTS_PER_ROW;

            int heartsInThisRow = Math.min(
                    HEARTS_PER_ROW,
                    maxHearts - row * HEARTS_PER_ROW
            );

            int rowWidth =
                    (heartsInThisRow - 1) * HEART_SPACING + HEART_SIZE;

            int startX = centerX - rowWidth / 2;

            int heartX = startX + column * HEART_SPACING;
            int heartY = y + row * HEART_ROW_SPACING;

            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    HEART_CONTAINER,
                    heartX,
                    heartY,
                    HEART_SIZE,
                    HEART_SIZE
            );

            int hpIndex = i * 2;

            if (hpIndex + 1 < healthPoints) {
                graphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_FULL,
                        heartX,
                        heartY,
                        HEART_SIZE,
                        HEART_SIZE
                );
            } else if (hpIndex < healthPoints) {
                graphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_HALF,
                        heartX,
                        heartY,
                        HEART_SIZE,
                        HEART_SIZE
                );
            }
        }
    }

    private void validateTarget() {
        if (lastHit == null) {
            return;
        }

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
        if (!enabled) {
            return;
        }

        validateTarget();

        if (lastHit == null) {
            return;
        }

        String name = lastHit.getDisplayName().getString();

        float hp = 0.0F;
        float maxHp = 0.0F;

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

        int heartRows = 0;
        int heartsHeight = 0;

        if (showHealth.getValue()) {
            int heartCount = Mth.ceil(maxHp / 2.0F);

            int heartsInLargestRow =
                    Math.min(heartCount, HEARTS_PER_ROW);

            int heartsWidth = heartsInLargestRow > 0
                    ? (heartsInLargestRow - 1) * HEART_SPACING + HEART_SIZE
                    : 0;

            heartRows = heartCount > 0
                    ? Mth.ceil(heartCount / (float) HEARTS_PER_ROW)
                    : 0;

            heartsHeight = heartRows > 0
                    ? HEART_SIZE + (heartRows - 1) * HEART_ROW_SPACING
                    : 0;

            width = Math.max(
                    width,
                    heartsWidth + padding * 2
            );

            height += gap + heartsHeight;
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
                    getX() + getWidth() / 2,
                    yOffset
            );

            yOffset += heartsHeight;
        }

        if (showArmor.getValue()) {
            yOffset += gap;

            int armorWidth = 4 * 16 + 3 * gap;
            int xOffset = getX() + (getWidth() - armorWidth) / 2;

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