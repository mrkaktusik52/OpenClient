package com.cactus.social.serverintegration;

public interface ServerIntegration {

    String getId();

    String getName();

    boolean supports(String address);

    void onChatMessage(String message);
}