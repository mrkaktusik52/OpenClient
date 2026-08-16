package com.cactus.gui;

import com.cactus.OpenClient;
import com.cactus.hud.HudModule;
import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;


public class ModsListScreen extends Screen {
    private static final Identifier GEAR_ICON =
            Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/gear.png");

    private final Screen parent;
    private ModuleList list;

    public ModsListScreen(Screen parent) {
        super(Component.translatable("key.kaktus.list.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        this.list = new ModuleList(this.minecraft, this.width, this.height - 60, 32, 26);

        for (HudModule module : OpenClient.hudModules) {
            this.list.addModule(module);
        }

        this.addRenderableWidget(this.list);
        this.addRenderableWidget(Button.builder(Component.translatable("key.kaktus.list.back"), btn ->

                this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 - 75, this.height - 28, 150, 20).build());

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {

        super.render(graphics, mouseX, mouseY, delta);


        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private class ModuleList extends ObjectSelectionList<ModuleList.ModuleEntry> {

        private final int itemH;

        public ModuleList(Minecraft mc, int width, int height, int y, int itemHeight) {
            super(mc, width, height, y, itemHeight);
            this.itemH = itemHeight;
        }

        public void addModule(HudModule module) {
            this.addEntry(new ModuleEntry(module));
        }

        @Override
        public int getRowWidth() {
            return Math.min(400, this.width - 20);
        }

        public class ModuleEntry extends ObjectSelectionList.Entry<ModuleEntry> {

            private final HudModule module;
            private final Button toggleButton;

            public ModuleEntry(HudModule module) {
                this.module = module;


                this.toggleButton = Button.builder(
                        Component.translatable(module.enabled ? "key.kaktus.list.on" : "key.kaktus.list.off"),
                        btn -> {

                            module.toggle();

                            btn.setMessage(Component.translatable(module.enabled ? "key.kaktus.list.on" : "key.kaktus.list.off"));
                        }
                ).bounds(0, 0, 44, 18).build();
            }

            @Override
            public @NonNull Component getNarration() {
                return Component.literal(module.getName());
            }

            @Override
            public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
                int index = ModuleList.this.children().indexOf(this);
                int top = ModuleList.this.getRowTop(index);
                int left = ModuleList.this.getRowLeft();
                int width = ModuleList.this.getRowWidth();
                int height = 28;
                int centerY = top + height / 2;

                int bgColor = hovered ? 0x80555555 : 0x60000000;
                graphics.fill(left, top, left + width, top + height - 2, bgColor);

                graphics.drawString(ModsListScreen.this.font, module.getName(), left + 26, centerY - 4, 0xFFFFFFFF);

                this.toggleButton.setX(left + width - 50);
                this.toggleButton.setY(centerY - 11);
                this.toggleButton.render(graphics, mouseX, mouseY, partialTick);

                int gearX = left + width - 71;
                int gearY = centerY - 10;

                if (module.hasSettings()) {
                    graphics.blit(
                            RenderPipelines.GUI_TEXTURED,
                            GEAR_ICON,
                            gearX,
                            gearY,
                            0,
                            0,
                            16,
                            16,
                            16,
                            16
                    );
                }

            }

            @Override
            public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
                int index = ModuleList.this.children().indexOf(this);
                int top = ModuleList.this.getRowTop(index);
                int left = ModuleList.this.getRowLeft();
                int width = ModuleList.this.getRowWidth();
                int centerY = top + ModuleList.this.itemH / 2;

                this.toggleButton.setX(left + width - 50);
                this.toggleButton.setY(centerY - 9);

                if (this.toggleButton.mouseClicked(event, doubleClick)) {
                    return true;
                }

                double mx = event.x();
                double my = event.y();

                int gx = left + width - 71;

                if (module.hasSettings()
                        && mx >= gx
                        && mx <= gx + 16
                        && my >= centerY - 8
                        && my <= centerY + 8) {

                    Minecraft.getInstance().setScreen(
                            new ModuleSettingsScreen(ModsListScreen.this, module)
                    );

                    return true;
                }

                return false;
            }
        }
    }
}