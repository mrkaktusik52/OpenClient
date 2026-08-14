package com.cactus.hud;


import com.cactus.OpenClient;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

public abstract class HudModule {
    private int height;
    private int width;
    private int posX;
    private int posY;
    public boolean enabled = true;
    protected int dragOffsetX;
    protected int dragOffsetY;
    protected boolean isDragging;

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

    public void render(GuiGraphics graphics){}
    public void writeConfig(JsonObject json) {
        json.addProperty("x", this.posX);
        json.addProperty("y", this.posY);
        json.addProperty("enabled", this.enabled);
    }
    public void readConfig(JsonObject json) {
        if (json.has("x")) this.posX = json.get("x").getAsInt();
        if (json.has("y")) this.posY = json.get("y").getAsInt();
        if (json.has("enabled")) this.enabled = json.get("enabled").getAsBoolean();
    }

    public Identifier getIcon() {
        return Identifier.fromNamespaceAndPath(OpenClient.MOD_ID, "textures/gui/logo-transperent.png");
    };
    public String getId(){return "";
    }
    public boolean hasSettings() {return true;};
    public String getName() {
        return "name";
    }
    public int getHeight(){return height;}
    public int getWidth(){return width;}
    public int getX(){return posX;}
    public int getY(){return posY;}
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
