package com.cactus.gui;

import com.cactus.configs.ConfigManager;
import com.cactus.settings.BooleanSetting;
import com.cactus.settings.Setting;
import com.cactus.settings.SettingSlider;
import com.cactus.settings.SliderSetting;
import com.cactus.social.serverintegration.BaseServerIntegration;
import com.cactus.social.serverintegration.ServerIntegration;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class IntegrationSettingsScreen extends Screen {
    private final Screen parent;
    private final ServerIntegration integration;

    public IntegrationSettingsScreen(Screen parent, ServerIntegration integration) {
        super(Component.literal(integration.getName() + " Settings"));
        this.parent = parent;
        this.integration = integration;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 150;
        int y = 50;
        int width = 300;

        for (Setting<?> setting : integration.getSettings()) {

            if (setting instanceof BooleanSetting booleanSetting) {

                Button button = Button.builder(
                        Component.literal(
                                booleanSetting.getName()
                                        + ": "
                                        + (booleanSetting.getValue()
                                        ? "ON"
                                        : "OFF")
                        ),
                        btn -> {
                            booleanSetting.toggle();

                            btn.setMessage(
                                    Component.literal(
                                            booleanSetting.getName()
                                                    + ": "
                                                    + (booleanSetting.getValue()
                                                    ? "ON"
                                                    : "OFF")
                                    )
                            );
                        }
                ).bounds(
                        x,
                        y,
                        width,
                        20
                ).build();

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
                ).bounds(
                        this.width / 2 - 100,
                        this.height - 28,
                        200,
                        20
                ).build()
        );
    }
    @Override
    public void onClose() {
        ConfigManager.save();
        super.onClose();
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
