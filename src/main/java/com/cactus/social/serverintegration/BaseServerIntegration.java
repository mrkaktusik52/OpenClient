package com.cactus.social.serverintegration;

import com.cactus.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseServerIntegration implements ServerIntegration {

    protected final List<Setting<?>> settings = new ArrayList<>();

    protected void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public boolean hasSettings() {
        return !settings.isEmpty();
    }
}