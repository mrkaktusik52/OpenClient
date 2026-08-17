package com.cactus.social.serverintegration.servers;

import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import com.cactus.social.serverintegration.ServerIntegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cactus.social.serverintegration.ServerIntegrationManager;

public class CubecraftIntegration implements ServerIntegration {

    private static final Pattern PARTY_INVITE =
            Pattern.compile("^You have received a party invite from (.+)\\.$");

    private static final Pattern KILL = Pattern.compile(
            "^\\+3 Points$");

    @Override
    public String getId() {
        return "cubecraft";
    }

    @Override
    public String getName() {
        return "CubeCraft";
    }

    @Override
    public boolean supports(String host) {
        return host.equals("cubecraft.net")
                || host.endsWith(".cubecraft.net");
    }

    @Override
    public void onChatMessage(String message) {
        Matcher partyMatcher = ServerIntegrationManager.matchMessage(PARTY_INVITE, message);
        Matcher killMatcher = ServerIntegrationManager.matchMessage(KILL, message);

        if (partyMatcher != null) {
            String playerName = partyMatcher.group(1);

            NotificationManager.push(
                    "[" + this.getName() + "]" + " Party invite",
                    playerName + " invited you to a party",
                    NotificationType.SOCIAL,
                    5000
            );
        }
        if (killMatcher != null) {
            NotificationManager.push(
                    "[" + this.getName() + "] Kill",
                    "You killed a player",
                    NotificationType.INFO,
                    3000
            );
        }
    }
}