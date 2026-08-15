package com.cactus.settings;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String id, boolean defaultValue) {
        super(name, id, defaultValue);
    }

    public void toggle() {
        setValue(!getValue());
    }
}