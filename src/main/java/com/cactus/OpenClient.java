package com.cactus;

import com.cactus.configs.ConfigManager;
import com.cactus.gui.HudEditorScreen;
import com.cactus.hud.HudModule;
import com.cactus.hud.Module;
import com.cactus.hud.modules.*;
import com.cactus.social.discord.DiscordRPCManager;
import com.cactus.social.notification.NotificationManager;
import com.cactus.social.serverintegration.ServerIntegrationManager;
import com.cactus.social.serverintegration.servers.CubecraftIntegration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OpenClient implements ModInitializer {
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final String MOD_ID = "openclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final List<Module> modules = new ArrayList<>();
    KeyMapping openScreen;
    Minecraft client = Minecraft.getInstance();

    @Override
    public void onInitialize() {

        modules.add(new Fps());
        modules.add(new SprintToggle());
        modules.add(new Coords());
        modules.add(new Biome());
        modules.add(new Ping());
        modules.add(new ArmorStatus());
        modules.add(new PotionHud());
        modules.add(new Keystrokes());
        modules.add(new TargetHud());

        ServerIntegrationManager.register(
                new CubecraftIntegration()
        );


        openScreen = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.cactus.openclient.openhudeditor",
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "binds"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreen.consumeClick()) {
                if (client.player != null) {

                    if (client.screen == null) {
                        client.setScreen(new HudEditorScreen(Component.empty()));
                    } else if (client.screen instanceof HudEditorScreen) {
                        client.setScreen(null);
                    }
                }
            }
        });

		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			String text = message.getString();

			ServerIntegrationManager.onChatMessage(text);
		});

        ConfigManager.load();

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("openclient", "hud_overlay"),
                (graphics, deltaTracker) -> {

                    if (client.screen instanceof HudEditorScreen) return;

                    for (Module module : modules) {
                        if (module.enabled && module instanceof HudModule hudModule) {
                            hudModule.render(graphics);
                        }
                    }

                    if (Minecraft.getInstance().screen == null) {
                        NotificationManager.render(graphics);
                    }
                }
        );


		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			Minecraft mc = Minecraft.getInstance();

            if (client.getCurrentServer() == null) {
                DiscordRPCManager.updatePresence(
                        "Playing Singleplayer",
                        "In a world"
                );

                return;
            }


            if (mc.getCurrentServer() == null) {
				return;
			}

			String host = mc.getCurrentServer().ip;

            ServerIntegrationManager.selectIntegration(host);

            if (ServerIntegrationManager.getActiveIntegration() != null) {
                DiscordRPCManager.updatePresence(
                        "Playing Multiplayer",
                        ServerIntegrationManager.getActiveIntegration().getName()
                );
            } else {
                DiscordRPCManager.updatePresence(
                        "Playing Multiplayer",
                        "Unknown Server"
                );
            }
		});

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            DiscordRPCManager.initialize();
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            DiscordRPCManager.shutdown();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ServerIntegrationManager.clearActiveIntegration();

            DiscordRPCManager.updatePresence(
                    "OpenClient",
                    "In Main Menu"
            );
        });

    }


}
