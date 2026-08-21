package com.cactus.gui;

import com.cactus.configs.ConfigManager;
import com.cactus.social.serverintegration.ServerIntegration;
import com.cactus.social.serverintegration.ServerIntegrationManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class IntegrationsScreen extends Screen {

    private final Screen parent;

    public IntegrationsScreen(Screen parent) {
        super(Component.literal("Integrations"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 150;
        int y = 40;
        int width = 300;

        for (ServerIntegration integration
                : ServerIntegrationManager.getIntegrations()) {

            this.addRenderableWidget(
                    Button.builder(
                            Component.literal(integration.getName()),
                            btn -> this.minecraft.setScreen(
                                    new IntegrationSettingsScreen(
                                            this,
                                            integration
                                    )
                            )
                    ).bounds(
                            x,
                            y,
                            width,
                            20
                    ).build()
            );

            y += 24;
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
