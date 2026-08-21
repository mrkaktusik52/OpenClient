package com.cactus.social.serverintegration;

import com.cactus.OpenClient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerIntegrationManager {

    private static final List<ServerIntegration> integrations = new ArrayList<>();
    private static ServerIntegration activeIntegration;

    private ServerIntegrationManager() {
    }

    public static void register(ServerIntegration integration) {
        integrations.add(integration);
    }

    public static void selectIntegration(String host) {
        activeIntegration = null;

        for (ServerIntegration integration : integrations) {
            if (integration.supports(host)) {
                activeIntegration = integration;
                break;
            }
        }
    }

    public static void onChatMessage(String message) {
        if (activeIntegration != null) {
            activeIntegration.onChatMessage(message);

            if (message.contains("killed")) {
                OpenClient.LOGGER.info("Message: >>>{}<<<", message);
            }
        }
    }

    public static ServerIntegration getActiveIntegration() {
        return activeIntegration;
    }

    public static void clearActiveIntegration() {
        activeIntegration = null;
    }

    public static List<ServerIntegration> getIntegrations() {
        return List.copyOf(integrations);
    }

    public static Matcher matchMessage(Pattern pattern, String message) {
        Matcher matcher = pattern.matcher(message);

        if (matcher.matches()) {
            return matcher;
        }

        return null;
    }
}