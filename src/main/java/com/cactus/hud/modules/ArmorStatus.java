package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArmorStatus extends HudModule {
    Minecraft client = Minecraft.getInstance();

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public ArmorStatus() {
        setX(100);
        setY(50);
        setHeight(15 * 4 + 1);
        setWidth(16);
    }

    @Override
    public String getName() {
        return "Armor Status";
    }

    private int getColor(ItemStack stack) {
        if (stack.getMaxDamage() <= 0) {
            return 0xFFFFFFFF;
        }

        float percentDurability = (float) (stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage();

        if (percentDurability > 0.75f) {
            return 0xFF55FF55;
        } else if (percentDurability > 0.50f) {
            return 0xFFFFFF55;
        } else if (percentDurability > 0.25f) {
            return 0xFFFFAA00;
        } else {
            return 0xFFFF5555;
        }
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (!enabled) return;

        if (client.player == null) return;

        int yOffset = 0;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = client.player.getItemBySlot(slot);

            if (!stack.isEmpty()) {

                graphics.renderItem(stack, getX(), getY() + yOffset);


                if (stack.isDamageableItem()) {
                    int currentDurability = stack.getMaxDamage() - stack.getDamageValue();
                    String durabilityText = String.valueOf(currentDurability);

                    graphics.drawString(
                            client.font,
                            durabilityText,
                            getX() + 20,
                            getY() + yOffset + 4,
                            getColor(stack),
                            true
                    );
                }
            }

            yOffset += 15;
        }
    }

    @Override
    public boolean hasSettings() {
        return true;
    }
}