package com.cactus.configs;

import com.cactus.OpenClient;
import com.cactus.hud.HudModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Path MODULES_DIR = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("openclient")
            .resolve("modules");

    private static void ensureDirectories() {
        try {
            if (!Files.exists(MODULES_DIR)) {
                Files.createDirectories(MODULES_DIR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        ensureDirectories();

        for (HudModule module : OpenClient.hudModules) {
            Path file = MODULES_DIR.resolve(module.getName() + ".json");
            JsonObject json = new JsonObject();

            module.writeConfig(json);

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                GSON.toJson(json, writer);
            } catch (IOException e) {
                OpenClient.LOGGER.error("Failed to save module config: " + module.getName(), e);
            }
        }
    }

    public static void load() {
        ensureDirectories();

        for (HudModule module : OpenClient.hudModules) {
            Path file = MODULES_DIR.resolve(module.getName() + ".json");

            if (!Files.exists(file)) continue;

            try (BufferedReader reader = Files.newBufferedReader(file)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                module.readConfig(json);
            } catch (IOException e) {
                OpenClient.LOGGER.error("Failed to load module config: " + module.getName(), e);
            }
        }
    }
}
