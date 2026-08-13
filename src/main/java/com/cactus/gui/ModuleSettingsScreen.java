package com.cactus.gui;

import com.cactus.hud.HudModule;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModuleSettingsScreen extends Screen {
    private final Screen parent;
    private final HudModule module;

    public ModuleSettingsScreen(Screen parent, HudModule module) {
        super(Component.literal(module.getName()));
        this.parent = parent;
        this.module = module;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Назад"), btn ->
                this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 - 75, this.height - 28, 150, 20).build());
    }

    @Override
    public boolean isPauseScreen() { return false; }
}