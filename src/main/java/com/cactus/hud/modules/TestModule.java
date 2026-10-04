package com.cactus.hud.modules;

import com.cactus.hud.HudModule;
import com.cactus.openui.Modifier;
import com.cactus.openui.OpenUI;
import com.cactus.openui.components.Column;
import com.cactus.openui.components.Text;
import net.minecraft.client.gui.GuiGraphics;

public class TestModule extends HudModule {

    public TestModule() {
        setX(100);
        setY(300);
        setHeight(100);
        setWidth(50);
        setEnabled(false);

        setId("test");
        setName("Test");
    }

    OpenUI ui = new OpenUI(
            new Column(Modifier.modifier().padding(10).width(200).height(100).background(0xFF543132))
                    .child(new Text("Hello"))
                    .child(new Text("OpenUI"))
    );

    @Override
    public void render(GuiGraphics graphics) {
        ui.render(graphics, getX(), getY());
    }
}
