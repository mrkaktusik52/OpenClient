package com.cactus.gui;

import com.cactus.social.friends.FriendsManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class AddFriendScreen extends Screen {

    private EditBox nameField;

    public AddFriendScreen() {
        super(Component.literal("Add Friend"));
    }

    @Override
    protected void init() {
        super.init();

        int fieldWidth = 200;
        int fieldHeight = 20;
        int x = (this.width - fieldWidth) / 2;
        int y = this.height / 2 - 30;

        nameField = new EditBox(
                this.font,
                x,
                y,
                fieldWidth,
                fieldHeight,
                Component.literal("Player name")
        );

        nameField.setMaxLength(16);
        nameField.setHint(Component.literal("Player name"));
        this.addRenderableWidget(nameField);

        Button addButton = Button.builder(
                Component.literal("Add"),
                button -> addFriend()
        ).bounds(
                x,
                y + 30,
                fieldWidth,
                20
        ).build();

        this.addRenderableWidget(addButton);

        // Сразу ставим курсор в поле
        this.setInitialFocus(nameField);
    }

    private void addFriend() {
        String name = nameField.getValue().trim();

        if (name.isEmpty()) {
            return;
        }

        FriendsManager.addFriend(name);

        this.onClose();
    }



    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 257 || event.key() == 335) {
            addFriend();
            return true;
        }

        return super.keyPressed(event);
    }
}
