package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TotemCounter extends HudModule {
    public TotemCounter() {
        setX(100);
        setY(500);
        setName("Totem Counter");
        setId("totem_counter");

        setHeight(20);
        setWidth(20);
    }



    @Override
    public void render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();

        int count = 0;

        assert mc.player != null;
        for (ItemStack stack : mc.player.getInventory().getNonEquipmentItems()) {
            if (stack.is(Items.TOTEM_OF_UNDYING)) {
                count += stack.getCount();
            }
        }

        graphics.renderItem(new ItemStack(Items.TOTEM_OF_UNDYING), getX(), getY());
        graphics.drawString(
                mc.font,
                String.valueOf(count),
                getX() + 8,
                getY() + 10,
                0xFFFFFFFF,
                true
        );
    }
}
