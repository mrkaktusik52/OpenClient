package com.cactus.social.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.ArrayList;
import java.util.List;

public final class NotificationManager {

    private static final List<Notification> notifications =
            new ArrayList<>();

    private NotificationManager() {
    }

    public static void push(Notification notification) {
        notifications.add(notification);
    }

    public static Notification push(
            String title,
            String message,
            NotificationType type,
            long duration
    ) {
        Notification notification =
                new Notification(
                        title,
                        message,
                        type,
                        duration
                );

        notifications.add(notification);

        return notification;
    }

    public static List<Notification> getNotifications() {
        return notifications;
    }

    public static void clear() {
        notifications.clear();
    }

    public static void render(GuiGraphics graphics) {

        int margin = 10;
        int gap = 5;
        int height = 40;

        for (int i = notifications.size() - 1; i >= 0; i--) {

            Notification notification = notifications.get(i);

            int width = getNotificationWidth(notification);

            float visibleX = graphics.guiWidth() - width - margin;

            float hiddenX = graphics.guiWidth() + width;

            int fromBottom = notifications.size() - 1 - i;

            float targetY = graphics.guiHeight() - margin - height - fromBottom * (height + gap);

            if (!notification.isInitialized()) {
                notification.setCurrentX(hiddenX);
                notification.setCurrentY(targetY);
                notification.setInitialized(true);
            }

            notification.setTargetY(targetY);

            if (notification.getState() == Notification.NotificationState.EXITING) {
                notification.setTargetX(hiddenX);
            } else {
                notification.setTargetX(visibleX);
            }

            notification.update();

            renderNotification(
                    graphics,
                    notification,
                    width,
                    height
            );
        }

        notifications.removeIf(notification -> {

            if (notification.getState()
                    != Notification.NotificationState.EXITING) {
                return false;
            }

            int width = getNotificationWidth(notification);

            float hiddenX = graphics.guiWidth() + width;

            return Math.abs(
                    notification.getCurrentX() - hiddenX
            ) < 1.0f;
        });
    }

    private static int getNotificationWidth(Notification notification) {
        Minecraft mc = Minecraft.getInstance();

        int rightPadding = 6;
        int iconLeftPadding = 9;
        int iconSize = 16;
        int iconGap = 7;

        int titleWidth = mc.font.width(notification.getTitle());
        int messageWidth = mc.font.width(notification.getMessage());

        int textWidth = Math.max(titleWidth, messageWidth);

        return iconLeftPadding + iconSize + iconGap + textWidth + rightPadding;
    }

    private static void renderNotification(
            GuiGraphics graphics,
            Notification notification,
            int width,
            int height
    ) {
        Minecraft mc =
                Minecraft.getInstance();

        int padding = 6;
        int iconSize = 16;

        int iconLeftPadding = 9;
        int iconGap = 7;
        int iconYOffset = -2;

        int x = (int) notification.getCurrentX();

        int y = (int) notification.getCurrentY();

        int iconX = x + iconLeftPadding;
        int iconY = y + (height - iconSize) / 2 + iconYOffset;

        int textX = iconX + iconSize + iconGap;

        int titleY = y + padding;
        int messageY = titleY + mc.font.lineHeight + 3;

        graphics.fill(
                x,
                y,
                x + width,
                y + height,
                0xCC000000
        );

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                notification.getIcon(),
                iconX,
                iconY,
                0,
                0,
                iconSize,
                iconSize,
                iconSize,
                iconSize
        );

        graphics.drawString(
                mc.font,
                notification.getTitle(),
                textX,
                titleY,
                0xFFFFFFFF,
                true
        );

        graphics.drawString(
                mc.font,
                notification.getMessage(),
                textX,
                messageY,
                0xFFCCCCCC,
                true
        );
    }
}