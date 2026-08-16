package com.cactus.social.notification;

public enum NotificationType {
    INFO("info.png"),
    SUCCESS("success.png"),
    WARNING("warning.png"),
    ERROR("error.png"),
    SOCIAL("social.png");

    private final String iconName;

    NotificationType(String iconName) {
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }
}