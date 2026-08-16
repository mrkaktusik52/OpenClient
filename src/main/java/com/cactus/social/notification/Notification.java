package com.cactus.social.notification;

import com.cactus.OpenClient;
import net.minecraft.resources.Identifier;

public class Notification {

    private final String title;
    private final String message;
    private final NotificationType type;
    private final long duration;
    private final long createdAt;
    private NotificationState state = NotificationState.ENTERING;

    private float currentX;
    private float currentY;
    private float targetX;
    private float targetY;

    private boolean initialized = false;

    public enum NotificationState {
        ENTERING,
        VISIBLE,
        EXITING
    }

    public Notification(
            String title,
            String message,
            NotificationType type,
            long duration
    ) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.duration = duration;
        this.createdAt = System.currentTimeMillis();
    }


    public void update() {

        if (state != NotificationState.EXITING) {

            long elapsed =
                    System.currentTimeMillis() - createdAt;

            if (elapsed >= duration) {
                state = NotificationState.EXITING;
            }
        }


        currentX += (targetX - currentX) * 0.15f;
        currentY += (targetY - currentY) * 0.15f;

        if (state == NotificationState.ENTERING
                && Math.abs(currentX - targetX) < 1.0f) {

            currentX = targetX;
            state = NotificationState.VISIBLE;
        }
    }


    public void exit() {
        state = NotificationState.EXITING;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public long getDuration() {
        return duration;
    }

    public NotificationState getState() {
        return state;
    }

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Identifier getIcon() {
        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/notifications/" + this.getType().getIconName());
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}