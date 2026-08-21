package com.cactus.gui;

import com.cactus.configs.ConfigManager;
import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends Screen {
    public SettingsScreen() {
        super(Component.literal("Settings"));
    }
    @Override
    protected void init() {
        int width = 300;
        int height = 20;
        int gap = 4;

        int x = this.width / 2 - width / 2;
        int y = 50;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("General"),
                        btn -> {
                            NotificationManager.push(
                                    "General",
                                    "Nothing to show yet",
                                    NotificationType.INFO,
                                    2000
                            );
                        }
                ).bounds(
                        x,
                        y,
                        width,
                        height
                ).build()
        );

        y += height + gap;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Discord RPC"),
                        btn -> {
                            NotificationManager.push(
                                    "Discord RPC",
                                    "Nothing to show yet",
                                    NotificationType.INFO,
                                    2000
                            );
                        }
                ).bounds(
                        x,
                        y,
                        width,
                        height
                ).build()
        );

        y += height + gap;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Notifications"),
                        btn -> {
                            NotificationManager.push(
                                    "Notifications",
                                    "Nothing to show yet",
                                    NotificationType.INFO,
                                    2000
                            );
                        }
                ).bounds(
                        x,
                        y,
                        width,
                        height
                ).build()
        );

        y += height + gap;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Servers Integrations"),
                        btn -> {
                            this.minecraft.setScreen(new IntegrationsScreen(this));
                        }
                ).bounds(
                        x,
                        y,
                        width,
                        height
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
