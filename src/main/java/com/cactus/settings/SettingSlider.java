package com.cactus.settings;


import com.cactus.settings.SliderSetting;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class SettingSlider extends AbstractSliderButton {

    private final SliderSetting setting;

    public SettingSlider(
            int x,
            int y,
            int width,
            SliderSetting setting
    ) {
        super(
                x,
                y,
                width,
                20,
                Component.empty(),
                toProgress(setting)
        );

        this.setting = setting;

        updateMessage();
    }

    private static double toProgress(SliderSetting setting) {
        return (setting.getValue() - setting.getMin())
                / (setting.getMax() - setting.getMin());
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(
                setting.getName() + ": " + setting.getValue()
        ));
    }

    @Override
    protected void applyValue() {
        double newValue =
                setting.getMin()
                        + this.value
                        * (setting.getMax() - setting.getMin());

        setting.setValue(newValue);

        updateMessage();
    }
}