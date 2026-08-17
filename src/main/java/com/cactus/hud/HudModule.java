package com.cactus.hud;


import com.cactus.OpenClient;
import com.cactus.settings.BooleanSetting;
import com.cactus.settings.Setting;
import com.cactus.settings.SliderSetting;
import com.cactus.social.notification.Notification;
import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class HudModule {
    private int height;
    private int width;
    private int posX;
    private int posY;
    public boolean enabled = true;
    protected int dragOffsetX;
    protected int dragOffsetY;
    protected boolean isDragging;
    protected final List<Setting<?>> settings = new ArrayList<>();

    private boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= this.posX && mouseX <= this.posX + this.width &&
                mouseY >= this.posY && mouseY <= this.posY + this.height;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY)) {
            this.isDragging = true;

            this.dragOffsetX = (int) (mouseX - this.posX);
            this.dragOffsetY = (int) (mouseY - this.posY);

            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isDragging = false;
        }
        return false;

    }

    public void renderInEditor(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!enabled) return;

        if (this.isDragging) {
            this.posX = mouseX - this.dragOffsetX;
            this.posY = mouseY - this.dragOffsetY;
        }

        graphics.fill(posX - 1, posY - 1, posX + width + 1, posY, 0xFFFFFFFF);
        graphics.fill(posX - 1, posY + height, posX + width + 1, posY + height + 1, 0xFFFFFFFF);
        graphics.fill(posX - 1, posY, posX, posY + height, 0xFFFFFFFF);
        graphics.fill(posX + width, posY, posX + width + 1, posY + height, 0xFFFFFFFF);

        render(graphics);
    }

    public void render(GuiGraphics graphics) {
    }

    public void writeConfig(JsonObject json) {
        json.addProperty("x", getX());
        json.addProperty("y", getY());
        json.addProperty("enabled", enabled);

        JsonObject settingsJson = new JsonObject();

        for (Setting<?> setting : settings) {

            if (setting instanceof BooleanSetting booleanSetting) {
                settingsJson.addProperty(
                        setting.getId(),
                        booleanSetting.getValue()
                );
            } else if (setting instanceof SliderSetting sliderSetting) {
                settingsJson.addProperty(
                        setting.getId(),
                        sliderSetting.getValue()
                );
            }
        }

        json.add("settings", settingsJson);
    }

    public void readConfig(JsonObject json) {
        if (json.has("x")) {
            setX(json.get("x").getAsInt());
        }

        if (json.has("y")) {
            setY(json.get("y").getAsInt());
        }

        if (json.has("enabled")) {
            enabled = json.get("enabled").getAsBoolean();
        }

        if (!json.has("settings")) {
            return;
        }

        JsonObject settingsJson = json.getAsJsonObject("settings");

        for (Setting<?> setting : settings) {

            if (!settingsJson.has(setting.getId())) {
                continue;
            }

            if (setting instanceof BooleanSetting booleanSetting) {
                booleanSetting.setValue(
                        settingsJson.get(setting.getId()).getAsBoolean()
                );
            } else if (setting instanceof SliderSetting sliderSetting) {
                sliderSetting.setValue(
                        settingsJson.get(setting.getId()).getAsDouble()
                );
            }
        }
    }

    public void toggle() {
        enabled = !enabled;
        onToggle();
    }

    protected void onToggle() {
        if (enabled) {
            NotificationManager.push(
                    getName(),
                    "Module enabled",
                    NotificationType.INFO,
                    4000
            );
        } else {
            NotificationManager.push(
                    getName(),
                    "Module disabled",
                    NotificationType.INFO,
                    4000
            );
        }
    }

    protected void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public Identifier getIcon() {
        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
    }

    ;

    public String getId() {
        return "";
    }

    public boolean hasSettings() {
        return !settings.isEmpty();
    }

    public String getName() {
        return "name";
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int posX) {
        this.posX = posX;
    }

    public void setY(int posY) {
        this.posY = posY;
    }

}
