package com.cactus.gui;

import com.cactus.hud.HudModule;
import com.cactus.settings.BooleanSetting;
import com.cactus.settings.Setting;
import com.cactus.settings.SettingSlider;
import com.cactus.settings.SliderSetting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModuleSettingsScreen extends Screen {
    private final Screen parent;
    private final HudModule module;

    public ModuleSettingsScreen(Screen parent, HudModule module) {
        super(Component.literal(module.getName() + " Settings"));
        this.parent = parent;
        this.module = module;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 150;
        int y = 50;
        int width = 300;

        for (Setting<?> setting : module.getSettings()) {

            if (setting instanceof BooleanSetting booleanSetting) {

                Button button = Button.builder(
                        Component.literal(
                                booleanSetting.getName()
                                        + ": "
                                        + (booleanSetting.getValue() ? "ON" : "OFF")
                        ),
                        btn -> {
                            booleanSetting.toggle();

                            btn.setMessage(Component.literal(
                                    booleanSetting.getName()
                                            + ": "
                                            + (booleanSetting.getValue() ? "ON" : "OFF")
                            ));
                        }
                ).bounds(x, y, width, 20).build();

                this.addRenderableWidget(button);

                y += 24;
            } else if (setting instanceof SliderSetting sliderSetting) {

                SettingSlider slider = new SettingSlider(
                        x,
                        y,
                        width,
                        sliderSetting
                );

                this.addRenderableWidget(slider);

                y += 24;
            }
        }

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        btn -> this.minecraft.setScreen(parent)
                ).bounds(this.width / 2 - 100, this.height - 28, 200, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}