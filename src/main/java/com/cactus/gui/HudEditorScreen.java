package com.cactus.gui;

import com.cactus.OpenClient;
import com.cactus.configs.ConfigManager;
import com.cactus.hud.HudModule;
import com.cactus.hud.Module;
import com.cactus.social.serverintegration.ServerIntegrationManager;
import com.cactus.social.serverintegration.servers.CubecraftIntegration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;


public class HudEditorScreen extends Screen {
    public HudEditorScreen(Component title) {
        super(title);
    }

    private static final Identifier BACKGROUND =
            Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");

    @Override
    protected void init() {
        Button modsButton = Button.builder(Component.literal("Mods"), (btn) -> {
            this.minecraft.setScreen(new ModsListScreen(this));
        }).bounds(
                this.width / 2 - 70,
                this.height / 2 + 30,
                140,
                30
        ).build();

        Button settingsButton = Button.builder(Component.literal("Settings"), (btn) -> {
            this.minecraft.setScreen(new SettingsScreen());
        }).bounds(
                this.width / 2 - 70,
                this.height / 2 + 62,
                140,
                20
        ).build();

        this.addRenderableWidget(modsButton);
        this.addRenderableWidget(settingsButton);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int button = mouseButtonEvent.button();

        for (Module module : OpenClient.modules) {
            if (module instanceof HudModule hudModule) {
                hudModule.mouseReleased(mouseX, mouseY, button);
            }
        }

        return super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int button = mouseButtonEvent.button();

        for (Module module : OpenClient.modules) {
            if (module instanceof HudModule hudModule && hudModule.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int x = (this.width - 128) / 2;
        int y = (this.height - 128) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y, 0, 0, 128, 128, 128, 128);
        for (Module module : OpenClient.modules) {
            if (module instanceof HudModule hudModule) {
                hudModule.renderInEditor(graphics, mouseX, mouseY);
            }
        }
        super.render(graphics, mouseX, mouseY, delta);
    }
}