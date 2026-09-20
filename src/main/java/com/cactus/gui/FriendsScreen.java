package com.cactus.gui;

import com.cactus.social.friends.Friend;
import com.cactus.social.friends.FriendsManager;
import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FriendsScreen extends Screen {
    public FriendsScreen() {
        super(Component.literal("Friends"));
    }
    @Override
    protected void init() {
        int width = 300;
        int height = 20;
        int gap = 4;

        int x = this.width / 2 - width / 2;
        int y = 50;

        for (Friend friend : FriendsManager.getFriends()) {
            this.addRenderableWidget(
                    Button.builder(
                            Component.literal(friend.getName()),
                            btn -> {
                                NotificationManager.push(
                                        friend.getName(),
                                        friend.getName() + " is your friend",
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
        }

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("+"),
                        btn -> {
                            Minecraft.getInstance().setScreen(new AddFriendScreen());
                        }
                ).bounds(
                        x,
                        y,
                        width,
                        height
                ).build()
        );
    }
}
