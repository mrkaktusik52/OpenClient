package com.cactus.configs;

import com.cactus.OpenClient;
import com.cactus.hud.Module;
import com.cactus.settings.BooleanSetting;
import com.cactus.settings.Setting;
import com.cactus.settings.SliderSetting;
import com.cactus.social.serverintegration.ServerIntegration;
import com.cactus.social.serverintegration.ServerIntegrationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Path MODULES_DIR = FabricLoader.getInstance().getConfigDir().resolve("openclient").resolve("modules");

    private static final Path INTEGRATIONS_DIR = FabricLoader.getInstance().getConfigDir().resolve("openclient").resolve("integrations");

    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("openclient");

    private static final Path CLIENT_FILE = CONFIG_DIR.resolve("client.json");

    private static void ensureDirectories() {
        try {
            Files.createDirectories(CONFIG_DIR);
            Files.createDirectories(MODULES_DIR);
            Files.createDirectories(INTEGRATIONS_DIR);
        } catch (IOException e) {
            OpenClient.LOGGER.error(
                    "Failed to create OpenClient config directories",
                    e
            );
        }
    }

    public static void saveClient() {
        ensureDirectories();

        JsonObject root = new JsonObject();

        JsonObject discord = new JsonObject();

        discord.addProperty("enabled", ClientConfig.discordRpcEnabled);

        discord.addProperty("showServer", ClientConfig.discordShowServer);

        root.add("discordRpc", discord);

        JsonObject notifications = new JsonObject();

        notifications.addProperty("enabled", ClientConfig.notificationsEnabled);

        notifications.addProperty("sound", ClientConfig.notificationSound);

        root.add("notifications", notifications);


        try (BufferedWriter writer = Files.newBufferedWriter(CLIENT_FILE)) {

            GSON.toJson(root, writer);

        } catch (IOException e) {
            OpenClient.LOGGER.error("Failed to save client config", e);
        }
    }

    public static void loadClient() {
        ensureDirectories();

        if (!Files.exists(CLIENT_FILE)) {
            saveClient();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(CLIENT_FILE)) {

            JsonObject root = GSON.fromJson(reader, JsonObject.class);

            if (root == null) {
                return;
            }


            if (root.has("discordRpc")) {
                JsonObject discord = root.getAsJsonObject("discordRpc");

                if (discord.has("enabled")) {
                    ClientConfig.discordRpcEnabled = discord.get("enabled").getAsBoolean();
                }

                if (discord.has("showServer")) {
                    ClientConfig.discordShowServer = discord.get("showServer").getAsBoolean();
                }
            }


            if (root.has("notifications")) {
                JsonObject notifications = root.getAsJsonObject("notifications");

                if (notifications.has("enabled")) {
                    ClientConfig.notificationsEnabled = notifications.get("enabled").getAsBoolean();
                }

                if (notifications.has("sound")) {
                    ClientConfig.notificationSound = notifications.get("sound").getAsBoolean();
                }

            }

        } catch (IOException e) {
            OpenClient.LOGGER.error("Failed to load client config", e);
        }
    }

    public static void saveIntegrations() {
        ensureDirectories();

        for (ServerIntegration integration : ServerIntegrationManager.getIntegrations()) {

            JsonObject settingsJson = new JsonObject();

            for (Setting<?> setting : integration.getSettings()) {

                if (setting instanceof BooleanSetting booleanSetting) {
                    settingsJson.addProperty(setting.getId(), booleanSetting.getValue());

                } else if (setting instanceof SliderSetting sliderSetting) {
                    settingsJson.addProperty(setting.getId(), sliderSetting.getValue());
                }
            }
            Path file = INTEGRATIONS_DIR.resolve(integration.getId() + ".json");

            JsonObject json = new JsonObject();
            json.add("settings", settingsJson);

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                GSON.toJson(json, writer);
            } catch (IOException e) {
                OpenClient.LOGGER.error("Failed to save integration config: " + integration.getId(), e);
            }
        }
    }

    public static void loadIntegrations() {
        ensureDirectories();

        for (ServerIntegration integration : ServerIntegrationManager.getIntegrations()) {

            Path file = INTEGRATIONS_DIR.resolve(integration.getId() + ".json");

            if (!Files.exists(file)) {
                continue;
            }

            try (BufferedReader reader = Files.newBufferedReader(file)) {

                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                if (json == null || !json.has("settings")) {
                    continue;
                }

                JsonObject settingsJson = json.getAsJsonObject("settings");

                for (Setting<?> setting : integration.getSettings()) {


                    if (!settingsJson.has(setting.getId())) {
                        continue;
                    }

                    if (setting instanceof BooleanSetting booleanSetting) {

                        booleanSetting.setValue(settingsJson.get(setting.getId()).getAsBoolean());

                    } else if (setting instanceof SliderSetting sliderSetting) {

                        sliderSetting.setValue(settingsJson.get(setting.getId()).getAsDouble());
                    }
                }

            } catch (IOException e) {
                OpenClient.LOGGER.error("Failed to load integration config: " + integration.getId(), e);
            }
        }
    }

    public static void save() {
        ensureDirectories();

        for (Module module : OpenClient.modules) {
            Path file = MODULES_DIR.resolve(module.getId() + ".json");

            JsonObject json = new JsonObject();

            module.writeConfig(json);

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {

                GSON.toJson(json, writer);

            } catch (IOException e) {
                OpenClient.LOGGER.error("Failed to save module config: " + module.getId(), e);
            }
        }
        saveClient();
        saveIntegrations();
    }

    public static void load() {
        ensureDirectories();

        for (Module module : OpenClient.modules) {
            Path file = MODULES_DIR.resolve(module.getId() + ".json");

            if (!Files.exists(file)) {
                continue;
            }

            try (BufferedReader reader = Files.newBufferedReader(file)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                module.readConfig(json);

            } catch (IOException e) {
                OpenClient.LOGGER.error("Failed to load module config: " + module.getId(), e);
            }
        }

        loadClient();
        loadIntegrations();
    }
}
