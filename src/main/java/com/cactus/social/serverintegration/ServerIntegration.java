package com.cactus.social.serverintegration;

import com.cactus.settings.Setting;

import java.util.List;

public interface ServerIntegration {
    String getId();
    String getName();
    boolean supports(String address);
    void onChatMessage(String message);
    List<Setting<?>> getSettings();
}