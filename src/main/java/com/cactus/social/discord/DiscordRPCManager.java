package com.cactus.social.discord;

import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;

import java.time.OffsetDateTime;

public final class DiscordRPCManager {

    private static final long APPLICATION_ID = 1538613243717746798L;

    private static IPCClient client;

    private DiscordRPCManager() {
    }

    public static void initialize() {
        if (client != null) {
            return;
        }

        client = new IPCClient(APPLICATION_ID);

        client.setListener(new IPCListener() {

            @Override
            public void onPacketSent(IPCClient client, Packet packet) {

            }

            @Override
            public void onPacketReceived(IPCClient client, Packet packet) {

            }

            @Override
            public void onActivityJoin(IPCClient client, String secret) {

            }

            @Override
            public void onActivitySpectate(IPCClient client, String secret) {

            }

            @Override
            public void onActivityJoinRequest(IPCClient client, String secret, User user) {

            }

            @Override
            public void onReady(IPCClient client) {
                System.out.println("[OpenClient] Discord RPC connected");

                NotificationManager.push(
                        "Discord",
                        "Discord RPC connected",
                        NotificationType.SUCCESS,
                        10000
                );

                updatePresence(
                        "OpenClient Alpha",
                        "In Main Menu"
                );
            }

            @Override
            public void onClose(IPCClient client, JsonObject json) {

            }

            @Override
            public void onDisconnect(
                    IPCClient client,
                    Throwable throwable
            ) {
                NotificationManager.push(
                        "Discord",
                        "Discord RPC disconnected",
                        NotificationType.WARNING,
                        10000
                );
            }
        });

        try {
            client.connect();

        } catch (Exception e) {
            NotificationManager.push(
                    "Discord",
                    "Failed to connect Discord RPC",
                    NotificationType.ERROR,
                    10000
            );

            e.printStackTrace();
        }
    }

    public static void updatePresence(
            String details,
            String state
    ) {
        if (client == null) {
            return;
        }

        if (client.getStatus() != PipeStatus.CONNECTED) {
            return;
        }

        RichPresence presence =
                new RichPresence.Builder()
                        .setDetails(details)
                        .setState(state)
                        .setStartTimestamp(
                                System.currentTimeMillis() / 1000L
                        )
                        .setLargeImage("openclient")
                        .build();

        client.sendRichPresence(presence);
    }

    public static void clearPresence() {
        if (client == null) {
            return;
        }

        if (client.getStatus() != PipeStatus.CONNECTED) {
            return;
        }

        client.sendRichPresence(null);
    }

    public static void shutdown() {
        if (client == null) {
            return;
        }

        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        client = null;
    }
}